/*
 * Tests for StackClass, which is stack implementation based on two queues .
 * Version 1.0
 */

package group.idru.assignment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StackClassTest {
    @Test
    public void pushPopTest() {
        StackClass<String> stack = new StackClass<>(2);
        stack.push("1");
        stack.push("2");

        assertEquals("2", stack.pop());
        assertEquals("1", stack.pop());
    }

    @Test(expected = IllegalStateException.class)
    public void pushExpectedExceptionTest() {
        StackClass<String> stack = new StackClass<>(2);
        stack.push("1");
        stack.push("2");
        stack.push("3");
    }

    @Test(expected = IllegalStateException.class)
    public void popExpectedExceptionTest() {
        StackClass<String> stack = new StackClass<>(1);
        stack.push("1");
        stack.pop();
        stack.pop();
    }

    @Test
    public void isEmptyStackTest() {
        StackClass<String> stack = new StackClass<>(1);
        stack.push("1");

        assertFalse(stack.isEmpty());

        stack.pop();

        assertTrue(stack.isEmpty());


    }
}
