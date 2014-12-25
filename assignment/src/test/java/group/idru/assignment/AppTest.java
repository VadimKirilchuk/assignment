package group.idru.assignment;

import org.junit.Assert;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest

{


    /**
     * Rigourous Test :-)
     */
    @org.junit.Test
    public void testQue() {
        Queue<String> queue=new Queue<>(3);
        queue.push("1");

        queue.push("2");
        queue.push("3");
        queue.pop();
        queue.push("5");
        Assert.assertEquals("2", queue.pop());
    }
}
