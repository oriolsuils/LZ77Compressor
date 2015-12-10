package textcompressor;


import java.util.Scanner;

/**
 * 
 * @author Oriol
 */
public class View {
    
    private Scanner sc;
    private LZ77Compressor lz77Comp;
    private RiceCompressor riceComp;

    public View() {
        sc = new Scanner(System.in);   
        lz77Comp = new LZ77Compressor();
        riceComp = new RiceCompressor();
    }
    
    public static void main(String args[]){
        View v = new View();
        v.audioCompressorMenu();
        //v.textCompressorMenu();
        //v.lz77Menu();
    }
    
    /**
     * Mostra el menu per fitxers d'audio.
     */    
    private void audioCompressorMenu() {
        String option; 
        boolean exit = false;
        while(!exit){
            System.out.println("##################### MENU ########################"
                            +"\n# 1.- Obtenir informacio de l'arxiu d'audio       #"
                            +"\n# 2.- Comprimir l'arxiu 'data.wav'                #"
                            +"\n# 3.- Sortir                                      #"
                            +"\n###################################################");
            System.out.print("-> ");
            option = sc.nextLine();
            
            try {
		int numero =  Integer.parseInt(option);
                switch (numero) {
                    case 1:
                        displayInfo("src/resources/data.wav");
                        break;
                    case 2:
                        audioCompressor("src/resources/data.wav");
                        break;
                    case 3:
                        System.out.println("Fins aviat!");
                        exit = true;
                        break;
                    default:
                        System.out.println("Opcio incorrecte");
                        break; 
                }
            } catch (NumberFormatException nfe){
                System.out.println("Format incorrecte, has d'introduir numeros.");
            } 
        }
    }
    
    /**
     * Mostra el menu per fitxers de text.
     */
    private void textCompressorMenu() {
        String option; 
        boolean exit = false;
        while(!exit){
            System.out.println("############# MENU ################"
                            +"\n# 1.- Comprimir Hamlet            #"
                            +"\n# 2.- Comprimir Quijote           #"
                            +"\n# 3.- Sortir                      #"
                            +"\n###################################");
            System.out.print("-> ");
            option = sc.nextLine();
            
            try {
		int numero =  Integer.parseInt(option);
                switch (numero) {
                    case 1:
                        textCompressor("src/resources/hamlet_short.txt");
                        break;
                    case 2:
                        textCompressor("src/resources/quijote_short.txt");
                        break;
                    case 3:
                        System.out.println("Fins aviat!");
                        exit = true;
                        break;
                    default:
                        System.out.println("Opcio incorrecte");
                        break; 
                }
            } catch (NumberFormatException nfe){
                System.out.println("Format incorrecte, has d'introduir numeros.");
            } 
        }
    }
    
    /**
     * Mostra el menu per trames binaries.
     */
    private void lz77Menu() {
        String option; 
        boolean exit = false;
        while(!exit){
            System.out.println("############# MENU ################"
                            +"\n# 1.- Introduir manualment        #"
                            +"\n# 2.- Proba random de 25 digits   #"
                            +"\n# 3.- Comprobar compresio         #"
                            +"\n# 4.- Sortir                      #"
                            +"\n###################################");
            System.out.print("-> ");
            option = sc.nextLine();
            
            try {
		int numero =  Integer.parseInt(option);
                switch (numero) {
                    case 1:
                        manualInput();
                        break;
                    case 2:
                        randomTest(generateRandomNumber(25));
                        break;
                    case 3:
                        checkCompression(generateRandomNumber(10000));
                        break;
                    case 4:
                        System.out.println("Fins aviat!");
                        exit = true;
                        break;
                    default:
                        System.out.println("Opcio incorrecte");
                        break; 
                }
            } catch (NumberFormatException nfe){
                System.out.println("Format incorrecte, has d'introduir numeros.");
            } 
        }
    }

