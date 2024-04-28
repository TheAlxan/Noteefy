package com.alxan.noteefy.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class OrderGeneratorUnitTest {
    @Test
    public void shouldGenerateOrdersCorrectly() {
        long order1 = OrderGenerator.getOrder();
        long order2 = OrderGenerator.getOrder();
        long order3 = OrderGenerator.getOrder();
        long order4 = OrderGenerator.getOrder();

        Assertions.assertTrue(order2 > order1);
        Assertions.assertTrue(order3 > order2);
        Assertions.assertTrue(order4 > order3);
    }
}
