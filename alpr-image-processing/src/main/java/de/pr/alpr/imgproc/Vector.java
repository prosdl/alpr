package de.pr.alpr.imgproc;

import org.springframework.util.Assert;

import lombok.Getter;

public class Vector {
   @Getter
   private int[] vector;

   @Getter
   private int rows;

   public Vector(int m, int... data) {
      Assert.state(m > 0, "m > 0");
      Assert.state(data.length == m, "vector length must equal m");
      rows = m;
      vector = new int[m];
      System.arraycopy(data, 0, vector, 0, m);
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int y = 0; y < rows; y++) {
         sb.append("v[").append(y).append("]=");
         sb.append(vector[y]);
         sb.append(" ");
         sb.append("\n");
      }
      return sb.toString();
   }
}
