package exercises;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Phaser;

/**
 * Created by Андрей on 25.04.2015.
 */
public class Gener {
    public static void main(String [] args){


        String [] str=new String[1];
        Object[] obj=str;
        obj[0]=new Integer("1");
        str[0].toString();
Number[] hh=new Number[1];


        List<Double>[] arrayOfListsOfDouble
                = (List<Double>[]) new List[1]; // (1) Unchecked cast warning!
        arrayOfListsOfDouble[0] = Arrays.asList(10.10); // (2) Initialize
        List<? extends Number>[] arrayofListsOfExtNums = arrayOfListsOfDouble; // (3)
        arrayofListsOfExtNums[0] = Arrays.asList(10); // (4) Array storage check ok
        Double firstOne = arrayOfListsOfDouble[0].get(0); // (5) ClassCastException!


        int [] a={1,2,3};
        List<Double>[] d=(List<Double>[])new ArrayList[1];
        d[0]=new ArrayList<Double>();
        List<? extends Number> []s=d;
        s[0]= Arrays.asList(10.0);
        Double k=d[0].get(0);

    }

    public static <T> T[] cast(int [] a){
        T[] array=(T[]) new Object[3];
        int i=0;
        for(int elem:a){
            array[i]=(T)new Integer(elem);
        }
        return array;
    }
}
