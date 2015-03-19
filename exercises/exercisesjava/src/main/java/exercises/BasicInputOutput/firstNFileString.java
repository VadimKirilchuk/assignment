package exercises.BasicInputOutput;

import java.io.*;

/**
 * Print first 10 lines of files from args array
 */
public class firstNFileString {
    public static void main(String[] args) {
        String[] array={"e:\\firstfile.txt","e:\\secondfile.txt"};
        try {
            for (String name : array) {

                printFileData(name);
                System.out.println("test");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printFileData(String name) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(name));
             BufferedWriter writer = new BufferedWriter(new FileWriter("e:\\tenString.txt",true))) {
            String string;
            int i = 0;
            while (i < 10 && ((string = reader.readLine()) != null)) {
                writer.write(string+"\r\n");
                i++;
            }
        }
    }
}
