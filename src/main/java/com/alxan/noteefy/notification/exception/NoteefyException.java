package com.alxan.noteefy.notification.exception;

import java.util.UUID;

public class NoteefyException extends Exception {
    private UUID nodeUuid;
    private Exception exception;

    public NoteefyException(UUID aNodeUuid) {
        this("General Exception", aNodeUuid);
    }

    public NoteefyException(String message, UUID aNodeUuid) {
        this(new RuntimeException("General Exception"), aNodeUuid);
    }

    public NoteefyException(Exception anException, UUID aNodeUuid) {
        super(anException);
        nodeUuid = aNodeUuid;
        exception = anException;
    }

    public UUID getNodeUuid() {
        return nodeUuid;
    }

    public Exception getException() {
        return exception;
    }
}
