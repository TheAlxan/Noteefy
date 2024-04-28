package com.alxan.noteefy.notification;

import com.alxan.noteefy.event.Event;

public class Notification<T> extends Event<T> {
    private final Level level;

    public Notification(T content, Level aLevel) {
        super(content);
        level = aLevel;
    }

    public Level getLevel() {
        return level;
    }
}
