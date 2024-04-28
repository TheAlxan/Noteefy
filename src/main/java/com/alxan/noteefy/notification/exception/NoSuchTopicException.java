package com.alxan.noteefy.notification.exception;

import java.util.UUID;

public class NoSuchTopicException extends NoteefyException {
    public NoSuchTopicException(UUID publisherUuid, String topic) {
        super("No such topic. Topic: " + topic, publisherUuid);
    }
}