/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 * @author dbickhart
 */
public class IndexedFastaReader {
    private final Path Input;
    private String CurHead;
    private String NextHead;
    private final List<Byte> seq = new ArrayList<>();
    private Map<String, FastaIndexEntry> indexMap;
    // A = 0, T = 1, G = 2, C =3, N = 4
    private final char[] codes = {'A', 'T', 'G', 'C', 'N'};
    private final char[] comp = {'T', 'A', 'C', 'G', 'N'};
    private boolean started = false;
    private static final Logger log = Logger.getLogger(IndexedFastaReader.class.getName());
    
    public IndexedFastaReader(Path Input){
        this.Input = Input;
        File index = new File(Input.toString() + ".fai");
        if(!this.Input.toFile().canRead()){
            log.log(Level.SEVERE, "Could not find input fasta file! Exiting...");
            System.exit(-1);
        }
        if(!index.canRead()){
            log.log(Level.WARNING, "Could not find fasta index file for: " + Input.toString() + "! Attempting to generate one now...");
            GenerateIndex(this.Input, index);
        }else{        
            try(BufferedReader input = Files.newBufferedReader(Paths.get(index.toURI()), Charset.defaultCharset())){
                indexMap = input.lines().map((s) -> new FastaIndexEntry(s))
                        .collect(Collectors.toMap(x -> x.getName(),x -> x));
            }catch(IOException ex){
                log.log(Level.SEVERE, "Error opening fasta index file!", ex);
                System.exit(-1);
            }
        }
    }
    
    private enum FaiState{
        START, // Default state
        CHRINTERNAL, // Has a contig name
        CHRCOUNT, // Need to count sequence lines
        LCONSISTENCY, // Flag to make sure that line lengths are consistent
        BCONSISTENCY, // Flag to make sure that line bp lengths are consistent
        PREVSPACE; // error flag to ensure that there are no double-spaces in the fasta
    }
    
