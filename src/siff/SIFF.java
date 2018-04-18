/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siff;

import GetCmdOpt.ArrayModeCmdLineParser;
import java.util.logging.Logger;

/**
 * SIFF stands for Summary and Interrogation of Fasta Format
 * @author dbickhart
 */
public class SIFF {
    private static String version = "0.0.1";
    private static final Logger log = Logger.getLogger(SIFF.class.getName());
    
    private static ArrayModeCmdLineParser PrepareCMDOptions(){
        String nl = System.lineSeparator();
        ArrayModeCmdLineParser cmd = new ArrayModeCmdLineParser("SIFF: Summary and Interrogation of Fasta Format assembly files" + nl +
                "Version: " + version + nl +
                "Usage: java -jar SIFF.jar [mode] [mode options]" + nl +
                "\tModes:" + nl +
                "\t\tstandardize\tMake fasta lines standard in a file" + nl +
                "\t\treorder\tCorrect assembly based on aligned map markers" + nl +
                "\t\tagp2fasta\tGenerate a new fasta by subsectioning a reference fasta" + nl, 
        "standardize", "agp2fasta", "reorder");
        
        cmd.AddMode("standardize", 
                "SIFF standardize:" + nl +
                        "Usage: java -jar SIFF.jar standardize -f [input fasta file] -o [output fasta file] -r [OPTIONAL: remove this suffix from fasta file entries]" + nl +
                        "\t-f\tInput fasta file" + nl+
                        "\t-r\tSuffix to remove from fasta entries" + nl +
                        "\t-o\tOutput fasta file with corrected settings" + nl, 
                "f:r:o:d|", 
                "fo", 
                "frod", 
                "fasta", "format", "output", "debug");
        
        cmd.AddMode("reorder", 
                "SIFF reorder:" + nl +
                        "Usage: java -jar SIFF.jar reorder -s [input marker sam file] -j [input jellyfish db] -f [input fasta] -o [output basename]" + nl +
                        "[MANDATORY]" + nl +
                        "\t-s\tA sam file with ordered map coordinates mapped t the assembly" + nl +
                        "\t-f\tInput fasta file" + nl+
                        "\t-o\tOutput file basename" + nl +
                        "[OPTIONAL]" + nl +
                        "\t-r\tSegment size filter (remove condensed segments less than this size) [default = 50000 bp]" + nl +
                        "\t-m\tMismapped marker resolution (removes segments that have less than X number of markers that do not map to this chromosome) [default 5]", 
                "s:f:o:d|", 
                "sfo", 
                "sfod", 
                "sam", "fasta", "output", "debug");
        
        cmd.AddMode("agp2fasta", 
                "SIFF agp2fasta: " + nl + 
                        "Usage: java -jar SIFF.jar agp2fasta -f [original fasta] -a [agp file] -o [output fasta name]" + nl +
                        "\t-f\tThe input fasta to be subsectioned for incorporation into the AGP file" + nl +
                        "\t-a\tThe agp file for ordering fasta subsections" + nl +
                        "\t-o\tThe full output name of the resultant fasta file" + nl, 
                "f:a:o:d|", 
                "fao", 
                "faod", 
                "fasta", "agp", "output", "debug");
        
        return cmd;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
