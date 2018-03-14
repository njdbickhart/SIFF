/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author dbickhart
 */
public class IndexedFastaReaderTest {
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
    
    public IndexedFastaReaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of GenerateIndex method, of class IndexedFastaReader.
     */
    @Test
    public void testGenerateIndex() {
        System.out.println("GenerateIndex");
        Path Input = null;
        File index = null;
        IndexedFastaReader instance = null;
        int expResult = 0;
        int result = instance.GenerateIndex(Input, index);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of LoadEntry method, of class IndexedFastaReader.
     */
    @Test
    public void testLoadEntry_3args() {
        System.out.println("LoadEntry");
        String chr = "";
        long start = 0L;
        long end = 0L;
        IndexedFastaReader instance = null;
        instance.LoadEntry(chr, start, end);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of LoadEntry method, of class IndexedFastaReader.
     */
    @Test
    public void testLoadEntry_String() {
        System.out.println("LoadEntry");
        String chr = "";
        IndexedFastaReader instance = null;
        instance.LoadEntry(chr);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChrLen method, of class IndexedFastaReader.
     */
    @Test
    public void testGetChrLen() {
        System.out.println("getChrLen");
        String chr = "";
        IndexedFastaReader instance = null;
        long expResult = 0L;
        long result = instance.getChrLen(chr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getChrNames method, of class IndexedFastaReader.
     */
    @Test
    public void testGetChrNames() {
        System.out.println("getChrNames");
        IndexedFastaReader instance = null;
        Set<String> expResult = null;
        Set<String> result = instance.getChrNames();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getHead method, of class IndexedFastaReader.
     */
    @Test
    public void testGetHead() {
        System.out.println("getHead");
        IndexedFastaReader instance = null;
        String expResult = "";
        String result = instance.getHead();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPath method, of class IndexedFastaReader.
     */
    @Test
    public void testGetPath() {
        System.out.println("getPath");
        IndexedFastaReader instance = null;
        Path expResult = null;
        Path result = instance.getPath();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearSeq method, of class IndexedFastaReader.
     */
    @Test
    public void testClearSeq() {
        System.out.println("clearSeq");
        IndexedFastaReader instance = null;
        instance.clearSeq();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getForwardSeq method, of class IndexedFastaReader.
     */
    @Test
    public void testGetForwardSeq_String() {
        System.out.println("getForwardSeq");
        String chr = "";
        IndexedFastaReader instance = null;
        List<Character> expResult = null;
        List<Character> result = instance.getForwardSeq(chr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getForwardSeq method, of class IndexedFastaReader.
     */
    @Test
    public void testGetForwardSeq_3args() {
        System.out.println("getForwardSeq");
        String chr = "";
        long start = 0L;
        long end = 0L;
        IndexedFastaReader instance = null;
        List<Character> expResult = null;
        List<Character> result = instance.getForwardSeq(chr, start, end);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRevSeq method, of class IndexedFastaReader.
     */
    @Test
    public void testGetRevSeq_String() {
        System.out.println("getRevSeq");
        String chr = "";
        IndexedFastaReader instance = null;
        List<Character> expResult = null;
        List<Character> result = instance.getRevSeq(chr);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getRevSeq method, of class IndexedFastaReader.
     */
    @Test
    public void testGetRevSeq_3args() {
        System.out.println("getRevSeq");
        String chr = "";
        long start = 0L;
        long end = 0L;
        IndexedFastaReader instance = null;
        List<Character> expResult = null;
        List<Character> result = instance.getRevSeq(chr, start, end);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
