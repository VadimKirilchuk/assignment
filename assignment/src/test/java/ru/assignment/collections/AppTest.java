package ru.assignment.collections;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
@RunWith(JUnitParamsRunner.class)
public class AppTest  {

    TestClass tclass;
    HashMap<String,String> map;
    @Before
    public void env(){
        tclass=new TestClass();
        map=new HashMap<>();
    }
    @Test
    @Parameters
    public void reverseTest(String stringForReverse, String stringForCheck){
        assertEquals(tclass.reverse(stringForReverse),stringForCheck);
    }




    private Object[] parametersForReverseTest(){
        return $($(new String("testmy"),new String("ymtset")),
                 $(new String(),new String()),
                 $(new String("12"),new String("21")));
    }


    @Test
    @Parameters
    public void hashMapTest(String key, String value){
        map.put(key,value);
        assertEquals(map.get(key), value);
    }

    private Object[] parametersForHashMapTest(){
        return $($(new String("testmy"),new String("ymtset")),
                 $(new String(),new String()),
                 $(new String("12"),new String("21")));
    }

}

