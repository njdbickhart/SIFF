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
    
    public FastaIndexEntry(String name, long length, long startByte, long lineLen, long totLen){
        this.name = name;
        this.length = length;
        this.startByte = startByte;
        this.lineLen = lineLen;
        this.totLen = totLen;
    }
    
    public FastaIndexEntry(String line){
        line = line.trim();
        String[] segs = line.split("\t");
        
        this.name = segs[0];
        this.length = Long.parseLong(segs[1]);
        this.startByte = Long.parseLong(segs[2]);
        this.lineLen = Long.parseLong(segs[3]);
        this.totLen = Long.parseLong(segs[4]);
    }
    
    public long GetStartByte(){
        return this.startByte;
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
