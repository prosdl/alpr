package de.pr.alpr.imgproc.transform.impl;

import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import de.pr.alpr.imgproc.transform.Transformation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConnectedComponents implements Transformation {

   @Override
   public BufferedImage process(BufferedImage im) {
      final BufferedImage labelImage = new BufferedImage(im.getWidth(), im.getHeight(), BufferedImage.TYPE_INT_RGB);

      final int tableSize = im.getWidth() * im.getHeight() / 4;

      // representative label table
      final int[] r = new int[tableSize];

      // next lable of provisional label
      final int[] next = new int[tableSize];

      // last lable of equivalent label set
      final int[] last = new int[tableSize];

      int label = 0;

      for (int y = 0; y < im.getHeight(); y++) {
         for (int x = 0; x < im.getWidth(); x++) {
            if ((im.getRGB(x, y) & 0xff) > 0) {
               final Set<Integer> neighborLabels = new HashSet<>(2);

               if (x > 0 && (im.getRGB(x - 1, y) & 0xff) > 0) {
                  neighborLabels.add(labelImage.getRGB(x - 1, y) & 0xffffff);
               }
               if (x > 0 && y > 0 && (im.getRGB(x - 1, y - 1) & 0xff) > 0) {
                  neighborLabels.add(labelImage.getRGB(x - 1, y - 1) & 0xffffff);
               }
               if (y > 0 && (im.getRGB(x, y - 1) & 0xff) > 0) {
                  neighborLabels.add(labelImage.getRGB(x, y - 1) & 0xffffff);
               }
               if (y > 0 && x < im.getWidth() - 1 && (im.getRGB(x + 1, y - 1) & 0xff) > 0) {
                  neighborLabels.add(labelImage.getRGB(x + 1, y - 1) & 0xffffff);
               }

               if (neighborLabels.isEmpty()) {
                  // new label
                  log.debug("new label at {},{}: {}", x, y, label);
                  setLabel(labelImage, x, y, label);
                  r[label] = label;
                  next[label] = -1;
                  last[label] = label;
                  label++;
               } else {
                  final int conn = neighborLabels.iterator().next();
                  log.debug("connected label at {},{}: {}", x, y, conn);
                  setLabel(labelImage, x, y, conn);

                  Set<Integer> ss = neighborLabels.stream().map(z -> r[z]).collect(Collectors.toSet());
                  if (ss.size() >= 2) {
                     if (ss.size() > 2) {
                        throw new IllegalStateException();
                     }
                     Iterator<Integer> it = ss.iterator();
                     int u = it.next();
                     int v = it.next();

                     if (v < u) {
                        int s = v;
                        v = u;
                        u = s;
                     }

                     log.debug("combine at {},{} ... u={},v={}", x, y, u, v);

                     // combine
                     for (int k = v; k != -1; k = next[k]) {
                        log.debug("k={} -> r[k]={}", k, u);
                        r[k] = u;
                        // try {
                        // Thread.sleep(100);
                        // } catch (InterruptedException e) {
                        // // TODO Auto-generated catch block
                        // e.printStackTrace();
                        // }
                     }
                     next[last[u]] = v;
                     last[u] = last[v];
                     // log.debug("labels: {}", label);
                     // if (log.isDebugEnabled()) {
                     // for (int i = 0; i < label; i++) {
                     // log.debug("{}", String.format("%3d: %3d %3d %3d", i,
                     // r[i], next[i], last[i]));
                     // }
                     // }
                  }
               }
            }
         }
      }

      log.debug("labels: {}", label);
      if (log.isDebugEnabled()) {
         for (int i = 0; i < label; i++) {
            log.debug("{}", String.format("%3d: %3d %3d %3d", i, r[i], next[i], last[i]));
         }
      }

      for (int y = 0; y < im.getHeight(); y++) {
         for (int x = 0; x < im.getWidth(); x++) {
            int pix = labelImage.getRGB(x, y) & 0xffffff;
            if (pix > 0) {
               int col = (50 + r[pix] * 30) % 0xffffff;
               labelImage.setRGB(x, y, col);
            }
         }
      }
      return labelImage;
   }

   private static void setLabel(BufferedImage labelImage, int x, int y, int label) {
      labelImage.setRGB(x, y, label);
   }

}
