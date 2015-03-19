package exercises.BasicInputOutput;

import java.util.Scanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Check symbols, strings and words count at file
 */
public class CountCharacters {
    public static void main(String[] args) {
        int [] array=checkStringCount("e:\\firstfile.txt");
        System.out.println("string count "+ array[0]+" char count "+ array[1]+" word count "+ array[2]);

    }

    public static int[] checkStringCount(String fileName) {
        int stringCount=0;
        int charCount=0;
        int wordCount=0;
        try {
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String string;

                while ((string=reader.readLine())!=null){
                    stringCount++;
                    charCount+=string.length();
                    Scanner scanner=new Scanner(string);
                    while (scanner.hasNext()){
                        scanner.next();
                        wordCount++;
                    }

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new int[]{stringCount,charCount,wordCount} ;
    }
}
