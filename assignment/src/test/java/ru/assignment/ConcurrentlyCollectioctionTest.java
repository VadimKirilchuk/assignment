package ru.assignment;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static junitparams.JUnitParamsRunner.$;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Андрей on 23.03.2015.
 */
@RunWith(JUnitParamsRunner.class)
public class ConcurrentlyCollectioctionTest {
    List<Integer> list;
    List<Thread> threadList;

    private class ListClient implements Runnable {
        Integer element;

        ListClient(int element) {
            this.element = element;
        }

        public void run() {
            list.add(element);
        }
    }

    @Before
    public void init() {
        list = new ArrayList<>();
        threadList = new ArrayList<>();
    }

    @Test
    @Parameters
    public void checkCountOfListElements(int count) {
        for (int i = 1; i <= count; i++) {
            Thread thread = new Thread(new ListClient(i));
            threadList.add(thread);
        }

        for (Thread thread : threadList) {
            thread.start();

        }
        try {
            for (Thread thread : threadList) {
                thread.join();
            }
        } catch (InterruptedException e) {
            fail(e.toString());
        }
        assertTrue(list.size()==count);
        for (int i = 1; i <= count; i++) {
            assertTrue("actual int " + i + " list elem " + list.get(i - 1),
                      (list.get(i - 1)) == i);
        }
    }

    private Object[] parametersForCheckCountOfListElements() {
        return $(5, 10, 50);
    }
}
