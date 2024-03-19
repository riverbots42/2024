/**
 * Program to explode a single animated .GIF to a list of PNGs.
**/

import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import org.w3c.dom.*;

public class Main
{
    public static int DEFAULT_WIDTH = 32;
    public static int DEFAULT_HEIGHT = 16;

    /**
     * Given a command-line arguments, convert an animated .GIF to a list of 32x16 PNGs.
    **/
    public static void main(String args[])
    {
        if(args.length < 1 || args[0].equals("--help"))
        {
            System.out.println("Usage: gif2pnglist something.gif [somethingelse.gif ...]");
            return;
        }
        for(int i=0; i<args.length; i++)
        {
            try {
                String gif = args[i];
                if(!gif.endsWith(".gif"))
                {
                    throw new Exception("Invalid file type.");
                }
                String pngdir = gif.substring(0, gif.length()-4);
                System.out.printf("Converting %s to %s/%s__*.png...\n", gif, pngdir, pngdir);
                convert(gif, pngdir);
            }
            catch(Exception E)
            {
                System.out.printf("Couldn't convert %s: %s\n", args[i], E.toString());
            }
        }
    }

    /**
        Given a filename and an output directory, read an animated GIF and save as a list of .PNGs in a subdirectory.

        @param filename the name of the GIF to open.
        @param pngdir the name of the directory to create.
    **/
    public static void convert(String filename, String pngdir) throws IOException
    {
        ImageFrame[] frames = readGif(new FileInputStream(new File(filename)));
        System.out.printf("-> Read %d frames.\n", frames.length);
        new File(pngdir).mkdirs();
        for(int i=0; i<frames.length; i++)
        {
            File outfile = new File(String.format("%s/%s__%03d.png", pngdir, pngdir, Integer.valueOf(i)));
            BufferedImage srcimg = frames[i].getImage();
            BufferedImage dstimg = new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_RGB);
            double aspect_src = 1.0 * srcimg.getWidth() / srcimg.getHeight();
            double aspect_dst = 1.0 * dstimg.getWidth() / dstimg.getHeight();
            double skip = 0.0;
            // We need to either center things horizontally or vertically, but still deal with the possibility that we have
            // a perfect 32x16 GIF.
            if(aspect_src > aspect_dst)
            {
                skip = srcimg.getWidth() / dstimg.getWidth();
            }
            else
            {
                skip = srcimg.getHeight() / dstimg.getHeight();
            }
            int margin_x = (DEFAULT_WIDTH - (int)(srcimg.getWidth()/skip)) / 2;
            int margin_y = (DEFAULT_HEIGHT - (int)(srcimg.getHeight()/skip)) / 2;
            for(int x=0; x<DEFAULT_WIDTH; x++)
            {
                for(int y=0; y<DEFAULT_HEIGHT; y++)
                {
                    // With the 320x320 images, we need to try to get *just barely* inside the square rather than reading the
                    // border of the square itself.
                    int xx = (int)(x*skip + 0.4 * skip);
                    int yy = (int)(y*skip + 0.4 * skip);
                    if(xx < 0 || xx >= srcimg.getWidth() || yy < 0 || yy >= srcimg.getHeight())
                    {
                        continue;
                    }
                    int rgb = srcimg.getRGB(xx, yy);
                    dstimg.setRGB(margin_x + x, margin_y + y, rgb);
                }
            }
            ImageIO.write(dstimg, "png", outfile);
        }
    }

    /**
        Read an animated GIF and return it as an array of Frames.
        Utterly and completely stolen from StackOverflow
        https://stackoverflow.com/questions/8933893/convert-each-animated-gif-frame-to-a-separate-bufferedimage

        @param stream an InputStream pointing to the source GIF file.
        @return an array of ImageFrames.
    **/
    private static ImageFrame[] readGif(InputStream stream) throws IOException
    {
        ArrayList<ImageFrame> frames = new ArrayList<ImageFrame>(2);

        // This code basically hacks up the GIF format ImageReader from javax...imageio to deal with replacement and all that
        // GIF-specific junk.
        ImageReader reader = (ImageReader) ImageIO.getImageReadersByFormatName("gif").next();
        reader.setInput(ImageIO.createImageInputStream(stream));

        int lastx = 0;
        int lasty = 0;

        int width = DEFAULT_WIDTH;
        int height = DEFAULT_HEIGHT;

        IIOMetadata metadata = reader.getStreamMetadata();

        Color backgroundColor = null;

        if(metadata != null)
        {
            IIOMetadataNode globalRoot = (IIOMetadataNode) metadata.getAsTree(metadata.getNativeMetadataFormatName());

            NodeList globalColorTable = globalRoot.getElementsByTagName("GlobalColorTable");
            NodeList globalScreeDescriptor = globalRoot.getElementsByTagName("LogicalScreenDescriptor");

            if (globalScreeDescriptor != null && globalScreeDescriptor.getLength() > 0)
            {
                IIOMetadataNode screenDescriptor = (IIOMetadataNode) globalScreeDescriptor.item(0);

                if (screenDescriptor != null)
                {
                    width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
                    height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
                }
            }

            if (globalColorTable != null && globalColorTable.getLength() > 0)
            {
                IIOMetadataNode colorTable = (IIOMetadataNode) globalColorTable.item(0);

                if (colorTable != null)
                {
                    String bgIndex = colorTable.getAttribute("backgroundColorIndex");

                    IIOMetadataNode colorEntry = (IIOMetadataNode) colorTable.getFirstChild();
                    while (colorEntry != null)
                    {
                        // GIFs use indexed color, so we need to track what's going with the palette.
                        if (colorEntry.getAttribute("index").equals(bgIndex))
                        {
                            int red = Integer.parseInt(colorEntry.getAttribute("red"));
                            int green = Integer.parseInt(colorEntry.getAttribute("green"));
                            int blue = Integer.parseInt(colorEntry.getAttribute("blue"));

                            backgroundColor = new Color(red, green, blue);
                            break;
                        }

                        colorEntry = (IIOMetadataNode) colorEntry.getNextSibling();
                    }
                }
            }
        }

        BufferedImage master = null;
        boolean hasBackround = false;

        for (int frameIndex = 0;; frameIndex++)
        {
            // This is the current frame we'll be generating.
            BufferedImage image;
            try
            {
                image = reader.read(frameIndex);
            }
            catch (IndexOutOfBoundsException io)
            {
                // We ran out of frames, which basically just means we're done.
                break;
            }

            IIOMetadataNode root = (IIOMetadataNode) reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
            IIOMetadataNode gce = (IIOMetadataNode) root.getElementsByTagName("GraphicControlExtension").item(0);
            NodeList children = root.getChildNodes();

            int delay = Integer.valueOf(gce.getAttribute("delayTime"));

            String disposal = gce.getAttribute("disposalMethod");

            if (master == null)
            {
                master = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                master.createGraphics().setColor(backgroundColor);
                master.createGraphics().fillRect(0, 0, master.getWidth(), master.getHeight());

                // Cheeky way to avoid an if()
                hasBackround = (image.getWidth() == width) && (image.getHeight() == height);

                // At this point, we can fill master with image data.  Animated GIFs are basically deltas off this first frame.
                master.createGraphics().drawImage(image, 0, 0, null);
            }
            else
            {
                // A GIF replacement frame *may* replace only a small chunk of the previous frame, so... :-/
                int x = 0;
                int y = 0;

                for (int nodeIndex = 0; nodeIndex < children.getLength(); nodeIndex++)
                {
                    Node nodeItem = children.item(nodeIndex);

                    if (nodeItem.getNodeName().equals("ImageDescriptor"))
                    {
                        NamedNodeMap map = nodeItem.getAttributes();

                        x = Integer.valueOf(map.getNamedItem("imageLeftPosition").getNodeValue());
                        y = Integer.valueOf(map.getNamedItem("imageTopPosition").getNodeValue());
                    }
                }

                if (disposal.equals("restoreToPrevious"))
                {
                    BufferedImage from = null;
                    for (int i = frameIndex - 1; i >= 0; i--)
                    {
                        if (!frames.get(i).getDisposal().equals("restoreToPrevious") || frameIndex == 0)
                        {
                            from = frames.get(i).getImage();
                            break;
                        }
                    }

                    ColorModel model = from.getColorModel();
                    boolean alpha = from.isAlphaPremultiplied();
                    WritableRaster raster = from.copyData(null);
                    master = new BufferedImage(model, raster, alpha, null);
                }
                else if (disposal.equals("restoreToBackgroundColor") && backgroundColor != null)
                {
                    if (!hasBackround || frameIndex > 1)
                    {
                        master.createGraphics().fillRect(
                            lastx,
                            lasty,
                            frames.get(frameIndex - 1).getWidth(),
                            frames.get(frameIndex - 1).getHeight()
                        );
                    }
                }
                master.createGraphics().drawImage(image, x, y, null);

                lastx = x;
                lasty = y;
            }
            BufferedImage copy;
            ColorModel model = master.getColorModel();
            boolean alpha = master.isAlphaPremultiplied();
            WritableRaster raster = master.copyData(null);
            copy = new BufferedImage(model, raster, alpha, null);
            frames.add(new ImageFrame(copy, delay, disposal, image.getWidth(), image.getHeight()));
            master.flush();
        }
        reader.dispose();

        return frames.toArray(new ImageFrame[frames.size()]);
    }
}
