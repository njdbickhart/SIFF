/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package siff;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Derek.Bickhart
 */
public class StatContainer {
    public int longest = 0;
    public List<Integer> lencounts = new ArrayList<>();
    public int sumN = 0;
    public int sumChrLen = 0;
    public String chrname;
    
    public void addMaskLen(int length){
        if(length > longest){
            longest = length;
        }
        
        lencounts.add(length);
        sumN += length;
    }
    
    public void setChrInfo(String chrname, int length){
        this.chrname = chrname;
        this.sumChrLen = length;
    }
    
    // output: chrname chrlen sumNs longestN avgN medianN stdevN percentChrLenN
    public String getFormatStatStr(){
        StringBuilder out = new StringBuilder();
        
        out.append(chrname).append("\t").append(sumChrLen).append("\t").append(sumN).append("\t");
        out.append(longest).append("\t");
        if(lencounts.size() > 0){
            double avg = this.IntAvg(lencounts);
            double stdev = this.stdevInt(avg, lencounts);
            int median = this.Median(lencounts);

            out.append(String.format("%1$.4f", avg)).append("\t");
            out.append(median).append("\t").append(String.format("%1$.4f", stdev)).append("\t");
            double percent = (this.sumN / (double) this.sumChrLen) * 100;

            out.append(String.format("%1$.4f", percent)).append(System.lineSeparator());
        }else{
            out.append(0).append("\t").append(0).append("\t").append(0).append("\t");
            out.append(0).append(System.lineSeparator());
        }
        
        return out.toString();
    }
    
    private double IntAvg(List<Integer> sum){
        if(sum.isEmpty()){
            return 0.0d;
        }
        double s = 0.0d;
        for(int d : sum){
            s += d;
        }
        return s / (double) sum.size();
    }
    
    private double stdevInt(double avg, List<Integer> sum){
        if(sum.isEmpty() || sum.size() == 1){
            return 0;
        }        
        double dev = 0.0d;
        for(int x = 0; x < sum.size(); x++){
            dev += (double) Math.pow(sum.get(x) - avg, 2.0d);
        }
        double variance = dev / (double) (sum.size() - 1);
        return Math.sqrt(variance);
    }
    
    private int Median(List<Integer> values){
        Collections.sort(values);
        if(values.size() % 2 == 0){
            return values.get(values.size() / 2);
        }else if(values.size() == 1){
            return values.get(0);
        }else{
            List<Integer> temp = new ArrayList<>();
            temp.add(values.get(values.size() / 2));
            temp.add(values.get((values.size() / 2) + 1));
            return (int) Math.floor(this.IntAvg(temp));
        }
    }
}