    public int GenerateIndex(Path Input, File index){
        long len = 0, lineLen = -1, lineBpLen = -1, charlen = 0; 
        long offset = 0;
        String contig = null;
        FaiState state = FaiState.START;
        final String nl = System.lineSeparator();
        
        this.indexMap = new HashMap<>();
        try(BufferedReader input = Files.newBufferedReader(Input, Charset.defaultCharset())){
            String line = null;
            while((line = input.readLine()) != null){
                if(line.equals(nl)){
                    // An empty line is only tolerated in the middle of the file to delineate Fasta entries
                    if(state == FaiState.START)
                        IndexFailure("Error! The fasta file is not allowed to start with an empty line!");
                    else if(state == FaiState.PREVSPACE)
                        IndexFailure("Error! Identified two empty lines in a row! Please check to see if the file is not malformed.");
                    else{
                        state = FaiState.PREVSPACE;
                        offset += nl.length();
                    }
                }
                
                if(line.startsWith(">")){
                    if(state == FaiState.CHRCOUNT || state == FaiState.BCONSISTENCY || state == FaiState.LCONSISTENCY){
                        FastaIndexEntry temp = new FastaIndexEntry(contig, len, offset, lineBpLen, lineLen);
                        if(this.indexMap.containsKey(contig))
                            log.log(Level.WARNING, "Warning! Ignoring duplicate fasta entry: " + contig);
                        else{
                            this.indexMap.put(contig, temp);
                            log.log(Level.FINE, "Generated index entry: " + temp.getOutFormat());
                        }
                    }else if(state == FaiState.CHRINTERNAL)
                        IndexFailure("Error! Encounted a Fasta index entry of zero length: " + contig);
                    charlen += line.length() + nl.length();
                    offset = charlen;
                    String[] segs = line.trim().split("\\s+");
                    contig = segs[0].replaceFirst(">", "");
                    state = FaiState.CHRINTERNAL;
                    len = 0;
                }else if(state == FaiState.START)
                    IndexFailure("Error! Did not encounter a valid fasta header sequence! The first line consisted of: " + line);
                else if(state == FaiState.CHRINTERNAL){
                    // We need to calculate the lineLen and lineBpLen for this chromosome
                    lineLen = line.length() + nl.length();
                    lineBpLen = line.length();
                    len += lineBpLen;
                    charlen += lineLen;
                    state = FaiState.CHRCOUNT;
                }else if(state == FaiState.PREVSPACE){
                    // Encountered a space and then a non-fasta entry
                    IndexFailure("Error! Encountered a space and then this non-fasta line: " + line);
                }else if(state == FaiState.LCONSISTENCY || state == FaiState.BCONSISTENCY)
                    IndexFailure("Error! Ran into an inconsistency in Fasta line lengths: " + state.name());
                else{
                    // Now, we have to be within the CHRCOUNT state
                    if(line.length() + nl.length() != lineLen)
                        state = FaiState.LCONSISTENCY;
                    charlen += line.length() + nl.length();
                    if(line.length() != lineBpLen)
                        state = FaiState.BCONSISTENCY;
                    len += line.length();
                }
                
            }
            
            // Now to store the results of the last entry
            if(len > 0 && contig != null){
                FastaIndexEntry temp = new FastaIndexEntry(contig, len, offset, lineBpLen, lineLen);
                if(this.indexMap.containsKey(contig))
                    log.log(Level.WARNING, "Warning! Ignoring duplicate fasta entry: " + contig);
                else{
                    this.indexMap.put(contig, temp);
                    log.log(Level.FINE, "Generated index entry: " + temp.getOutFormat());
                }
            }
        }catch(IOException ex){
            log.log(Level.SEVERE, "Error in reading fasta file during index generation!", ex);
        }
        
        // Now to print the index file
        try(BufferedWriter output = Files.newBufferedWriter(index.toPath(), Charset.defaultCharset())){
            this.indexMap.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.comparing(FastaIndexEntry::GetStartByte)))
                    .forEach((s) -> {
                        try {
                            output.write(s.getValue().getOutFormat());
                        } catch (IOException ex) {
                            log.log(Level.SEVERE, "Error writing fasta index entry to fai: " + s.getValue().getOutFormat(), ex);
                        }
                    });
        }catch(IOException ex){
            log.log(Level.SEVERE, "Error in writing index file for fasta!", ex);
        }
        return 0;
    }
    
    private void IndexFailure(String message){
        log.log(Level.SEVERE, message);
        System.exit(-1);
    }
    
    public void LoadEntry(String chr, long start, long end){
        // Empty previous container
        this.seq.clear();
        start--; // Take care of 1 base sequence lookup
        
        // Check validity of search
        FastaIndexEntry entry = this.indexMap.get(chr);
        long len = end - start;
        
        if(start < 0 || end < 0)
            IndexFailure("Error loading fasta file entry using coordinates: " + chr + ":" + start + "-" + end);
        if(start > end){
            long temp = start;
            start = end;
            end = temp;
        }
        if(end > entry.length)
            end = entry.length;
        try(RandomAccessFile fasta = new RandomAccessFile(this.Input.toFile(), "r")){
            // Indexing strategy similar to the samtools byte count
            fasta.seek(entry.startByte
                    + start / entry.lineLen * entry.totLen
                    + start % entry.totLen
            );
            
            int c = 0;
            
            while(c < len){
                byte b = fasta.readByte();
                if(isBase(b)){
                    c++;
                    this.seq.add(convertNucToCode((char)b));
                }                    
            }
        }catch(IOException ex){
            log.log(Level.SEVERE, "Error subsectioning fasta entry on coordinates: " + chr + ":" + start + "-" + end, ex);
            System.exit(-1);
        }
    }
    
    private boolean isBase(byte b){
        switch(b){
            case 'a':
            case 't':
            case 'g':
            case 'c':
            case 'n':
            case 'A':
            case 'T':
            case 'G':
            case 'C':
            case 'N':
                return true;
            default:
                return false;
        }
    }
    
    public void LoadEntry(String chr){
        FastaIndexEntry entry = this.indexMap.get(chr);
        this.LoadEntry(chr, 1, entry.length);
    }
    
    private byte convertNucToCode(char c){
        switch(c){
            case 'a':
            case 'A':
                return (byte) 0;
            case 't':
            case 'T':
                return (byte) 1;
            case 'g':
            case 'G':
                return (byte) 2;
            case 'c':
            case 'C':
                return (byte) 3;
            default:
                return (byte) 4;
        }
    }
        
    public long getChrLen(String chr){
        return this.indexMap.get(chr).length;
    }
    
    public Set<String> getChrNames(){
        return this.indexMap.keySet();
    }
    
    public String getHead(){
        return this.CurHead;
    }
    
    public Path getPath(){
        return this.Input;
    }
    
    public void clearSeq(){
        this.seq.clear();
    }
    
    public List<Character> getForwardSeq(String chr){
        this.LoadEntry(chr);
        return this.seq.stream().sequential()
                .map(i -> this.codes[i])
                .collect(Collectors.toList());
    }
    
    public List<Character> getForwardSeq(String chr, long start, long end){
        this.LoadEntry(chr, start, end);
        return this.seq.stream().sequential()
                .map(i -> this.codes[i])
                .collect(Collectors.toList());
    }
    
    public List<Character> getRevSeq(String chr){
        this.LoadEntry(chr);
        return this.revRange(0, this.seq.size())
                .mapToObj(i -> this.comp[this.seq.get(i)])
                .collect(Collectors.toList());
    }
    
    public List<Character> getRevSeq(String chr, long start, long end){
        this.LoadEntry(chr, start, end);
        return this.revRange(0, this.seq.size())
                .mapToObj(i -> this.comp[this.seq.get(i)])
                .collect(Collectors.toList());
    }
    
    private IntStream revRange(int from, int to){
        return IntStream.range(from, to)
                    .map(i -> to - i + from - 1);
    }
}
