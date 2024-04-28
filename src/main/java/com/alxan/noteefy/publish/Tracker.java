package com.alxan.noteefy.publish;

import java.util.Set;
import java.util.UUID;

public interface Tracker {
    Set<UUID> getPublishedEvents();

    boolean isPublished(UUID uuid);

    void published(UUID uuid);
}
