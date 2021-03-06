package de.pr.alpr.imgproc.transform.impl;

import java.awt.image.BufferedImage;
import java.math.RoundingMode;

import com.google.common.math.IntMath;

import de.pr.alpr.imgproc.imgutil.RgbConvert;
import de.pr.alpr.imgproc.imgutil.RgbConvert.ARGB;
import de.pr.alpr.imgproc.math.Matrix;
import de.pr.alpr.imgproc.transform.Transformation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Sobel implements Transformation {

   /**
    * Sobel edge operator x-Richtung.
    */
   public static final Matrix S_X = new Matrix(3, 3, -1, 0, 1, -2, 0, 2, -1, 0, 1);

   /**
    * Sobel edge operator y-Richtung.
    */
   public static final Matrix S_Y = S_X.transpose();

   @Override
   public BufferedImage process(BufferedImage im) {
      BufferedImage out = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_RGB);

      int height = im.getHeight();
      int width = im.getWidth();

      int c = 0;
      for (int y = 1; y < height - 1; y++) {
         for (int x = 1; x < width - 1; x++) {
            ARGB b00 = RgbConvert.toARGB(im.getRGB(x - 1, y - 1));
            ARGB b01 = RgbConvert.toARGB(im.getRGB(x, y - 1));
            ARGB b02 = RgbConvert.toARGB(im.getRGB(x + 1, y - 1));
            ARGB b10 = RgbConvert.toARGB(im.getRGB(x - 1, y));
            ARGB b11 = RgbConvert.toARGB(im.getRGB(x, y));
            ARGB b12 = RgbConvert.toARGB(im.getRGB(x + 1, y));
            ARGB b20 = RgbConvert.toARGB(im.getRGB(x - 1, y + 1));
            ARGB b21 = RgbConvert.toARGB(im.getRGB(x, y + 1));
            ARGB b22 = RgbConvert.toARGB(im.getRGB(x + 1, y + 1));

            Matrix imMatrix = new Matrix(3, 3, b00.getBlue(), b01.getBlue(), b02.getBlue(), b10.getBlue(), b11.getBlue(),
                  b12.getBlue(), b20.getBlue(), b21.getBlue(), b22.getBlue());

            int gx = S_X.convolution(imMatrix);
            int gy = S_Y.convolution(imMatrix);
            int gradient = IntMath.sqrt(gx * gx + gy * gy, RoundingMode.HALF_DOWN);

            if (log.isDebugEnabled())
               if (c % 1000 == 0) {
                  log.debug("completion: {}%", 100.0 * y / height);
               }
            c++;
            out.setRGB(x, y, new ARGB(0, gradient, gradient, gradient).toTypeIntRgb());
         }
      }
      return out;
   }
}
