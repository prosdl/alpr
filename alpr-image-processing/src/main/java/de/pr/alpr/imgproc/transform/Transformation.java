package de.pr.alpr.imgproc.transform;

import java.awt.image.BufferedImage;
import java.util.function.Function;

/**
 * Transforms a {@link BufferedImage} into a new {@link BufferedImage}.
 *
 */
public interface Transformation extends Function<BufferedImage, BufferedImage> {
   default String getName() {
      return this.getClass().getSimpleName();
   };

   BufferedImage process(BufferedImage image);

   @Override
   default BufferedImage apply(BufferedImage im) {
      return process(im);
   }
}
