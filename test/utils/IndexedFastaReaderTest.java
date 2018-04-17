/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dbickhart
 */
public class IndexedFastaReaderTest {
    private static IndexedFastaReader instance; 

    private static final String[] fastaArray ={
        ">tig00123512",
        "CGTCGCCGCCTCGGCCCTTTGGGCCGCCTCCTTCGGAGGACGGCGCCTCCTCCCCGCACC",
        "TGGAACCGGAGGAGCTTGCCCATCTTCCCGAAGTTCCGTCTTTTATCTACAATATAGTCA",
        "CTCTCGAGATTGGAGGCTCATGTCTCACTTTCGGGGCATGGTGCACCTACACCTACACCC",
        "CCCCCTTATAGGGGGTGTAGGGGGTGTAGGGGTTAGGGGTGGGGCTCATTCGTCTGTTTC",
        "CTCGAACATTTCCTGGCGTTCCGATCTTCATTGGTTTTCCGTCTTTCCACCTGATGCGTG",
        "GTGATTACATGCATGTCGAGGCCTTGTTGTACCAGTTTCGCAGCGTTGTGTCGCTGCCGA",
        "TGGAGTAGTTGCTCCGAAGCGCCTCGAACATTGCGCGCTGGAGTCGAAGGGCTTTCCGTT",
        "CCCTTGATGCCTCGAGACGCGTTTGACGCTGACAGGTAGCTTCTCGCCGTCCTGGCGGGA",
        "TTTGGGCCTCGCCGCGCGCGCCTCGTAAATCGTCGCCTCGCGCCAGAATATCAGCCCGGT",
        "GTCATCCC",
        ">tig02644451",
        "AGGAACCGGACATAGATCTGAATTTCGCATCGGAAATCCAGGAACTTCTTCAGAATTACG",
        "TTAAGGATTTACCCGGCGTGGGAAACGTATGTTACGGCGGAACCATTGGAAGATTGATGG",
        "AGAAGACTGCCAGGATGTATGTGGAGGAATACTACCATCATAATGATCTTCCTCTTCCGG",
        "GTAAAAGATCATAAGAGAACAGACAGAAGCTATTATCGGCACTAAGAGAAGTGATGGCTT",
        "TCATCCCGGTGGTATTATTGTTGTTCCAATCGGTGAGGAACTCGTATCCTTTACACCGCT",
        "TCAGCACCCTTGGAACAGTGATCGTATTACTACACATTTTGATTATCATGCAATCGATAA",
        "AAATCTTTTAAAGCTGGATATTCTTGGGCTTTGGCATATGAGCCTTCTGCACGAGCTGCA",
        "GGAACTGACAGGTGTTATGACAGATTCAATCCCGCTTGAAGACAGGAAAGTGCTTGATGT",
        "GATATTGGATCCTGAAGCTGGGGAAATCGAGGGGCTTCCTGAATTTGGCAGTGAATATGC",
        ">tig00150055",
        "ACCGCATGAATGCACAAGTGAATAAACTTTTTAAAACGACATCATAGACAATGAAAATGA",
        "CTACTTTTGCAACGACTTCGGCGATGGATTTCCATCGGCTTCAATTTTGGCAGGCCTCCG",
        "CCAACGGCTCTGACCGACCTTTATTCCGGGAGTGTCCAACAGGACAGACGGCTCCATCGT",
        "TGATGGGATTGCAAGTTGTGCGTTTGTTACCACAAAACGCCACTTGCCAGACCTTCGGCT",
        "GGGAGGAAAGTCTGTTCGCAACTCACAGACTATTACAGAGACCATGAAAGATTCCGGAAA",
        "CAAAGACCGATCGTAAGACTCCGAGTAAGCGGTGCGGAAAGGGATATGTTCGAGGCAAAG",
        "GCGGCTAACTATGGCTCCATCAGCGCCATGATACGTGATGCGGTAGCCCACTATGATGAC",
        "ACGCTCATGAAGCGTCGTATCGAGTCGATGAATCTGCTTTATCCGCTCATATCGAAGCAC",
        "GAGGCAGATCTGAACCGCATCGGAAACAACCTCAATCAGATAGCGCATTACTGCAACGTG",
        "GGGAGAGTTTTTTTTTTAGAGAGAGAGTATGATA"
    };
    
    private static void WriteOut(String[] data, File fileName){
        Path output = fileName.toPath();
        output.toFile().deleteOnExit();
        try(BufferedWriter writer = Files.newBufferedWriter(output, Charset.defaultCharset())){
            for(String d : data){
                writer.write(d);
                writer.write(System.lineSeparator());
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
    
    public IndexedFastaReaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        File temp = null;
        try {
            temp = File.createTempFile("test", ".fa");
            temp.deleteOnExit();
        } catch (IOException ex) {
            Logger.getLogger(IndexedFastaReaderTest.class.getName()).log(Level.SEVERE, "Could not create output temp file!", ex);
        }
        
        WriteOut(fastaArray, temp);
        instance = new IndexedFastaReader(temp.toPath());
    }
    
    @AfterClass
    public static void tearDownClass() {

    }

    /**
     * Test of getForwardSeq method, of class IndexedFastaReader.
     */
    @Test
    public void testGetForwardSeq_3args() {
        System.out.println("getForwardSeq");
        String chr = "tig02644451";
        long start = 1L;
        long end = 60L;
        String expResult = "AGGAACCGGACATAGATCTGAATTTCGCATCGGAAATCCAGGAACTTCTTCAGAATTACG";
        List<Character> result = instance.getForwardSeq(chr, start, end);
        assertEquals(expResult, result.stream().map(s -> s.toString()).reduce((acc, s) -> acc + s).get());
        
        System.out.println("Now testing between the lines...");
        expResult = "ATTACGTTAAGGATTTACCCGGCGTGGGAAACGTATGTTACGGCGGAACCATTGGAAGATTGATGGAGAAGACTGCCAGGATGTATGTGGAGGAATACTACCATCATAATGATCTTCCTCTTCCGGGTAAAAGATC";
        result = instance.getForwardSeq(chr, 55L, 190L);
        String finalResult = result.stream().map(s -> s.toString()).reduce((acc, s) -> acc + s).get();
        assertEquals(expResult, finalResult);
    }

    /**
     * Test of getRevSeq method, of class IndexedFastaReader.
     */
    @Test
    public void testGetRevSeq_3args() {
        System.out.println("getRevSeq");
        String chr = "tig00150055";
        long start = 1L;
        long end = 60L;
        List<Character> result = instance.getRevSeq(chr, start, end);
        String expResult = "TCATTTTCATTGTCTATGATGTCGTTTTAAAAAGTTTATTCACTTGTGCATTCATGCGGT";
        String finalResult = result.stream().map(s -> s.toString()).reduce((acc, s) -> acc + s).get();
        assertEquals(expResult, finalResult);

    }
    
}
