package de.pr.alpr.imgproc.transform.impl;

import java.awt.image.BufferedImage;
import java.math.RoundingMode;

import com.google.common.math.IntMath;

import de.pr.alpr.imgproc.imgutil.RgbConvert.ARGB;
import de.pr.alpr.imgproc.transform.Transformation;

public class StandardDeviation implements Transformation {
   final private int rx;

   final private int ry;

   final private int n;

   public StandardDeviation() {
      this(1, 1);
   }

   public StandardDeviation(int rx, int ry) {
      this.rx = rx;
      this.ry = ry;
      n = (ry * 2 + 1) * (rx * 2 + 1);
   }

   @Override
   public BufferedImage process(BufferedImage im) {
      BufferedImage out = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_RGB);

      int height = im.getHeight();
      int width = im.getWidth();

      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            final int devXYSquared = (int) deviationRectangle(im, width, height, x, y);
            final int devXY = IntMath.sqrt(devXYSquared, RoundingMode.HALF_EVEN);
            out.setRGB(x, y, ARGB.ofGrayscale(devXY).toTypeIntRgb());
         }
      }

      return out;
   }

   private double deviationRectangle(BufferedImage im, int width, int height, int x0, int y0) {
      int sum = 0;
      int sumSquares = 0;

      for (int x = x0 - rx; x <= x0 + rx; x++) {
         if (x < 0 || x >= width) {
            continue;
         }
         for (int y = y0 - ry; y <= y0 + ry; y++) {
            if (y < 0 || y >= height) {
               continue;
            }
            int valXY = im.getRGB(x, y) & 0xff;
            sum += valXY;
            sumSquares += valXY * valXY;
         }
      }

      return (sumSquares - sum * sum / (1.0 * n)) / (n - 1.0);
   }

}
