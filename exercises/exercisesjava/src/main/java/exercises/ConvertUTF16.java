package exercises;

/**
 * Created by Андрей on 05.02.2015.
 */
public class ConvertUTF16 {
    public static void main(String[] args) {
        int[] intArray = {35, 115, 65890};
        String string=new String(convertFromIntToChar(intArray));
        System.out.println(string.codePointAt(2));
    }

    public static char[] convertFromIntToChar(int[] intArray) {
        char[] charArray = new char[intArray.length * 2];
        int intIndex = 0;
        int charIntex = 0;
        while (intIndex != intArray.length) {
            if (intArray[intIndex] < 65536) {
                charArray[charIntex++] = (char) intArray[intIndex++];
            } else {
                charArray[charIntex++] = (char) ((((intArray[intIndex] - 0x10000) >> 10) & 0b1111111111) + 0xD800);
                charArray[charIntex++] = (char) ((((intArray[intIndex++] - 0x10000)) & 0b1111111111) + 0xDC00);
            }
        }
        return charArray;
    }

   
}
