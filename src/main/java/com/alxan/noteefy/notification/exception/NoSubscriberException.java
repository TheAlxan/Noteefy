package com.alxan.noteefy.notification.exception;

import java.util.UUID;

public class NoSubscriberException extends NoteefyException {
    public NoSubscriberException(UUID publisherUuid, UUID eventUuid) {
        super("No subscriber provided. Event: " + eventUuid, publisherUuid);
    }
}