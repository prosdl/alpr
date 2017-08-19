package de.pr.alpr.imgproc.transform;

import java.awt.image.BufferedImage;

public interface Transformation {
   default String getName() {
      return this.getClass().getSimpleName();
   };

   BufferedImage process(BufferedImage image);
}
