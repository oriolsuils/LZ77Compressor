package textcompressor;


import java.util.Arrays;

/**
 * Aquesta classe s'encarrega de validar les dades, i comprimir i descomprimir
 * en LZ77.
 *
 * @author Oriol
 */
public class Compressor {
    //La trama binaria de dades original
    private String originalData;
    //La trama comprimida
    private String compressData;
    //La trama descomprimida
    private String decompressData;
    //Longitud de la finestra d'inputData
    private int inputWindow;
    //Longitud de la finestra de lliscament
    private int slidingWindow;
    private Long t0;
    private Long t1;

    public Compressor(String data, int inputWindow, int slidingWindow) {
        this.originalData = data;
        this.inputWindow = inputWindow;
        this.slidingWindow = slidingWindow;
    }

    public Compressor() {
        super();
        this.originalData = "";
        this.compressData = "";
        this.decompressData = "";
        this.inputWindow = 0;
        this.slidingWindow = 0;
    }

    public String getOriginalData() {
        return originalData;
    }

    public String getCompressData() {
        return compressData;
    }

    public String getDecompressData() {
        return decompressData;
    }

    public int getInputWindow() {
        return inputWindow;
    }

    public int getSlidingWindow() {
        return slidingWindow;
    }

    public void setOriginalData(String data) {
        this.originalData = data;
    }

    public void setDecompressData(String decompressData) {
        this.decompressData = decompressData;
    }
    
    public void setCompressData(String compressData) {
        this.compressData = compressData;
    }

    public void setInputWindow(int inputWindow) {
        this.inputWindow = inputWindow;
    }

    public void setSlidingWindow(int slidingWindow) {
        this.slidingWindow = slidingWindow;
    }
    
    public boolean validateWindows(int inputW, int slidingW) {
        return isPowerOf2(inputW) && isPowerOf2(slidingW) && inputW >= slidingW && inputW+slidingW <= this.originalData.length();
    }
    
    /**
     * Aquest metode s'encarrega de validar la trama de dades original, per 
     * veure si tots son valors binaris.
     * 
     * @param data
     * @return 
     */
    public boolean validateData(String data) {
        if(data.length() > 0){
            for(int i=0; i<data.length(); i++){
                char c = data.charAt(i);
                if(c != '0' && c != '1') return false;
            }
        }
        return true;
    }
    
    /**
     * Aquest metode serveix per veure si un numero es potencia de dos.
     * 
     * @param num
     * @return 
     */
    private boolean isPowerOf2(int num) { 
        while (num >= 2) { 
            if (num % 2 != 0) { 
                return false; 
            } 
            num /= 2; 
        } 
        return true; 
    } 
    
    /**
     * Aquest metode s'encarrega de comprimir les dades originals.
     */
    public void compressData() {
        t0 = System.nanoTime();
        this.compressData = "";
        String inputData=this.originalData.substring(0,this.inputWindow);
        String slidingData=this.originalData.substring(this.inputWindow,(this.slidingWindow)+this.inputWindow);           
        this.compressData = this.compressData.concat(inputData);
        boolean trobat = false;
        int i=1;
        int idx=0;
        int l=0, d=0;
        while(idx <= this.originalData.length()-(this.slidingWindow+this.inputWindow)){
            trobat=false;
            i=1;
            while(!trobat && i<=this.slidingWindow){
                int index = inputData.lastIndexOf(slidingData);
                if(index == -1){  
                    slidingData=this.originalData.substring(this.inputWindow+idx,(this.inputWindow+idx)+((this.slidingWindow)-i)); 
                    i++;                                                         
                }else{
                    trobat=true;
                    l = slidingData.length(); 
                    d = this.inputWindow-index; 
                    this.compressData=this.compressData.concat(int2Binary(l,d));   
                }
            }
            idx += l;
            if(idx <= this.originalData.length()-(this.slidingWindow+this.inputWindow)){
                inputData = this.originalData.substring(idx,idx+this.inputWindow);
                slidingData = this.originalData.substring(this.inputWindow+idx,(this.slidingWindow+idx)+this.inputWindow);
            }
        }
        if(this.originalData.length() > idx+this.inputWindow){ 
            this.compressData = this.compressData.concat(this.originalData.substring(idx+this.inputWindow, this.originalData.length()));
        }
        t1 = System.nanoTime();
    }

