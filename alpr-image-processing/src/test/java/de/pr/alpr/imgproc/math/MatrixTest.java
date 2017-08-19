package de.pr.alpr.imgproc.math;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import de.pr.alpr.imgproc.math.Matrix;
import de.pr.alpr.imgproc.transform.impl.Sobel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MatrixTest {
   @Test
   public void toString1x3ThrowsNoException() {
      Matrix m = new Matrix(1, 3, 1, 2, 3);
      log.info("1x3:\n{}", m.toString());
   }

   @Test
   public void toString3x1ThrowsNoException() {
      Matrix m = new Matrix(3, 1, 1, 2, 3);
      log.info("3x1:\n{}", m.toString());
   }

   @Test
   public void toString2x3ThrowsNoException() {
      Matrix m = new Matrix(2, 3, 1, 2, 3, 4, 5, 6);
      log.info("2x3:\n{}", m.toString());
   }

   @Test
   public void multiply1x3() {
      Matrix m = new Matrix(1, 3, 1, 2, 3);
      int[] r = m.multiply(1, 2, 3);
      assertThat(r, is(new int[] { 1 + 4 + 9 }));
   }

   @Test
   public void transpose() {
      Matrix m = new Matrix(2, 3, 1, 2, 3, 4, 5, 6);
      assertThat(m.transpose(), is(new Matrix(3, 2, 1, 4, 2, 5, 3, 6)));
   }

   @Test
   public void convolution() {
      Matrix image = new Matrix(3, 3, 1, 2, 2, 1, 1, 0, 2, 4, 1);
      int gx = Sobel.S_X.convolution(image);
      int gy = Sobel.S_Y.convolution(image);
      assertThat(gx, is(4));
      assertThat(gy, is(-2));
   }
}
