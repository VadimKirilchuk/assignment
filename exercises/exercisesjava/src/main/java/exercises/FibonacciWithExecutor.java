package exercises;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Андрей on 10.04.2015.
 */
public class FibonacciWithExecutor {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        long startTime = System.nanoTime();
        Future<BigInteger> element = executor.submit(new FibonacciMultithreading(8, executor));
        try {

            System.out.println(element.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        long estimatedTime = System.nanoTime() - startTime;
        System.out.println(estimatedTime);
    }

    private static class FibonacciCalculator implements Callable<Integer> {
        private int number;
        private ExecutorService executor;

        FibonacciCalculator(int number, ExecutorService executor) {
            this.number = number;
            this.executor = executor;
        }

        @Override
        public Integer call() throws Exception {

            if (number <= 2) {
                return 1;
            }

            Future<Integer> fistNumber = executor.submit(new FibonacciCalculator(number - 1, executor));
            Future<Integer> secondNumber = executor.submit(new FibonacciCalculator(number - 2, executor));

            return fistNumber.get() + secondNumber.get();
        }
    }

    private static class FibonacciCalculatorWithMemorizing implements Callable<BigInteger> {
        private static List<BigInteger> numberList = new ArrayList<>();

        static {
            numberList.add(BigInteger.ONE);
            numberList.add(BigInteger.ONE);
        }

        private int number;
        private ExecutorService executor;

        FibonacciCalculatorWithMemorizing(int number, ExecutorService executor) {
            this.number = number;
            this.executor = executor;
        }

        @Override
        public BigInteger call() throws Exception {
            if (numberList.size() >= number) {
                return numberList.get(number - 1);
            }

            Future<BigInteger> numberObject =
                    executor.submit(new FibonacciCalculatorWithMemorizing(number - 2,
                            executor));
            BigInteger prevPrevElement = numberObject.get();

            if (number % 2 == 0) {
                BigInteger prevElement = prevPrevElement.add(numberList.get(number - 4));
                numberList.add(number - 2, prevElement);
                BigInteger result = prevElement.add(prevPrevElement);
                numberList.add(number - 1, result);
                return result;
            } else {
                BigInteger prevElement = numberList.get(number - 2);
                BigInteger result = prevElement.add(prevPrevElement);
                numberList.add(number - 1, result);
                BigInteger nextElement = prevElement.add(result);
                numberList.add(number, nextElement);

                return result;
            }
        }
    }

    public static class FibonacciMultithreading implements Callable<BigInteger> {
        private static List<BigInteger> numberList = new ArrayList<>();

        static {
            numberList.add(BigInteger.ONE);
            numberList.add(BigInteger.ONE);
        }

        private int number;
        private ExecutorService executor;

        FibonacciMultithreading(int number, ExecutorService executor) {
            this.number = number;
            this.executor = executor;
        }

        @Override
        public BigInteger call() throws Exception {
            synchronized (numberList) {
                if (number <= numberList.size()) {
                    return numberList.get(number - 1);
                }
            }
            Future<BigInteger> fistNumber = executor.submit(new FibonacciMultithreading(number - 1, executor));
            Future<BigInteger> secondNumber = executor.submit(new FibonacciMultithreading(number - 2, executor));
            BigInteger result = fistNumber.get().add(secondNumber.get());
            synchronized (numberList) {
                numberList.add(result);
            }
            return result;
        }
    }
}
