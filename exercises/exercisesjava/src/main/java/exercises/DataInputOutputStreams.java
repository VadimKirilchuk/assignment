package exercises;

import java.io.*;

/**
 * Created by Андрей on 31.01.2015.
 */
public class DataInputOutputStreams {
    public static void main(String[] args) {
        try (DataOutputStream outStream = new DataOutputStream(new FileOutputStream("e:\\outStream"));
             DataInputStream inputStream =new DataInputStream(new FileInputStream("e:\\outStream"))) {
            //outStream.writeDouble(0.45);
            outStream.writeUTF("");
            System.out.println(inputStream.readUTF()+"h");
           // System.out.println(inputStream.readChar());
           // System.out.println(inputStream.readDouble());

        } catch (FileNotFoundException file) {
            System.out.println("filenotfound");
        } catch (IOException ff) {
            System.out.println("IOException");
        }
    }
}