    /**
     * Aquest metode serveix per insertar una trama de dades binaries, i dues
     * finestres per comprimir i descomprimir a traves de LZ77
     */
    private void manualInput() {
        String data;
        int inputWindow, slidingWindow;
        try{
            System.out.print("Inserta la trama binaria per comprimir"
                            +"\n ->");
            data = sc.nextLine();
            if(lz77Comp.validateData(data)){
                lz77Comp.setOriginalData(data);
                System.out.print("Inserta el tamany de la finestra d'entrada"
                                +"\n ->");
                inputWindow = Integer.parseInt(sc.nextLine());
                System.out.print("Inserta el tamany de la finestra lliscant"
                                +"\n ->");
                slidingWindow = Integer.parseInt(sc.nextLine());
                if(lz77Comp.validateWindows(inputWindow, slidingWindow)){
                    lz77Comp.setInputWindow(inputWindow);
                    lz77Comp.setSlidingWindow(slidingWindow);
                    showResults();
                } else {
                    System.out.println("Tamany de les finestres incorrecte.");
                }
            } else {
                System.out.println("Trama binaria incorrecta");
            }   
        } catch (NumberFormatException nfe){
            System.out.println("Format incorrecte, has d'introduir numeros.");
        }
    }

    /**
     * Aquest metode s'encarrega de fer una prova de 25 digits random binaris,
     * comprimir i descomprimir LZ77
     */
    private void randomTest(String data) {
        lz77Comp.setOriginalData(data);
        lz77Comp.setInputWindow(8);
        lz77Comp.setSlidingWindow(4);
        showResults();
    }
    
    /**
     * Aquest metode mostra per pantalla la trama original, la comprimida, la 
     * descomprimida, i l'efecte de compressio (Original/Comprimida). 
     */
    private void showResults(){
        lz77Comp.compressData();
        lz77Comp.decompressData();
        System.out.println("     Trama original: " + lz77Comp.getOriginalData());
        System.out.println("   Trama comprimida: " + lz77Comp.getCompressData());
        System.out.println("Trama descomprimida: " + lz77Comp.getDecompressData());
        System.out.println("Efecte de compressio: " + ((float)lz77Comp.getOriginalData().length()/(float)lz77Comp.getCompressData().length()));
    }
    
    /**
     * Mostra nomes l'efecte de compressio i el temps de compressio. 
     */
    private void showSummaryResults(){
        lz77Comp.compressData();
        System.out.println("\tEfecte de compressio: " + ((float)lz77Comp.getOriginalData().length()/(float)lz77Comp.getCompressData().length()));
        System.out.println("\tTemps de compressio: " + lz77Comp.getCompressionTime() + " s");
    }
    
    /**
     * Genera un numero aleatori de longitud size.
     * @param size la longitud del numero aleatori.
     * @return el numero aleatori de tipus String.
     */
    private String generateRandomNumber(int size){
        String randomNumber = "";
        for(int i=0; i<size; i++) {
            randomNumber = randomNumber.concat(String.valueOf(Math.round(Math.random())));
        }
        return randomNumber;
    }

    /**
     * Metode per comprobar la compressio, provant entre 4 i 4096 en les dues
     * finestres (Mdes, Ment).
     * @param data la trama binaria original de dades. 
     */
    private void checkCompression(String data) {
        int[] sizes = new int[11];
        int n = 4;
        for(int i=0; i<sizes.length; i++) {
            sizes[i] = n;
            n *= 2;
        }
        double bestTime = Double.MAX_VALUE, bestCompression = 0.0, bestAverage = Double.MAX_VALUE;
        int slidingTime=0, inputTime=0, slidingCompression=0, inputCompression=0, slidingAverage=0, inputAverage=0;
        lz77Comp.setOriginalData(data);
        for(int i=0; i<sizes.length; i++){
            for(int j=i; j<sizes.length; j++){
                //System.out.println("Entrada: " + sizes[i] + " Lliscant: " + sizes[j]);
                lz77Comp.setInputWindow(sizes[j]);
                lz77Comp.setSlidingWindow(sizes[i]);
                if(lz77Comp.validateWindows(sizes[j], sizes[i])) showSummaryResults();
                else System.out.println("Longitud de finestres incorrectes");
                if(lz77Comp.getCompressionTime() < bestTime){
                    bestTime = lz77Comp.getCompressionTime();
                    slidingTime = sizes[j];
                    inputTime = sizes[i];
                }
                float compression = ((float)lz77Comp.getOriginalData().length()/(float)lz77Comp.getCompressData().length());
                if(compression > bestCompression){
                    bestCompression = compression;
                    slidingCompression = sizes[j];
                    inputCompression = sizes[i];
                }
                double average = (((float)1.0/compression)*0.5)+(lz77Comp.getCompressionTime()*0.5);
                if(average < bestAverage){
                    bestAverage = average;
                    slidingAverage = sizes[j];
                    inputAverage = sizes[i];
                }
            }
        }
        System.out.println("Final results (Bests): "
                + "\n\tTime: " + bestTime + " Sliding: " + slidingTime + " Input: " + inputTime
                + "\n\tCompression: " + bestCompression + " Sliding: " +slidingCompression + " Input: " + inputCompression
                + "\n\tAverage: " + bestAverage + " Sliding: " +slidingAverage + " Input: " + inputAverage);
    }

