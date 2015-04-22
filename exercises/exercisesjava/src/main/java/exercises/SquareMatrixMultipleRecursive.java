package exercises;

import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Андрей on 20.04.2015.
 */
public class SquareMatrixMultipleRecursive {
    private static CyclicBarrier barrier;
    private static int[][] CMatrix;

    public static void main(String[] args) {

        int n = 64;
        int matrix[][] = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = i + j +1;
            }
        }
System.out.println(Arrays.deepToString(matrix));
        barrier = new CyclicBarrier(n * n * n, new BarrierRunnable());
        CMatrix = new int[n][n];
        new Matrix(matrix, matrix, CMatrix,0,0,0,0,n).multiply();
    }

    private static class BarrierRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(Arrays.deepToString(CMatrix));
        }
    }

    private static class Matrix implements Runnable {
        private int[][] AMatrix;
        private int[][] BMatrix;
        private int[][] CMatrix;
        int CYCoordinate;
        int CXCoordinate;
        int AYCoordinate;
        int AXCoordinate;
        int countOfActiveElements;

        Matrix(int[][] AMatrix, int[][] BMatrix, int[][] CMatrix, int CYCoordinate, int CXCoordinate,
               int AYCoordinate, int AXCoordinate, int countOfActiveElements) {
            this.AMatrix = AMatrix;
            this.BMatrix = BMatrix;
            this.CMatrix = CMatrix;
            this.CYCoordinate = CYCoordinate;
            this.CXCoordinate = CXCoordinate;
            this.AYCoordinate = AYCoordinate;
            this.AXCoordinate = AXCoordinate;
            this.countOfActiveElements = countOfActiveElements;
        }

        public void multiply() {

            new Thread(this).start();
        }

        @Override
        public void run() {
            makeMultiply(AMatrix, BMatrix, CMatrix, CYCoordinate, CXCoordinate,
                    AYCoordinate, AXCoordinate, countOfActiveElements);
        }

        private void makeMultiply(int[][] AMatrix, int[][] BMatrix, int[][] CMatrix,
                                  int CYCoordinate, int CXCoordinate,
                                  int AYCoordinate, int AXCoordinate, int countOfActiveElements) {

            int n = countOfActiveElements / 2;

            if (countOfActiveElements == 1) {
                synchronized (CMatrix) {
                    CMatrix[CYCoordinate][CXCoordinate] = CMatrix[CYCoordinate][CXCoordinate]
                            + (AMatrix[AYCoordinate][AXCoordinate] * BMatrix[AXCoordinate][CXCoordinate]);
                }
                /*
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                */
            } else {

                for (int i = CYCoordinate; i < (CYCoordinate + countOfActiveElements); i += n) {
                    for (int j = CXCoordinate; j < (CXCoordinate + countOfActiveElements); j += n) {

                        new Thread(new Matrix(AMatrix, BMatrix, CMatrix, i, j, AYCoordinate, AXCoordinate, n)).start();
                        new Thread(new Matrix(AMatrix, BMatrix, CMatrix, i, j, AYCoordinate, AXCoordinate+n, n)).start();

                    }
                    AYCoordinate += n;
                }
            }
        }
    }
}

