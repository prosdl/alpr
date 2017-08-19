package de.pr.alpr.imgproc.transform.impl;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

import de.pr.alpr.imgproc.transform.Transformation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Thresholding implements Transformation {

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

   private int threshold = 25;

   @Override
   public BufferedImage process(BufferedImage im) {
      ImageProducer producer = new FilteredImageSource(im.getSource(), new ThresholdFilter(threshold));
      Image binary = Toolkit.getDefaultToolkit().createImage(producer);

      BufferedImage bufferedImage = new BufferedImage(binary.getWidth(null), binary.getHeight(null),
            BufferedImage.TYPE_BYTE_BINARY);
      bufferedImage.getGraphics().drawImage(binary, 0, 0, null);
      return bufferedImage;
   }
}
