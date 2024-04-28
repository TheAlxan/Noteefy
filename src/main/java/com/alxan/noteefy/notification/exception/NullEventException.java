package com.alxan.noteefy.notification.exception;

import java.util.UUID;

public class NullEventException extends NoteefyException {
    public NullEventException(UUID nodeUuid) {
        super("Null event.", nodeUuid);
    }
}