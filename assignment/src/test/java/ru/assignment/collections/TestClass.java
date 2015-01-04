package ru.assignment.collections;
/**
 * Created by Андрей on 02.01.2015.
 */
public class TestClass {
    public String reverse(String string) {
        char[] array = string.toCharArray();
        StringBuilder builder = new StringBuilder(array.length);
        for (int i = array.length - 1; i >= 0; i--) {
            builder.append(array[i]);
        }
        return builder.toString();
    }
}
