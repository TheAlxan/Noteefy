package com.alxan.noteefy.log;

import com.alxan.noteefy.common.Identifiable;
import com.alxan.noteefy.notification.Level;
import com.alxan.noteefy.notification.Notification;
import com.alxan.noteefy.notification.NotificationCenter;
import com.alxan.noteefy.notification.exception.NoteefyException;

import java.util.UUID;

public class DefaultLogger implements Logger {
    private UUID nodeId;

    public DefaultLogger(Identifiable node) {
        nodeId = node.getUUID();
    }

    public void log(String log, Level level) {
        if (level == Level.ERROR) {
            error(log);
            return;
        }
        Notification<String> notification = new Notification<>(log, level);
        NotificationCenter.notify(notification);
    }

    @Override
    public void error(String log) {
        NoteefyException exception = new NoteefyException(log, nodeId);
        Notification<? extends NoteefyException> notification = new Notification<>(exception, Level.ERROR);
        NotificationCenter.notify(notification);
    }

    @Override
    public void trace(String log) {
        log(log, Level.TRACE);
    }

    @Override
    public void warn(String log) {
        log(log, Level.WARN);
    }

    @Override
    public void debug(String log) {
        log(log, Level.DEBUG);
    }

    @Override
    public void info(String log) {
        log(log, Level.INFO);
    }
}
