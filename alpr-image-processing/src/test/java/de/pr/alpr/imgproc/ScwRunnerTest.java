package de.pr.alpr.imgproc;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import de.pr.alpr.imgproc.transform.impl.Grayscale;
import de.pr.alpr.imgproc.transform.impl.SauvolaAdaptiveThresholding;
import de.pr.alpr.samples.Samples;

public class ScwRunnerTest {

   @Rule
   public final Stopwatch watch = new Stopwatch() {
      @Override
      protected void succeeded(long nanos, Description description) {
         System.out.println(description.getMethodName() + " succeeded, time taken " + nanos / 1000000000.0);
      }
   };

   @Test
   public void runSamples() throws IOException {
      for (Samples s : Samples.values()) {
         Runner.builder()//
               .inputStream(s.asClassPathResource().getInputStream())//
               .outputFile("/tmp/scw" + s.name() + ".jpg")//
               .transformation(new Grayscale(10))//
               // .transformation(new Mean(2, 2))//
               // .transformation(new StandardDeviation(4, 2))//
               // .transformation(new SCW())//
               .transformation(new SauvolaAdaptiveThresholding()).build()//
               .runTransformations();
      }
   }
}
