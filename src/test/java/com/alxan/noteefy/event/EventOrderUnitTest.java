package com.alxan.noteefy.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventOrderUnitTest {
    @Test
    public void shouldHaveOrder() {
        EventOrder eventOrder1 = new EventOrder();
        EventOrder eventOrder2 = new EventOrder();
        EventOrder eventOrder3 = new EventOrder();
        EventOrder eventOrder4 = new EventOrder();

        Assertions.assertTrue(eventOrder2.getOrder() > eventOrder1.getOrder());
        Assertions.assertNotSame(eventOrder2.getOrder(), eventOrder1.getOrder());
        Assertions.assertTrue(eventOrder3.getOrder() > eventOrder2.getOrder());
        Assertions.assertNotSame(eventOrder3.getOrder(), eventOrder2.getOrder());
        Assertions.assertTrue(eventOrder4.getOrder() > eventOrder3.getOrder());
        Assertions.assertNotSame(eventOrder4.getOrder(), eventOrder3.getOrder());
    }
}
