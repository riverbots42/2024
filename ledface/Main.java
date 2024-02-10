/**
 * Program to scan the current directory for subdirectories and generate
 * an Animations.java that contains all animations (lists of PNGs)
 * discovered.
**/

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

public class Main {

    /**
     * Return a TreeMap of all the identifiers/filenames found mathing *.png
     * in a given directory.
     *
     * @param dir the directory to scan.
     * @return a TreeMap where the key is the identifier discovered and the val is the filename.
    **/
    public static TreeMap<String,String> Iterate(File dir)
    {
        String filenames[] = dir.list();
        TreeMap<String,String> rereturn=new TreeMap<String,String>();
        if(filenames!=null)
        {
            for(int i=0; i<filenames.length; i++)
	    {
                if(filenames[i].endsWith(".png"))
                {
                    rereturn.put(filenames[i].substring(0,filenames[i].length()-4), filenames[i]);
                }
            }
        }
        return rereturn;
    }

    /**
     * Given a BufferedImage, convert it to a list of integers (r0, g0, b0, r1, g1, b1, ...)
     * and then generate a Java source line of the form:
     *     int identifier[] = { r0, g0, b0, r1, g1, b1, ... }
     *
     * @param animation the name of the animation.
     * @param img the BufferedImage to convert to source.
     * @param identifier the name of the identifier to make.
     * @return a Java source code line.
    **/
    public static String imageToSource(String animation, BufferedImage img, String identifier) {
        int width = img.getWidth();
        int height = img.getHeight();
        // This will eventually hold something like "int identifier[] = { ###, ###, ... };"
        StringBuilder ret = new StringBuilder();
	// The first part of the line is "int animation__identifier[] = { "
        ret.append("int ");
        ret.append(animation);
        ret.append("__");
        ret.append(identifier);
        ret.append("[] = { ");
	// Now we generate the list of ints to put in the file.
        Color[][] result = new Color[height][width];  // deprecated
        for (int row = 0; row < height; row++) 
        {
            for (int col = 0; col < width; col++) 
            {
                result[row][col] = new Color(img.getRGB(col, row));
                ret.append(result[row][col].getRed()); ret.append(", ");
                ret.append(result[row][col].getGreen()); ret.append(", ");
                ret.append(result[row][col].getBlue()); ret.append(", ");
            }
        }
	// Finally we close with the "};" to end the line.
        ret.append("};\n");
        return ret.toString();
    }

    /**
     * Look in the current directory for subdirectories and .png files therein and generate
     * an Animations.java that has all the raw data therefrom.
    **/
    public static void main(String args[]) {
        File files[] = new File(".").listFiles();
        for(int j=0; j<files.length; j++) {
            if(files[j].isDirectory()) {
                TreeMap<String,String> rereturn= Iterate(files[j]);
                Iterator<String> i=rereturn.keySet().iterator();
                while(i.hasNext())
                {
                    String key = i.next();
                    try 
                    {
                        File f = new File (files[j], rereturn.get(key));
                        BufferedImage winkyFace = ImageIO.read(f);
                        System.out.println(imageToSource(files[j].getName(), winkyFace, key));
                    } 
                    catch(IOException E) 
                    {
                        E.printStackTrace();
                    }
                }
            }
        }
    }
}
