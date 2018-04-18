/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reorder;

import GetCmdOpt.ArrayModeCmdLineParser;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author dbickhart
 */
public class Reorder {
    private static final Logger log = Logger.getLogger(Reorder.class.getName());
    private final String samFile;
    private final String fastaFile;
    private final String outbase;
    //"sam", "fasta", "jellydb", "output",
    public Reorder(ArrayModeCmdLineParser cmd){
        this.samFile = cmd.GetValue("sam");
        this.fastaFile = cmd.GetValue("fasta");
        this.outbase = cmd.GetValue("output");
    }
    
    public void Run(){
        RearrangementPlan rearrange = new RearrangementPlan(this.samFile, this.fastaFile);
        
        log.log(Level.INFO, "Beginning marker plan mapping");
        rearrange.CreateMarkerPlan();
        
        log.log(Level.INFO, "Plotting points");
        rearrange.plotGraph(this.outbase + ".map.png");
        
                
        log.log(Level.INFO, "Printing out AGP file");
        rearrange.printOrderedListToAGP(this.outbase + ".agp");
    }
}
