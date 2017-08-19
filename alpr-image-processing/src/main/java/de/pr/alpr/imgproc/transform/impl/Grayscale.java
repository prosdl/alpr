package de.pr.alpr.imgproc.transform.impl;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;

import javax.swing.GrayFilter;

import de.pr.alpr.imgproc.transform.Transformation;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Grayscale implements Transformation {
   private int grayPercentage = 20;

   @Override
   public BufferedImage process(BufferedImage image) {
      ImageProducer producer = new FilteredImageSource(image.getSource(), new GrayFilter(true, grayPercentage));
      Image gray = Toolkit.getDefaultToolkit().createImage(producer);

      BufferedImage bufferedImage = new BufferedImage(gray.getWidth(null), gray.getHeight(null), BufferedImage.TYPE_INT_RGB);
      bufferedImage.getGraphics().drawImage(gray, 0, 0, null);
      return bufferedImage;
   }
}
