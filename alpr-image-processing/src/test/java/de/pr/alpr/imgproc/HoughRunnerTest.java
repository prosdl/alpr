package de.pr.alpr.imgproc;

import java.io.IOException;

import org.junit.Test;

import de.pr.alpr.imgproc.transform.impl.Grayscale;
import de.pr.alpr.imgproc.transform.impl.HoughTransform;
import de.pr.alpr.imgproc.transform.impl.Sobel;
import de.pr.alpr.imgproc.transform.impl.Thresholding;
import de.pr.alpr.samples.Samples;

public class HoughRunnerTest {

   @Test
   public void runSamples() throws IOException {
      for (Samples s : Samples.values()) {
         Runner.builder()//
               .inputStream(s.asClassPathResource().getInputStream())//
               .outputFile("/tmp/hough_" + s.name() + ".jpg")//
               .transformation(new Grayscale(10))//
               .transformation(new Sobel())//
               .transformation(new Thresholding(30))//
               .transformation(new HoughTransform(90, 1.0))//
               .build()//
               .runTransformations();
      }
   }
}
