package com.alxan.noteefy.subscribe;

import com.alxan.noteefy.event.EventHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HandlerRegistryUnitTest {
    @Test
    public void shouldRegisterHandler() {
        HandlerRegistry handlerRegistry = new HandlerRegistry();
        EventHandler<Integer> eventHandler = event -> {};
        handlerRegistry.register(Integer.class, eventHandler);

        Assertions.assertEquals(eventHandler, handlerRegistry.getHandler(Integer.class));
    }

    @Test
    public void shouldLookForSuperclass() {
        HandlerRegistry handlerRegistry = new HandlerRegistry();
        EventHandler<Object> eventHandler = event -> {};
        handlerRegistry.register(Object.class, eventHandler);

        Assertions.assertEquals(eventHandler, handlerRegistry.getHandler(Integer.class));
    }
}
