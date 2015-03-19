package exercises.BasicInputOutput;

import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * simple state machine
 */
public class ZeroByteFilter {
    public static void main(String[] args) {
        byte[] array = new byte[3];
        array[0] = 121;

        array[1] = 0;
        array[2] = 115;
        InputStream input = new ByteArrayInputStream(array);
        try {
            OutputStream  output = new FileOutputStream("e:\\testArray.txt");
            filter(input, output, array.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void filter(InputStream input, OutputStream output, int bufferSize) {
        try {
            byte[] buffer = new byte[bufferSize];
            final int ZERO = 0;
            final int NUMBER = 1;
            int count;
            while ((count = input.read(buffer)) != -1) {
                int state = ZERO;
                int firstIndex = -1;
                for (int index = 0; index < count; index++) {
                    switch (state) {
                        case ZERO:
                            if (buffer[index] == 0) {
                                byte elem = buffer[index];
                                buffer[index] = buffer[++firstIndex];
                                buffer[firstIndex] = elem;
                            } else {
                                state = NUMBER;
                            }
                            break;
                        case NUMBER:
                            if (buffer[index] == 0) {
                                byte elem = buffer[index];
                                buffer[index] = buffer[++firstIndex];
                                buffer[firstIndex] = elem;
                                state = ZERO;
                            }
                            break;
                    }
                }
                output.write(buffer, ++firstIndex, bufferSize - firstIndex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