    /**
     * S'encarrega de llegir un fitxer de text i passar-lo a una trama binaria. 
     * A mes es comproba la compressio amb diferents mides de finestres.
     * @param path on esta situat el fitxer.
     */
    private void textCompressor(String path) {
        String data = TxtReader.cargarTxt(path).toString();
        if(lz77Comp.validateData(data)) checkCompression(data);
        else System.out.println("Trama erronia, ha de ser una trama binaria");
    }

    private void audioCompressor(String path) {
        int[] array = WavReader.Wav2Array(path);
        if(riceComp.getBestM() == 0) riceComp.displayInfo(array);
        int bestM = riceComp.getBestM();
        checkAudioCompression(array, bestM);
    }
    
    private void checkAudioCompression(int[] data, int bestM) {  
        testLZ77WindowsCompress(data, 0);
        testLZ77WindowsCompress(data, bestM);
    } 
    
    private void testLZ77WindowsCompress(int[] data, int bestM){
        int[] sizes = new int[11];
        int n = 4;
        for(int i=0; i<sizes.length; i++) {
            sizes[i] = n;
            n *= 2;
        }
        String binaryData = "";
        for(int num : data) binaryData = binaryData.concat(Integer.toBinaryString(num));
        lz77Comp.setOriginalData(binaryData);
        if(bestM != 0){
            String riceData = "";
            for(int i = 0; i < data.length; i++){
                riceData = riceData.concat(riceComp.compress(bestM, data[i]));
            }
            lz77Comp.setOriginalData(riceData);
        }
        double bestCompression = 0.0;
        int slidingCompression = 0, inputCompression = 0, bestSize = 0;
        for(int i=0; i<sizes.length; i++){
            for(int j=i; j<sizes.length; j++){
                System.out.println("Input window: " + sizes[i] + " Sliding window: " + sizes[j]);
                lz77Comp.setInputWindow(sizes[j]);
                lz77Comp.setSlidingWindow(sizes[i]);
                if(lz77Comp.validateWindows(sizes[j], sizes[i])){
                    lz77Comp.compressData();
                    System.out.println("\tSize compressed: " + lz77Comp.getCompressData().length() + " Compress ratio: " + (binaryData.length()/(float)lz77Comp.getCompressData().length()));
                }else{
                    System.out.println("Longitud de finestres incorrectes");
                }
                float compression = ((float)binaryData.length()/(float)lz77Comp.getCompressData().length());
                if(compression > bestCompression){
                    bestCompression = compression;
                    bestSize = lz77Comp.getCompressData().length();
                    slidingCompression = sizes[j];
                    inputCompression = sizes[i];
                }
            }
        }
        if(bestM != 0){
            System.out.println("Original frame size: " + binaryData.length());
            System.out.println("========= Rice + LZ77 =========");
            System.out.println("Final results (Bests): "
                + "\n\tCompression ratio: " + bestCompression + "| Size: " + bestSize + "| Sliding window: " +slidingCompression + "| Input window: " + inputCompression);
            System.out.println("===============================");
        } else {
            System.out.println("Original frame size: " + binaryData.length());
            System.out.println("============ LZ77 =============");
            System.out.println("Final results (Bests): "
                + "\n\tCompression ratio: " + bestCompression + "| Size: " + bestSize + "| Sliding window: " +slidingCompression + "| Input window: " + inputCompression);
            System.out.println("===============================");
        }
    }

    private void displayInfo(String path) {
        int[] Wav2Array = WavReader.Wav2Array(path);
        riceComp.displayInfo(Wav2Array);
    }
}
