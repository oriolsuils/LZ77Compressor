package textcompressor;


import java.util.Scanner;

/**
 * 
 * @author Oriol
 */
public class View {
    
    private Scanner sc;
    private Compressor compressor;

    public View() {
        sc = new Scanner(System.in);   
        compressor = new Compressor();
    }
    
    public static void main(String args[]){
        View v = new View();
        v.textCompressorMenu();
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
            if(compressor.validateData(data)){
                compressor.setOriginalData(data);
                System.out.print("Inserta el tamany de la finestra d'entrada"
                                +"\n ->");
                inputWindow = Integer.parseInt(sc.nextLine());
                System.out.print("Inserta el tamany de la finestra lliscant"
                                +"\n ->");
                slidingWindow = Integer.parseInt(sc.nextLine());
                if(compressor.validateWindows(inputWindow, slidingWindow)){
                    compressor.setInputWindow(inputWindow);
                    compressor.setSlidingWindow(slidingWindow);
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
        compressor.setOriginalData(data);
        compressor.setInputWindow(8);
        compressor.setSlidingWindow(4);
        showResults();
    }
    
    /**
     * Aquest metode mostra per pantalla la trama original, la comprimida, la 
     * descomprimida, i l'efecte de compressio (Original/Comprimida). 
     */
    private void showResults(){
        compressor.compressData();
        compressor.decompressData();
        System.out.println("     Trama original: " + compressor.getOriginalData());
        System.out.println("   Trama comprimida: " + compressor.getCompressData());
        System.out.println("Trama descomprimida: " + compressor.getDecompressData());
        System.out.println("Efecte de compressio: " + ((float)compressor.getOriginalData().length()/(float)compressor.getCompressData().length()));
    }
    
    /**
     * Mostra nomes l'efecte de compressio i el temps de compressio. 
     */
    private void showSummaryResults(){
        compressor.compressData();
        System.out.println("\tEfecte de compressio: " + ((float)compressor.getOriginalData().length()/(float)compressor.getCompressData().length()));
        System.out.println("\tTemps de compressio: " + compressor.getCompressionTime() + " s");
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
        double bestTime = 999.0, bestCompression = 0.0, bestAverage = 999.0;
        int slidingTime=0, inputTime=0, slidingCompression=0, inputCompression=0, slidingAverage=0, inputAverage=0;
        compressor.setOriginalData(data);
        for(int i=0; i<sizes.length; i++){
            for(int j=i; j<sizes.length; j++){
                System.out.println("Entrada: " + sizes[i] + " Lliscant: " + sizes[j]);
                compressor.setInputWindow(sizes[j]);
                compressor.setSlidingWindow(sizes[i]);
                if(compressor.validateWindows(sizes[j], sizes[i])) showSummaryResults();
                else System.out.println("Longitud de finestres incorrectes");
                if(compressor.getCompressionTime() < bestTime){
                    bestTime = compressor.getCompressionTime();
                    slidingTime = sizes[j];
                    inputTime = sizes[i];
                }
                float compression = ((float)compressor.getOriginalData().length()/(float)compressor.getCompressData().length());
                if(compression > bestCompression){
                    bestCompression = compression;
                    slidingCompression = sizes[j];
                    inputCompression = sizes[i];
                }
                double average = (((float)1.0/compression)*0.5)+(compressor.getCompressionTime()*0.5);
                if(average < bestAverage){
                    bestAverage = average;
                    slidingAverage = sizes[j];
                    inputAverage = sizes[i];
                }
            }
        }
        System.out.println("FINAL RESULTS: "
                + "\n\tTime: " + bestTime + " Lliscant: " + slidingTime + " Entrada: " + inputTime
                + "\n\tCompression: " + bestCompression + " Lliscant: " +slidingCompression + " Entrada: " + inputCompression
                + "\n\tAverage: " + bestAverage + " Lliscant: " +slidingAverage + " Entrada: " + inputAverage);
    }

    /**
     * S'encarrega de llegir un fitxer de text i passar-lo a una trama binaria. 
     * A mes es comproba la compressio amb diferents mides de finestres.
     * @param path on esta situat el fitxer.
     */
    private void textCompressor(String path) {
        String data = TxtReader.cargarTxt(path).toString();
        if(compressor.validateData(data)) checkCompression(data);
        else System.out.println("Trama erronia, ha de ser una trama binaria");
    }
}
