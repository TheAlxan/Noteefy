package com.alxan.noteefy.event;

public interface Handler<T> {
    void handle(T data);
}
