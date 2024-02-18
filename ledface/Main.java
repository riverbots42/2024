/**
 * Program to scan the current directory for subdirectories and generate
 * an AnimationList.java that contains all animations (lists of PNGs)
 * discovered.
**/

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

public class Main
{
    public static int DEFAULT_WIDTH = 32;
    public static int DEFAULT_HEIGHT = 16;
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
     * and then generate a Java source snippet of the form:
     *     private static int[] id = new int[] { r0, g0, b0, r1, g1, b1, ... })
     *
     * @param img the BufferedImage to convert to source.
     * @return a Java source code line.
    **/
    public static String imageToSource(String id, BufferedImage img) throws Exception
    {
        int width = img.getWidth();
        int height = img.getHeight();
        if( width != DEFAULT_WIDTH) {
            throw new Exception("Invalid image width!");
        }
        if( height != DEFAULT_HEIGHT) {
            throw new Exception("Invalid image height!");
        }
        // This will eventually hold something like "private static int[] id = new int[] { ###, ###, ... })"
        StringBuilder ret = new StringBuilder("    private static int[] ");
        ret.append(id);
        ret.append("() { return new int[] {");
        // Now we generate the list of ints to put in the file.
        for (int col = 0; col < width; col++) 
        {
            for (int row = 0; row < height; row++) 
            {
                if (row != 0 || col != 0) {
                    ret.append(",");
                }
                ret.append(" ");
                Color cur = new Color(img.getRGB(col, row));
                ret.append(cur.getRed()); ret.append(", ");
                ret.append(cur.getGreen()); ret.append(", ");
                ret.append(cur.getBlue());
            }
        }
        // Finally we close with the "}"
        ret.append(" }; }\n");
        return ret.toString();
    }

    /**
     * Look in the current directory for subdirectories and .png files therein and generate
     * an AnimationList.java that has all the raw data therefrom.
    **/
    public static void main(String args[])
    {
        File files[] = new File(".").listFiles();
        StringBuilder constants = new StringBuilder();
        StringBuilder data = new StringBuilder();
        for(int j=0; j<files.length; j++)
        {
            if(files[j].isDirectory())
            {
                TreeMap<String,String> rereturn = Iterate(files[j]);
                String id = files[j].getName();
                if(rereturn.size() < 1)
                {
                    continue;
                }
                data.append("        ArrayList<Frame> "); data.append(id); data.append(" = new ArrayList<Frame>();\n");
                Iterator<String> i = rereturn.keySet().iterator();
                int frameCount = 1;
                while(i.hasNext())
                {
                    String key = i.next();
                    try 
                    {
                        StringBuffer fid_buf = new StringBuffer(id);
                        fid_buf.append("__"); fid_buf.append(frameCount);
                        String fid = fid_buf.toString();
                        File frameFile = new File(files[j], rereturn.get(key));
                        BufferedImage frame = ImageIO.read(frameFile);
                        data.append("        // "); data.append(frameFile.getName());
                        data.append(" Frame # "); data.append(frameCount); data.append("\n");
                        data.append("        "); data.append(id); data.append(".add(new Frame("); data.append(fid); data.append("()));\n");
                        constants.append(imageToSource(fid, frame));
                        constants.append("\n");
                    } 
                    catch(Exception E) 
                    {
                        E.printStackTrace();
                    }
                    frameCount++;
                }
                data.append("        animations.put(\""); data.append(id); data.append("\", new Animation("); data.append(id); data.append("));\n");
            }
        }
        try
        {
            BufferedReader template = new BufferedReader(new FileReader("AnimationList.tmpl"));
            String line = template.readLine();
            while(line != null)
            {
                if(line.startsWith("// TEMPLATE:"))
                    ;
                else if(line.startsWith("// CONSTANTS:"))
                    System.out.println(constants.toString());
                else if(line.startsWith("// DATA:"))
                    System.out.println(data.toString());
                else
                    System.out.println(line);
                line = template.readLine();
            }
        }
        catch(IOException E)
        {        
            E.printStackTrace();
        }
    }
}
