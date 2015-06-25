package exercises;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Андрей on 02.04.2015.
 */
public class CallableExample {

    public static void main(String [] args)throws Exception{
        DirectoryAnalyzer directoryAnalyzer=new DirectoryAnalyzer("e:\\JAVA","import");

       FutureTask<Integer> future=new FutureTask<Integer>(directoryAnalyzer);
        Thread thread=new Thread(future);
        thread.start();
        System.out.println(future.get());

    }

    private static class DirectoryAnalyzer implements Callable<Integer> {
        List<Future<Integer>> futureList = new ArrayList<>();
        String fileName;
        String wordToCheck;
        int count = 0;

        DirectoryAnalyzer(String fileName, String wordToCheck) {
            this.fileName = fileName;
            this.wordToCheck = wordToCheck;
        }

        @Override
        public Integer call() {
            File[] fileList = new File(fileName).listFiles();

            for (File file : fileList) {
                if (file.isDirectory()) {
                    DirectoryAnalyzer directory = new DirectoryAnalyzer(file.getAbsolutePath(), wordToCheck);
                    FutureTask<Integer> futureObject = new FutureTask<>(directory);
                    futureList.add(futureObject);
                    Thread thread = new Thread(futureObject);
                    thread.start();
                } else {
                    if (search(file, wordToCheck)) {
                        count++;
                    }
                }
            }
            for (Future<Integer> future : futureList) {
                try {
                    count += future.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return count;
        }

        private boolean search(File file, String wordForCheck) {

            try (Scanner scanner = new Scanner(new InputStreamReader(new FileInputStream(file)))) {
                while (scanner.hasNext()) {
                    if (scanner.next().equals(wordForCheck)) {
                        return true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
            return false;
        }
    }
    @Override
    public void finalize() throws InterruptedException{

    }
}