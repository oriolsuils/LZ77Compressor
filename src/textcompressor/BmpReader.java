/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package textcompressor;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Oriol
 */
public class BmpReader {
        
    /**
     * @param filename the path and the name to the .bmp file
     * @return dataout integer array with all the data samples
     */
    public static int[] bmp2Array(String filename) {
        int[] dataOut = null;
        int[] rVector = null;
        int[] gVector = null;
        int[] bVector = null;
        try {
            BufferedImage image = ImageIO.read(new File(filename));
            dataOut = new int[(image.getHeight()*image.getWidth())*3];
            rVector = new int[image.getHeight()*image.getWidth()];
            gVector = new int[image.getHeight()*image.getWidth()];
            bVector = new int[image.getHeight()*image.getWidth()];
            int idx = 0;
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    int rgb = image.getRGB(x, y);
                    int  red   = (rgb & 0x00ff0000) >> 16;
                    int  green = (rgb & 0x0000ff00) >> 8;
                    int  blue  = (rgb & 0x000000ff);
                    rVector[idx] = red;
                    gVector[idx] = green;
                    bVector[idx] = blue;
                    idx++;
                }
            }
            System.arraycopy(rVector, 0, dataOut, 0, rVector.length);
            System.arraycopy(gVector, 0, dataOut, rVector.length, gVector.length);
            System.arraycopy(bVector, 0, dataOut, (rVector.length+gVector.length), bVector.length);
        } catch (IOException ex) {
            Logger.getLogger(BmpReader.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataOut;
    }
}
