package com.alxan.noteefy.notification;

import com.alxan.noteefy.publish.broadcaster.BaseBroadcaster;

public class NotificationCenter extends BaseBroadcaster {
    private static final NotificationCenter instance = new NotificationCenter();

    public static void notify(Notification<?> notification) {
        if (instance.hasSubscribers())
            instance.publish(notification);
    }

    public static void error(Exception exception) {
        if (instance.hasSubscribers()) {
            Notification<?> notification = new Notification<>(exception, Level.ERROR);
            notify(notification);
        }
    }

    public static void registerListener(NotificationListener listener) {
        instance.register(listener);
    }

    public static void unregisterListener(NotificationListener listener) {
        instance.unregister(listener);
    }
}
