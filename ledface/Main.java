//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.*;

public class Main {
    public static String imageToSource(BufferedImage img, String identifier) {
       int width = img.getWidth();
       int height = img.getHeight();
       // This will eventually hold something like "int identifier[] = { ###, ###, ... };"
       StringBuilder ret = new StringBuilder();
       ret.append("int ");
       ret.append(identifier);
       ret.append("[] = { ");
       Color[][] result = new Color[height][width];  // deprecated
       for (int row = 0; row < height; row++) {
           for (int col = 0; col < width; col++) {
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
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        final String winky = "C:\\Users\\ashsr\\Desktop\\ver 2/winky.png";
        try {
            BufferedImage winkyFace = ImageIO.read(new File (winky));
            System.out.println(imageToSource(winkyFace, "winky"));
        } catch(IOException E) {
            E.printStackTrace();
        }
    }
}