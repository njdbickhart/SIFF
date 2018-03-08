/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siff;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Derek.Bickhart
 */
public class BufferedFastaReader {
    private final FileReader fasta;
    // Buffer will handle 5 megabases at a time for now.
    private final char[] buffer = new char[5000000];
    // This is an index to the last section of the buffer for the next chromosome
    private int lastIdx = 0;
    private int lastLen = 0;
    //private int lastCurPos = 0;
    private String curChr = "N/A";
    
    public BufferedFastaReader(String fastaFile) throws FileNotFoundException{
        this.fasta = new FileReader(fastaFile);
    }
    
    public StatContainer readToNextChr(BufferedWriter bed) throws IOException{
        StatContainer stats = new StatContainer();
        
        char[] chrname = new char[256]; 
        int chrnamebuff = 0, currentpos = 0, currentrun = 0, charRead = 0;
        if(lastIdx > 0 && lastLen > 0){
            getChrName(chrname, chrnamebuff);
            lastIdx++;
            
            int[] ret = processBufferedChunk(lastIdx, lastLen, 0, 0, stats, bed);
            if(ret[0] == 0){
                // The end of the chromosome was reached! set the lengths
                this.lastIdx = ret[1];
                stats.setChrInfo(this.curChr, ret[2]);
                return stats;
            }else{
                currentpos = ret[0];
                currentrun = ret[1];
            }
        }else{
            // We're starting fresh            
            if((charRead = fasta.read(buffer)) != -1){
                // We need to get the chromosome name!
                getChrName(chrname, chrnamebuff);
                this.lastIdx++;
                int[] ret = processBufferedChunk(this.lastIdx, charRead, currentpos, currentrun, stats, bed);
                if(charRead == -1)
                    return null;
                else if(ret[0] == 0){
                    this.lastIdx = ret[1];
                    this.lastLen = charRead;
                    stats.setChrInfo(this.curChr, ret[2]);
                    return stats; // reached the end of the chromosome! Storing the remainder in the buffer!
                }else{
                    currentpos = ret[0];
                    currentrun = ret[1];
                }
            }else{
                return null;
            }

        }
        
        while(true){
            if((charRead = fasta.read(buffer)) != -1){
                int[] ret = processBufferedChunk(0, charRead, currentpos, currentrun, stats, bed);
                if(charRead == -1)
                    return null; // reached the end of the file!
                if(ret[0] == 0){
                    this.lastIdx = ret[1];
                    this.lastLen = charRead;
                    stats.setChrInfo(this.curChr, ret[2]);
                    break; // reached end of chromosome!
                }else{
                    currentpos = ret[0];
                    currentrun = ret[1];
                }
            }else{
                return null;
            }
        }
        
        return stats;
    }
    
    public void close() throws IOException{
        this.fasta.close();
    }

    private void getChrName(char[] chrname, int chrnamebuff) {
        // We last read part of the previous chromosome
        while(true){
            if(lastIdx >= buffer.length)
                break;
            // get the chromosome name
            if(buffer[lastIdx] == '>'){
                this.lastIdx++;
                continue;
            }else if(buffer[lastIdx] == '\n')
                break;
            else
                chrname[chrnamebuff] = buffer[lastIdx];
            
            chrnamebuff++;
            lastIdx++;
        }
        this.curChr = String.copyValueOf(chrname).trim();
    }
    
    // Returns three integers, first integer is the currentposition, second is the current run of N's, the third integer is only used if the first integer is zero
    // If the current position returned is 0, then the end of the chr was reached, and the second int is the last index value and the third is the current position
    private int[] processBufferedChunk(int idx, int len, int currentPos, int currentRun, StatContainer stats, BufferedWriter bed) throws IOException{
        for(int x = idx; x < len; x++){
            if(buffer[x] == 'N' || buffer[x] == 'n' || buffer[x] == 'X'){
                 currentRun++;
            }else if (buffer[x] == '\n' || buffer[x] == '\r' || buffer[x] == ' '){
                // whitespace found! not counting
                continue;
            }else if (buffer[x] == '>'){
                // Reached the end of the chromosome!
                if(currentRun > 0){
                    stats.addMaskLen(currentRun);
                    int start = currentPos - currentRun + 1;
                    bed.write(this.curChr + "\t" + start + "\t" + currentPos + System.lineSeparator());
                    currentRun = 0;
                }
                int[] ret = {0, x, currentPos};
                return ret;
            }else if(currentRun > 0){
                stats.addMaskLen(currentRun);
                int start = currentPos - currentRun + 1;
                bed.write(this.curChr + "\t" + start + "\t" + currentPos + System.lineSeparator());
                currentRun = 0;
            }
            currentPos++;
        }
        int[] ret = {currentPos, currentRun, 0};
        return ret;
    }
}
