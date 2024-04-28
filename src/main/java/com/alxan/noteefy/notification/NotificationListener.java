package com.alxan.noteefy.notification;

import com.alxan.noteefy.event.Handler;
import com.alxan.noteefy.subscribe.Listener;

public class NotificationListener extends Listener {
    public <T> void doOnNotification(Class<T> clazz, Handler<? extends Notification<T>> block) {
        doOnEvent(clazz, block);
    }

    public <T extends Exception> void doOnException(Class<T> clazz, Handler<? extends Notification<T>> block) {
        doOnNotification(clazz, block);
    }
}
