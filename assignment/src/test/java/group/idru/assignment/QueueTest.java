/*
 * Tests for Queue.
 * Version 1.0
 */

package group.idru.assignment;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class QueueTest {
    @Test
    public void pushPopTest() {
        Queue<String> queue = new Queue<>(2);
        queue.push("1");
        queue.push("2");
        assertEquals("1", queue.pop());
        assertEquals("2", queue.pop());
    }

    @Test(expected = IllegalStateException.class)
    public void pushExpectedExceptionTest() {
        Queue queue = new Queue(2);
        queue.push("1");
        queue.push("2");
        queue.push("3");
    }

    @Test(expected = IllegalStateException.class)
    public void popExpectedExceptionTest() {
        Queue queue = new Queue(1);
        queue.push("1");
        queue.pop();
        queue.pop();
    }
}
