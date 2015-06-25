/**
 * Преобразовать массив codepoint-ов, в
 * массив byte[] в формате UTF8.
 */

package exercises.Charsets;

import java.io.UnsupportedEncodingException;

public class ArrayToUTF {
    public static void main(String[] args) {
        int[] array = {121, 2000, 2211};
        byte[] byteArray = convertArrayToUTF(array);
        try {
            String string = new String(byteArray, "UTF-8");
            System.out.println(string);
            for (int i = 0; i < string.length(); i++) {
                System.out.println(string.codePointAt(i));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static byte[] convertArrayToUTF(int[] intArray) {
        byte[] byteArray = new byte[intArray.length * 3];
        int index = 0;
        int byteIndex = 0;
        while (index != intArray.length) {
            if (intArray[index] < 0x80) {
                byteArray[byteIndex++] = (byte) intArray[index++];
            } else {
                if (intArray[index] < 0x800) {
                    byteArray[byteIndex++] = (byte) (((intArray[index] >> 6) & 0b11111) | 0b11000000);
                    byteArray[byteIndex++] = (byte) (((intArray[index++]) & 0b111111) | 0b10000000);
                } else {
                    if (intArray[index] < 0x10000) {
                        byteArray[byteIndex++] = (byte) (((intArray[index] >> 12) & 0b1111) | 0b11100000);
                        byteArray[byteIndex++] = (byte) (((intArray[index] >> 6) & 0b111111) | 0b10000000);
                        byteArray[byteIndex++] = (byte) (((intArray[index++]) & 0b111111) | 0b10000000);
                    }
                }
            }
        }
        return byteArray;
    }
}
