/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textcompressor;

import java.util.Arrays;

/**
 *
 * @author Oriol
 */
public class RiceCompressor {
    
    private int bestM;

    public RiceCompressor() {
        this.bestM = 0;
    }

    public void displayInfo(int[] data) {
        int bestAcc = Integer.MIN_VALUE;
        int nBits = (int) Math.ceil((Math.log10(this.getMaxValue(data))/Math.log10(2)) + 1);
        int j=2;
        while(j<4097){
            int acc = 0;
            for(int i = 0; i < data.length; i++){
                String riceCode = this.compress(j, data[i]);
                acc += (nBits-riceCode.length());
            }     
            if(acc > bestAcc){
                bestAcc = acc;
                this.bestM = j;
            }
            j*=2;
        }
        System.out.println("Some information about the audio file: "
                + "\n\t- Bits in natural binary: " + nBits
                + "\n\t- Mean: " + calculateMean(data) 
                + "\n\t- Standard deviation: " + calculateStandardDeviation(data)
                + "\n\t- Optimal M: " + this.bestM);
    }
    
    public String compress(int m, int n){
        String riceCode;
        int nBitsM = (int) (Math.log10(m)/Math.log10(2));
        if(n < 0) riceCode = "0"; //Negative value
        else riceCode = "1"; //Positive value
        int q = Math.abs(n)/m;
        char[] array = new char[q];
        Arrays.fill(array, '1');
        if(array.length > 0) riceCode = riceCode.concat(String.valueOf(array));
        riceCode = riceCode.concat("0");
        int r = Math.abs(n)%m;
        String rBinary = String.format("%"+nBitsM+"s", Integer.toBinaryString(r)).replace(' ', '0');
        riceCode = riceCode.concat(rBinary);
        return riceCode;
    }

    private float calculateMean(int[] arg) {
        int sum = 0;
        for(int i=0; i<arg.length; i++){
            sum += arg[i];
        }
        return (float)sum / (float)arg.length;
    }

    private float calculateStandardDeviation(int[] arg) {
        float mean = calculateMean(arg);
        float acc = 0;
        for(int i=0; i<arg.length; i++){
            acc += Math.pow((arg[i]-mean), 2);
        }
        return (float) Math.sqrt(acc/(float)arg.length);
    }
    
    private int getMaxValue(int[] array){  
        int maxValue = 0;  
        for(int i=1; i<array.length; i++){  
            if(Math.abs(array[i]) > maxValue) maxValue = Math.abs(array[i]);  
        }  
        return maxValue;  
    }  

    public int getBestM() {
        return bestM;
    }
}   