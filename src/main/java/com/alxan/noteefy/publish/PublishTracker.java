package com.alxan.noteefy.publish;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class PublishTracker implements Tracker {
    private final Integer capacity;
    private final LinkedHashSet<UUID> set = new LinkedHashSet<>();

    public PublishTracker(Integer aCapacity) {
        capacity = aCapacity;
    }

    public void published(UUID uuid) {
        if (set.size() >= capacity) {
            set.remove(set.iterator().next());
        }
        set.add(uuid);
    }

    @Override
    public Set<UUID> getPublishedEvents() {
        return new HashSet<>(set);
    }

    public boolean isPublished(UUID uuid) {
        return set.contains(uuid);
    }
}
