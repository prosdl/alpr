package de.pr.alpr.imgproc.transform.impl;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import com.google.common.math.IntMath;

import de.pr.alpr.imgproc.transform.Transformation;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Slf4j
public class HoughTransform implements Transformation {
   int thetaSteps = 360;
   private double cachedCos[] = new double[thetaSteps];
   private double cachedSin[] = new double[thetaSteps];
   private double scale = 1.0;

   @Value
   public static class HoughCoordinate implements Comparable<HoughCoordinate> {
      private double theta;
      private double r;
      private int votes;

      @Override
      public int compareTo(HoughCoordinate o) {
         return -this.votes + o.votes;
      }

      public int x(double y) {
         return (int) ((r - y * Math.sin(theta)) / Math.cos(theta));
      }

      public int y(double x) {
         return (int) ((r - x * Math.cos(theta)) / Math.sin(theta));
      }

   }

   public static class Accumulator {
      private int raw[];
      private int thetaSteps;
      private double rScale;
      private int rMaxScaled;

      public Accumulator(int rMax, int thetaSteps, double rScale) {
         this.thetaSteps = thetaSteps;
         this.rScale = rScale;
         this.rMaxScaled = (int) (rMax * rScale);

         raw = new int[thetaSteps * (2 * this.rMaxScaled + 1)];
      }

      public void vote(int thetaStep, int r) {
         raw[thetaStep + ((int) (r * rScale) + rMaxScaled) * thetaSteps]++;
      }

      public TreeSet<HoughCoordinate> sort() {
         TreeSet<HoughCoordinate> set = new TreeSet<>();
         for (int thetaStep = 0; thetaStep < thetaSteps; thetaStep++) {
            for (int r = -rMaxScaled; r <= rMaxScaled; r++) {
               set.add(new HoughCoordinate(thetaStep * Math.PI / thetaSteps, r / rScale,
                     raw[thetaStep + (r + rMaxScaled) * thetaSteps]));
            }
         }
         return set;
      }

   }

   private void warmCache() {
      for (int thetaStep = 0; thetaStep < thetaSteps; thetaStep++) {
         cachedCos[thetaStep] = Math.cos(thetaStep * Math.PI / thetaSteps);
         cachedSin[thetaStep] = Math.sin(thetaStep * Math.PI / thetaSteps);
      }
   }

   @Override
   public BufferedImage process(BufferedImage im) {
      BufferedImage out = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_RGB);

      final int xMax = im.getWidth();
      final int yMax = im.getHeight();
      int rMax = IntMath.sqrt(yMax * yMax + xMax * xMax, RoundingMode.CEILING);

      Accumulator acc = new Accumulator(rMax, thetaSteps, scale);

      int c = 0;
      warmCache();
      for (int x = 0; x < xMax; x++) {
         for (int y = 0; y < yMax; y++) {
            int h = im.getRGB(x, y) & 0xff;
            if (h > 250) {
               for (int thetaStep = 0; thetaStep < thetaSteps; thetaStep++) {
                  int r = (int) Math.rint(x * cachedCos[thetaStep] + y * cachedSin[thetaStep]);
                  acc.vote(thetaStep, r);
               }

            }
            c++;

            if (log.isDebugEnabled()) {
               final int percStep = xMax * yMax / 100;
               if (c % percStep == 0) {
                  System.out.println(100.0 * c / xMax / yMax);
               }
            }
         }
      }

      TreeSet<HoughCoordinate> s = acc.sort();

      // copy im -> out
      for (int x = 0; x < xMax; x++) {
         for (int y = 0; y < yMax; y++) {
            out.setRGB(x, y, im.getRGB(x, y));
         }
      }

      Graphics2D g2d = out.createGraphics();
      g2d.setColor(Color.GREEN);
      BasicStroke bs = new BasicStroke(1);
      g2d.setStroke(bs);
      Iterator<HoughCoordinate> it = s.iterator();
      for (int i = 0; i < 100; i++) {
         if (!it.hasNext()) {
            break;
         }
         HoughCoordinate hc = it.next();
         System.out.println(hc);

         ArrayList<Integer> al = new ArrayList<>();

         int x0 = hc.x(0);
         if (x0 >= 0 && x0 < xMax) {
            al.add(x0);
            al.add(0);
         }
         int x1 = hc.x(yMax - 1);
         if (x1 >= 0 && x1 < xMax) {
            al.add(x1);
            al.add(yMax - 1);
         }
         int y0 = hc.y(0);
         if (y0 >= 0 && y0 < yMax) {
            al.add(0);
            al.add(y0);
         }
         int y1 = hc.y(xMax - 1);
         if (y1 >= 0 && y1 < yMax) {
            al.add(xMax - 1);
            al.add(y1);
         }

         if (al.size() >= 4) {
            g2d.drawLine(al.get(0), al.get(1), al.get(2), al.get(3));
         }

      }

      return out;
   }

   public HoughTransform(int thetaSteps, double scale) {
      super();
      this.thetaSteps = thetaSteps;
      this.scale = scale;
   }
}
