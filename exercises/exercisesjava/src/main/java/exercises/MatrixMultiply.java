package exercises;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Андрей on 22.04.2015.
 */
public class MatrixMultiply {
 <T> MatrixMultiply(){

    }
    private static CyclicBarrier barrier;
int d=0;
    public static void main(String[] args) {

        int n = 4;

        int[][] matrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = i + j + 1;
            }
        }
        int[][] resultMatrix = new int[n][n];
        barrier = new CyclicBarrier(n, new BarrierRunnable(resultMatrix));
        multiply(matrix, matrix, resultMatrix);
    }

    public static <T> void stTest(ArrayList<T> stack){
        ArrayList<Double> elem=new ArrayList<>();
        T g=(T)elem;
        stack.add(g);
    }
public void show(){
   class F{

    }

}
    private static class BarrierRunnable implements Runnable {
        int[][] CMatrix;

       BarrierRunnable(int[][] CMatrix) {
           this.CMatrix = CMatrix;
        }

        @Override
        public void run() {

            System.out.println(Arrays.deepToString(CMatrix));
        }
    }

    public static void multiply(int[][] AMatrix, int[][] BMatrix, int[][] CMatrix) {
        for (int i = 0; i < AMatrix.length; i++) {
            new Thread(new MatrixRunnable(AMatrix, BMatrix, CMatrix, i)).start();
        }
    }

    private static class MatrixRunnable implements Runnable {
        private int[][] AMatrix;
        private int[][] BMatrix;
        private int[][] CMatrix;
        private int rowNumber;

        MatrixRunnable(int[][] AMatrix, int[][] BMatrix, int[][] CMatrix, int rowNumber) {
            this.AMatrix = AMatrix;
            this.BMatrix = BMatrix;
            this.CMatrix = CMatrix;
            this.rowNumber = rowNumber;
        }

        @Override
        public void run() {
            for (int j = 0; j < BMatrix[0].length; j++) {
                for (int i = 0; i < AMatrix[0].length; i++) {
                    CMatrix[rowNumber][j] = CMatrix[rowNumber][j] + AMatrix[rowNumber][i] * BMatrix[i][j];
                }
            }
            try {
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        private class RowColumnRunnable implements Runnable {
            private int[][] AMatrix;
            private int[][] BMatrix;
            private int[][] CMatrix;
            private int x;
            private int y;

            RowColumnRunnable(int x, int y, int[][] AMatrix, int[][] BMatrix, int[][] CMatrix) {
                this.AMatrix = AMatrix;
                this.BMatrix = BMatrix;
                this.CMatrix = CMatrix;
                this.x = x;
                this.y = y;
            }

            @Override
            public void run() {
                for (int i = 0; i < AMatrix[0].length; i++) {
                    CMatrix[y][x] = CMatrix[y][x] + AMatrix[y][i] * BMatrix[i][x];
                }
            }
        }
    }



}

class A<T> {
    T t;
    public  T show(){
       return t;
    }
}

class B  extends A<Object>{
@Override
    public  Object show(){
        return "1";
    }
}
class SupJ {
    public void set(Object obj) {/*...*/}// (1)
    public String get() {return null;} // (2)
}
class SubJ  <S extends  String>  extends SupJ {

 public <S extends  String> void set(S s) {/*...*/} // (1a) Error: same erasure



@Override
  public     S get() {return null;} // (2a) Error: same erasure
}