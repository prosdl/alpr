package de.pr.alpr.imgproc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import de.pr.alpr.imgproc.transform.Transformation;
import de.pr.alpr.imgproc.transform.impl.Grayscale;
import de.pr.alpr.imgproc.transform.impl.HoughTransform;
import de.pr.alpr.imgproc.transform.impl.Sobel;
import de.pr.alpr.imgproc.transform.impl.Thresholding;
import de.pr.alpr.samples.Samples;
import lombok.Builder;
import lombok.Singular;
import lombok.extern.slf4j.Slf4j;

@Builder
@Slf4j
public class Runner {
   @Singular
   private List<Transformation> transformations;

   private InputStream inputStream;

   private String outputFile;

   public void runTransformations() throws IOException {
      int count = 0;
      BufferedImage im = null;
      for (Transformation t : transformations) {
         if (count++ == 0) {
            im = ImageIO.read(inputStream);
         }

         log.info("step {} - running ... {}", count, t.getName());
         im = t.process(im);

      }
      ImageIO.write(im, outputFile.substring(outputFile.lastIndexOf('.') + 1), new File(outputFile));
   }

   public static void main(String[] args) throws IOException {
      Runner.builder()//
            .inputStream(Samples.CAR001.asClassPathResource().getInputStream())//
            .outputFile("/tmp/car001.jpg")//
            .transformation(new Grayscale(30))//
            .transformation(new Sobel())//
            .transformation(new Thresholding(25))//
            .transformation(new HoughTransform(180, 1.0))//
            .build()//
            .runTransformations();
   }
}
