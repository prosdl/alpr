package de.pr.alpr.imgproc;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Thresholding {

   public static class ThresholdFilter extends RGBImageFilter {
      private int threshold;

      public ThresholdFilter(int threshold) {
         this.threshold = threshold;
         this.canFilterIndexColorModel = true;
      }

      @Override
      public int filterRGB(int x, int y, int rgb) {
         return (rgb & 0xff) > threshold ? 0xffffffff : 0;
      }
   }

   public static void main(String... args) throws IOException {
      BufferedImage im = ImageIO.read(new File("/tmp/sobel.jpg"));
      final int threshold = 20;

      ImageProducer producer = new FilteredImageSource(im.getSource(), new ThresholdFilter(threshold));
      Image binary = Toolkit.getDefaultToolkit().createImage(producer);

      BufferedImage bufferedImage = new BufferedImage(binary.getWidth(null), binary.getHeight(null), BufferedImage.TYPE_INT_RGB);
      bufferedImage.getGraphics().drawImage(binary, 0, 0, null);
      ImageIO.write(bufferedImage, "jpg", new File("/tmp/binary.jpg"));
   }
}
