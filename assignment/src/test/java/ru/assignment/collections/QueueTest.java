/*
 * Tests for Queue.
 * Version 1.0
 */

package ru.assignment.collections;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueueTest {
    private Queue<String> underTest;

    @Before
    public void before() {
        underTest = new Queue<>(2);
    }

    @Test
    public void pushPopTest() {
        underTest.push("1");
        underTest.push("2");
        /**
         *
         */
        assertEquals("1", underTest.pop());
        assertEquals("2", underTest.pop());
    }

    @Test(expected = IllegalStateException.class)
    public void pushExpectedExceptionTest() {
        underTest.push("1");
        underTest.push("2");
        underTest.push("3");
    }

    @Test(expected = IllegalStateException.class)
    public void popExpectedExceptionTest() {
        underTest.push("1");
        underTest.pop();
        underTest.pop();
    }
}
