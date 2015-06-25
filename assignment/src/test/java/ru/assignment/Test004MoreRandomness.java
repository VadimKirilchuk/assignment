package ru.assignment;

import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.annotations.Seed;
import com.carrotsearch.randomizedtesting.annotations.Seeds;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        Test004MoreRandomness.OrderRandomized.class,
        Test004MoreRandomness.OrderRandomizedButFixed.class,
        Test004MoreRandomness.OrderRegression.class
})
public class Test004MoreRandomness {
    public static class OrderRandomized extends RandomizedTest {
        @Test
        public void method1() {
            System.out.println("method1, " + randomInt());
        }

        @Test
        public void method2() {
            System.out.println("method2, " + randomInt());
        }

        @Test
        public void method3() {
            System.out.println("method3, " + randomInt());
        }

        @AfterClass
        public static void empty() {
            System.out.println("--");
        }
    }

    @Seed("deadbeef")
    public static class OrderRandomizedButFixed extends OrderRandomized {
    }

    public static class OrderRegression extends RandomizedTest {
        @Seeds({
                @Seed("deadbeef"),
                @Seed()
        })
        @Test
        public void regression() {
            System.out.println("regression, " + randomInt());
        }
    }
}
