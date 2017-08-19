package de.pr.alpr.imgproc.math;

import java.util.Arrays;

import org.springframework.util.Assert;

import lombok.Getter;

public class Matrix {
   @Getter
   private int[][] matrix;

   @Getter
   private int rows;
   @Getter
   private int columns;

   public Matrix(int m, int n, int... data) {
      Assert.state(m > 0, "m > 0");
      Assert.state(n > 0, "n > 0");
      Assert.state(data.length == n * m, "matrix length must equal n*m");

      rows = m;
      columns = n;

      matrix = new int[m][];
      for (int i = 0; i < m; i++) {
         matrix[i] = new int[n];
         System.arraycopy(data, n * i, matrix[i], 0, n);
      }
   }

   public Matrix transpose() {
      int[] d = new int[columns * rows];
      for (int j = 0; j < columns; j++) {
         for (int i = 0; i < rows; i++) {
            d[j * rows + i] = matrix[i][j];
         }
      }
      return new Matrix(columns, rows, d);
   }

   @Override
   public String toString() {
      StringBuilder sb = new StringBuilder();
      for (int y = 0; y < rows; y++) {
         for (int x = 0; x < columns; x++) {
            sb.append("m[").append(y).append(",").append(x).append("]=");
            sb.append(matrix[y][x]);
            sb.append(" ");
         }
         sb.append("\n");
      }
      return sb.toString();
   }

   public int[] multiply(int... vector) {
      Assert.state(vector.length == columns, "vector length must be " + columns);

      int[] result = new int[rows];
      for (int i = 0; i < rows; i++) {
         int sum = 0;
         for (int j = 0; j < columns; j++) {
            sum += matrix[i][j] * vector[j];
         }
         result[i] = sum;
      }

      return result;
   }

   public int convolution(Matrix m) {
      Assert.state(rows == columns && m.rows == m.columns && rows == m.rows, "only rows == columns supported");

      int sum = 0;
      for (int i = 0; i < rows; i++) {
         for (int j = 0; j < columns; j++) {
            sum += matrix[i][j] * m.getMatrix()[j][i];
         }
      }

      return sum;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + Arrays.deepHashCode(matrix);
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      Matrix other = (Matrix) obj;
      if (!Arrays.deepEquals(matrix, other.matrix))
         return false;
      return true;
   }

}
