package de.pr.alpr.imgproc;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.GrayFilter;

import de.pr.alpr.samples.Samples;

public class Grayscale {
   public static void main(String... args) throws IOException {
      BufferedImage im = ImageIO.read(Samples.CAR001.asClassPathResource().getInputStream());
      ImageProducer producer = new FilteredImageSource(im.getSource(), new GrayFilter(true, 20));  
      Image gray = Toolkit.getDefaultToolkit().createImage(producer);
      
      BufferedImage bufferedImage= new BufferedImage(gray.getWidth(null), gray.getHeight(null), BufferedImage.TYPE_INT_RGB);
      bufferedImage.getGraphics().drawImage(gray, 0, 0, null);
      ImageIO.write(bufferedImage, "jpg", new File("/tmp/foo.jpg"));
   }
}
