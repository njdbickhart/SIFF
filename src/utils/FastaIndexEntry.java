/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author dbickhart
 */
public class FastaIndexEntry {
    public final String name;
    public final long length;
    public final long startByte;
    public final long lineLen;
    public final long totLen;
    public boolean isModified = false;
    
    public FastaIndexEntry(String line){
        line = line.trim();
        String[] segs = line.split("\t");
        
        name = segs[0];
        length = Long.parseLong(segs[1]);
        startByte = Long.parseLong(segs[2]);
        lineLen = Long.parseLong(segs[3]);
        totLen = Long.parseLong(segs[4]);
    }
    
    public FastaIndexEntry(String name, long length, long startByte, long lineLen, long totLen){
        this.name = name;
        this.length = length;
        this.startByte = startByte;
        this.lineLen = lineLen;
        this.totLen = totLen;
    }
    
    public String getName(){
        return this.name;
    }
    
    public String getOutFormat(){
        StringBuilder out = new StringBuilder();
        out.append(this.name).append("\t").append(this.length).append("\t").append(this.startByte);
        out.append("\t").append(this.lineLen).append("\t").append(this.totLen).append(System.lineSeparator());
        return out.toString();
    }
}
