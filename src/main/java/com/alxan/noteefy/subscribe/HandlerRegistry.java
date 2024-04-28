package com.alxan.noteefy.subscribe;


import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.event.Handler;

import java.util.HashMap;
import java.util.Map;

class HandlerRegistry {
    private final Map<String, Handler<?>> handlers = new HashMap<>();

    public <T> void register(Class<T> clazz, Handler<? extends Event<T>> handler) {
        handlers.put(clazz.getCanonicalName(), handler);
    }

    @SuppressWarnings("unchecked") //TODO Come up with better solution
    public <T> Handler<Event<T>> getHandler(Class<T> clazz) {
        Handler<?> handler = handlers.get(clazz.getCanonicalName());
        Class<?> type = clazz;
        while (handler == null && type.getSuperclass() != null) {
            type = type.getSuperclass();
            handler = handlers.get(type.getCanonicalName());
        }
        return (Handler<Event<T>>) handler;
    }
}
