package de.pr.alpr.imgproc.transform.impl;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import de.pr.alpr.imgproc.transform.Transformation;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GaussianBlur implements Transformation {
   private final int kernelSize;

   private static Kernel gaussian(int size) {
      int r = size * size;
      float[] matrix = new float[r];
      for (int k = 1; k <= r; k++) {
         long p = pascalTriangle(r, k);
         matrix[k - 1] = p / (float) Math.pow(2.0, r - 1.0);
      }
      return new Kernel(size, size, matrix);
   }

   public static long pascalTriangle(int r, int k) {
      if (r == 1 || k <= 1 || k >= r)
         return 1L;
      return pascalTriangle(r - 1, k - 1) + pascalTriangle(r - 1, k);
   }

   @Override
   public BufferedImage process(BufferedImage image) {
      Kernel kernel = gaussian(kernelSize);
      ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
      return op.filter(image, null);
   }

}
