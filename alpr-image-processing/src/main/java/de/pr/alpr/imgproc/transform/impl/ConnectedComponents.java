package de.pr.alpr.imgproc.transform.impl;

import java.awt.image.BufferedImage;

import de.pr.alpr.imgproc.transform.Transformation;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectedComponents implements Transformation {

   /**
    * Von:
    * 
    * <em>He, Lifeng & Chao, Yuyan. (2015). A Very Fast Algorithm for
    * Simultaneously Performing Connected-Component Labeling and Euler Number
    * Computing. IEEE transactions on image processing : a publication of the
    * IEEE Signal Processing Society. 24. 10.1109/TIP.2015.2425540.</em>
    * 
    */
   public static class HCS {
      private final BufferedImage image;

      @Getter
      private final BufferedImage labelImage;

      /**
       * Repräsentanden für die Äquivalenzklassen verbundene Komponenten.
       */
      @Getter
      private final int[] r;

      /**
       * Für ein Label l bezeichne [l]/~ = { m | m Label und m~l } die
       * zugehörige Äquivalenzklasse. Ist l0 der kleinste Repräsentant in [l]/~,
       * dann gilt: l0 = r[l] und [l]/~ = {l0, next[l0], next[next[l0]], ...,
       * last[l0]}. Dabei gibt next[..] == - 1 an, dass wir das letzte Element
       * erreicht haben.
       */
      @Getter
      private final int[] next;

      /**
       * Für den kleinsten Repräsentanten l0 aus [l], ist last[l0] das letzte
       * Label in [l].
       * 
       * Siehe {@link #next}.
       */
      @Getter
      private final int[] last;

      int label = 0;

      public HCS(BufferedImage im) {
         this.image = im;
         labelImage = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_RGB);

         final int tableSize = im.getWidth() * im.getHeight() / 4;

         r = new int[tableSize];
         next = new int[tableSize];
         last = new int[tableSize];
      }

      private int b(int x, int y) {
         return image.getRGB(x, y) & 0xff;
      }

      private void setLabel(int x, int y, int label) {
         labelImage.setRGB(x, y, label);
      }

      private int getLabel(int x, int y) {
         return labelImage.getRGB(x, y) & 0xffffff;
      }

      private void newset(int label) {
         log.debug("newset, label = {}", label);
         r[label] = label;
         next[label] = -1;
         last[label] = label;
      }

      private void combine(int u, int v) {
         log.debug("combine u={}, v={}", u, v);
         if (u == v) {
            return;
         }
         if (u > v) {
            int h = u;
            u = v;
            v = h;
         }

         for (int k = v; k != -1; k = next[k]) {
            r[k] = u;
         }
         next[last[u]] = v;
         last[u] = last[v];
      }

      private void logLabelSets() {
         if (log.isDebugEnabled()) {
            log.debug("labels: {}", label);
            for (int i = 0; i < label; i++) {
               log.debug("{}", String.format("%3d: %3d %3d %3d", i, r[i], next[i], last[i]));
            }
         }
      }

      public void start() {
         for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
               if (b(x, y) > 0) {

                  if (y > 0 && b(x, y - 1) > 0) {
                     setLabel(x, y, getLabel(x, y - 1));
                  } else if (y > 0 && x < image.getWidth() - 1 && b(x + 1, y - 1) > 0) {
                     setLabel(x, y, getLabel(x + 1, y - 1));
                     if (x > 0 && b(x - 1, y - 1) > 0) {
                        combine(r[getLabel(x - 1, y - 1)], r[getLabel(x + 1, y - 1)]);
                     }
                  } else if (x > 0 && y > 0 && b(x - 1, y - 1) > 0) {
                     setLabel(x, y, getLabel(x - 1, y - 1));
                  } else {
                     label++;
                     setLabel(x, y, label);
                     newset(label);
                  }

                  x++;
                  while (x < image.getWidth() && b(x, y) > 0) {
                     setLabel(x, y, getLabel(x - 1, y));
                     if (y > 0 && b(x, y - 1) == 0 && b(x + 1, y - 1) > 0) {
                        combine(r[getLabel(x - 1, y)], r[getLabel(x + 1, y - 1)]);
                     }
                     x++;
                  }

                  // logLabelSets();

               }
            }
         }

         for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
               int label = r[getLabel(x, y)];
               if (label > 0) {
                  labelImage.setRGB(x, y, colorizeLabel(label));
               }
            }
         }
      }

      private int colorizeLabel(int label) {
         return (((label * 10) << 8) + 255) % 0xffffff;
      }

   }

   @Override
   public BufferedImage process(BufferedImage im) {
      final HCS hcs = new HCS(im);
      hcs.start();
      return hcs.getLabelImage();
   }

}
