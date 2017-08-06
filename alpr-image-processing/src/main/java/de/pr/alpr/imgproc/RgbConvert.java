package de.pr.alpr.imgproc;

import lombok.AllArgsConstructor;
import lombok.Value;

public class RgbConvert {
   @Value
   @AllArgsConstructor
   public static class ARGB {
      int alpha, red, green, blue;

      public ARGB(int rgb) {
         alpha = (rgb >> 24) & 0xFF;
         red = (rgb >> 16) & 0xFF;
         green = (rgb >> 8) & 0xFF;
         blue = (rgb) & 0xFF;
      }

      public int toTypeIntArgb() {
         return (alpha << 24) | (red << 16) | (green << 8) | blue;
      }

      public int toTypeIntRgb() {
         return (red << 16) | (green << 8) | blue;
      }
   }

   public static ARGB toARGB(int argb) {
      return new ARGB(argb);
   }
}