    /**
     * Aquest metode s'encarrega de descomprimir les dades comprimides.
     */
    public void decompressData() {
        this.decompressData = "";
        this.decompressData = this.decompressData.concat(this.compressData.substring(0, inputWindow));
        int nBitsL = (int) Math.round(Math.log10(this.slidingWindow)/Math.log10(2));
        int nBitsD = (int) Math.round(Math.log10(this.inputWindow)/Math.log10(2));
        int idxInput = inputWindow;
        int idxOutput = inputWindow;
        while(idxInput+nBitsL+nBitsD <= this.compressData.length()){
            int bitsL = Integer.parseInt(this.compressData.substring(idxInput, idxInput+nBitsL), 2);
            int bitsD = Integer.parseInt(this.compressData.substring(idxInput+nBitsL, idxInput+nBitsL+nBitsD), 2);
            if(bitsL == 0 && bitsD == 0){
                bitsL = slidingWindow;
                bitsD = inputWindow;
            } else if(bitsL == 0){
                bitsL = slidingWindow;
            } else if(bitsD == 0){
                bitsD = inputWindow;
            }
            String subSequence = new String(this.decompressData.substring(idxOutput-bitsD, (idxOutput-bitsD)+bitsL));
            this.decompressData = this.decompressData.concat(subSequence);
            idxInput+=(nBitsL+nBitsD);
            idxOutput+=bitsL;
        }
        
        if(idxInput != this.compressData.length()){
            this.decompressData = this.decompressData.concat(this.compressData.substring(idxInput, this.compressData.length()));
        }
    }
    
    /**
     * Aquest metode serveix per transformar un (L,D) de forma int a forma 
     * binaria.
     * 
     * @param lengthSubSequence
     * @param posSubSequence
     * @return 
     */
    private String int2Binary(int lengthSubSequence, int posSubSequence){
        String s = "", binary1 = "", binary2 = "";
        int nBitsL = (int) Math.round(Math.log10(this.slidingWindow)/Math.log10(2));
        int nBitsD = (int) Math.round(Math.log10(this.inputWindow)/Math.log10(2));
        if(lengthSubSequence == this.slidingWindow && posSubSequence == this.inputWindow){
            char[] charsL = new char[nBitsL];
            char[] charsD = new char[nBitsD];
            Arrays.fill(charsL, '0');
            Arrays.fill(charsD, '0');
            binary1 = new String(charsL);
            binary2 = new String(charsD);
        } else if(lengthSubSequence == this.slidingWindow){
            char[] charsL = new char[nBitsL];
            Arrays.fill(charsL, '0');
            binary1 = new String(charsL);
            binary2 = String.format("%"+nBitsD+"s", Integer.toBinaryString(posSubSequence)).replace(' ', '0');
        } else if(posSubSequence == this.inputWindow){
            binary1 = String.format("%"+nBitsL+"s", Integer.toBinaryString(lengthSubSequence)).replace(' ', '0');
            char[] charsD = new char[nBitsD];
            Arrays.fill(charsD, '0');
            binary2 = new String(charsD);
        } else {
            binary1 = String.format("%"+nBitsL+"s", Integer.toBinaryString(lengthSubSequence)).replace(' ', '0');
            binary2 = String.format("%"+nBitsD+"s", Integer.toBinaryString(posSubSequence)).replace(' ', '0');
        }
        s = s.concat(binary1);
        s = s.concat(binary2);
        return s;
    }
    
    public double getCompressionTime() {
        Long result = this.t1 - this.t0;
        return result / 1000000000.0;
    } 
}
