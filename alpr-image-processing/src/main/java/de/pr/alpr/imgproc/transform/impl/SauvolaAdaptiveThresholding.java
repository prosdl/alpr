package de.pr.alpr.imgproc.transform.impl;

import java.awt.image.BufferedImage;

import de.pr.alpr.imgproc.imgutil.RgbConvert.ARGB;
import de.pr.alpr.imgproc.transform.Transformation;

public class SauvolaAdaptiveThresholding implements Transformation {

   private int rx;
   private int ry;
   private double r;
   private double k;

   public SauvolaAdaptiveThresholding() {
      this(10, 2, 128.0, 0.5);
   }

   public SauvolaAdaptiveThresholding(int rx, int ry, double r, double k) {
      this.rx = rx;
      this.ry = ry;
      this.r = r;
      this.k = k;
   }

   @Override
   public BufferedImage process(BufferedImage im) {

      final BufferedImage mean = new Mean(rx, ry).process(im);
      final BufferedImage stDev = new StandardDeviation(rx, ry).process(im);

      final BufferedImage out = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

      int height = im.getHeight();
      int width = im.getWidth();

      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            final double m = mean.getRGB(x, y) & 0xff;
            final double s = stDev.getRGB(x, y) & 0xff;
            final double val = im.getRGB(x, y) & 0xff;

            double t = m * (1.0 + (1 + (k * s / r - 1.0)));
            if (val >= t) {
               out.setRGB(x, y, ARGB.ofGrayscale(255).toTypeIntRgb());
            }
         }
      }

      return out;
   }

}
