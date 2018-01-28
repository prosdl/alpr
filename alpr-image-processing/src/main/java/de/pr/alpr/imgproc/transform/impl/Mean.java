package de.pr.alpr.imgproc.transform.impl;

import java.awt.image.BufferedImage;

import de.pr.alpr.imgproc.imgutil.RgbConvert.ARGB;
import de.pr.alpr.imgproc.transform.Transformation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Berechnet ein Bild aus Mittelwerten. Dazu wird an der Koordinate (x,y) ein
 * Rechteck R mit den folgenden Eckpunkten gebildet: (x-rx,y-ry) und
 * (x+rx,y+ry). Über dieses wird der Mittelwert u(R) gebildet, und für das neue
 * Bild J dann J(x,y) := u(R) gesetzt.
 * </p>
 * 
 * <p>
 * Voraussetzung ist, dass das eingehende Bild ein Graustufen-Bild ist.
 * </p>
 *
 */
@AllArgsConstructor
@NoArgsConstructor
public class Mean implements Transformation {

   private int rx = 1;

   private int ry = 1;

   @Override
   public BufferedImage process(final BufferedImage im) {
      BufferedImage out = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_RGB);

      int height = im.getHeight();
      int width = im.getWidth();

      int n = (ry * 2 + 1) * (rx * 2 + 1);

      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            int meanXY = sumRectangle(im, width, height, x, y) / n;
            out.setRGB(x, y, ARGB.ofGrayscale(meanXY).toTypeIntRgb());
         }
      }

      return out;
   }

   private int sumRectangle(BufferedImage im, int width, int height, int x0, int y0) {
      int sum = 0;

      for (int x = x0 - rx; x <= x0 + rx; x++) {
         if (x < 0 || x >= width) {
            continue;
         }
         for (int y = y0 - ry; y <= y0 + ry; y++) {
            if (y < 0 || y >= height) {
               continue;
            }
            sum += im.getRGB(x, y) & 0xff;
         }
      }

      return sum;
   }

}
