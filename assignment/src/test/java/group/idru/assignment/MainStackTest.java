/*
 * Tests for MainStack, which is stack implementation based on two queues .
 * Version 1.0
 */

package group.idru.assignment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MainStackTest {
    @Test
    public void pushPopTest() {
        MainStack<String> stack = new MainStack<>(2);
        stack.push("1");
        stack.push("2");

        assertEquals("2", stack.pop());
        assertEquals("1", stack.pop());
    }

    @Test(expected = IllegalStateException.class)
    public void pushExpectedExceptionTest() {
        MainStack<String> stack = new MainStack<>(2);
        stack.push("1");
        stack.push("2");
        stack.push("3");
    }

    @Test(expected = IllegalStateException.class)
    public void popExpectedExceptionTest() {
        MainStack<String> stack = new MainStack<>(1);
        stack.push("1");
        stack.pop();
        stack.pop();
    }

    @Test
    public void isEmptyStackTest() {
        MainStack<String> stack = new MainStack<>(1);
        stack.push("1");

        assertFalse(stack.isEmpty());

        stack.pop();

        assertTrue(stack.isEmpty());


    }
}
