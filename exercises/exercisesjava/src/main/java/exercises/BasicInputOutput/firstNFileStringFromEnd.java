package exercises.BasicInputOutput;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * last ten lines from file
 */
public class firstNFileStringFromEnd {
    public static void main(String[] args) {
        String[] array = {"e:\\firstfile.txt"};
        try {
            for (String name : array) {

                printFileData(name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printFileData(String name) throws IOException {
        StringBuilder builder = new StringBuilder();
        int stringCount = 10;
        char symb;
        try (RandomAccessFile reader = new RandomAccessFile(name, "r");
             BufferedWriter writer = new BufferedWriter(new FileWriter("e:\\String.txt", true))) {
            long point = reader.length() - 1;
            String string = null;
            reader.seek(point);
            while (stringCount > 0 && point >= 0) {
                reader.seek(point);
                symb = (char) reader.read();
                if (symb == '\n') {
                    builder.reverse();
                    string = builder.toString();
                    writer.write(string + System.getProperty("line.separator"));
                    builder = new StringBuilder();
                    stringCount++;
                } else {
                    if (symb != '\r') {
                        builder.append(symb);
                    }
                }
                point = point - 1;
            }
            if (builder.length() != 0) {
                builder.reverse();
                string = builder.toString();
                writer.write(string + System.getProperty("line.separator"));
            }
        }
    }
}
