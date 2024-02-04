import java.io.File;
import java.io.IOException;
import java.util.*;
import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Hello {
    public static TreeMap<String,String> Iterate(File dir){
        String filenames[] = dir.list();
        TreeMap<String,String> rereturn=new TreeMap<String,String>();
        if(filenames!=null)
        {
            for(int i=0; i<filenames.length; i++) {
                if(filenames[i].endsWith(".png"))
                {
                rereturn.put(filenames[i].substring(0,filenames[i].length()-4), filenames[i]);
                }
            }
        }
        return rereturn;
    }
    public static String imageToSource(BufferedImage img, String identifier) {
        int width = img.getWidth();
        int height = img.getHeight();
        // This will eventually hold something like "int identifier[] = { ###, ###, ... };"
        StringBuilder ret = new StringBuilder();
        ret.append("int ");
        ret.append(identifier);
        ret.append("[] = { ");
        Color[][] result = new Color[height][width];  // deprecated
        for (int row = 0; row < height; row++) 
        {
            for (int col = 0; col < width; col++) 
            {
                result[row][col] = new Color(img.getRGB(col, row));
                ret.append(result[row][col].getRed()); ret.append(", ");
                ret.append(result[row][col].getGreen()); ret.append(", ");
                ret.append(result[row][col].getBlue()); ret.append(", ");
                // System.out.printf("row = %d, col = %d, val = %d, %d, %d\n", row, col, result[row][col].getRed(), result[row][col].getGreen(), result[row][col].getBlue());
            }
        }
        ret.append("};\n");
        return ret.toString();
     }
    public static void main(String args[]) {
        // Replace this with something that:
        //  1. Look at how to strip the .png from the filenames
        //  2. Only display filenames that end in .png
        File files[] = new File(".").listFiles();
        for(int j=0; j<files.length; j++) {
            if(files[j].isDirectory()) {
                //System.out.printf("%s is a directory.\n", files[j].getName());
                TreeMap<String,String> rereturn= Iterate(files[j]);
                Iterator<String> i=rereturn.keySet().iterator();
                while(i.hasNext())
                {
                    String key = i.next();
                    try 
                    {
                        File f = new File (files[j], rereturn.get(key));
                        System.out.println(f.toString());
                        BufferedImage winkyFace = ImageIO.read(f);
                        System.out.println(imageToSource(winkyFace, key));
                    } 
                    catch(IOException E) 
                    {
                        E.printStackTrace();
                    }
                    System.out.println(key + " = " + rereturn.get(key));
                }
            }
        }
    }
}
