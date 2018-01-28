package de.pr.alpr.imgproc.transform.impl;

import java.awt.image.BufferedImage;

import de.pr.alpr.imgproc.imgutil.RgbConvert.ARGB;
import de.pr.alpr.imgproc.transform.Transformation;

/**
 * SCW - Segmentation Method.
 * 
 *
 * <p>
 * C. N.E. Anagnostopoulos, I. E. Anagnostopoulos, V. Loumos, and E. Kayafas.
 * 2006. A License Plate-Recognition Algorithm for Intelligent Transportation
 * System Applications. Trans. Intell. Transport. Sys. 7, 3 (September 2006),
 * 377-392.
 * </p>
 *
 */
public class SCW implements Transformation {

   private int rxSmall, rySmall;
   private int rxBig, ryBig;

   private double t;

   public SCW() {
      this(4, 2, 8, 4, 2.0);
   }

   public SCW(int rxSmall, int rySmall, int rxBig, int ryBig, double t) {
      this.rxSmall = rxSmall;
      this.rySmall = rySmall;
      this.rxBig = rxBig;
      this.ryBig = ryBig;
      this.t = t;
   }

   @Override
   public BufferedImage process(BufferedImage im) {

      final BufferedImage smallSD = new StandardDeviation(rxSmall, rySmall).process(im);
      final BufferedImage bigSD = new StandardDeviation(rxBig, ryBig).process(im);

      final BufferedImage out = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_BYTE_BINARY);

      int height = im.getHeight();
      int width = im.getWidth();

      for (int x = 0; x < width; x++) {
         for (int y = 0; y < height; y++) {
            double mSmall = smallSD.getRGB(x, y) & 0xff;
            int mBig = bigSD.getRGB(x, y) & 0xff;

            if (mSmall > 0.0) {
               if (mBig / mSmall > t) {
                  out.setRGB(x, y, ARGB.ofGrayscale(255).toTypeIntRgb());
               }
            }
         }
      }

      return out;
   }

}
