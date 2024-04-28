package com.alxan.noteefy.event;

import java.util.Set;

interface Taggable {
    default void addTag(String tag) {
        getTags().add(tag);
    }

    default boolean hasTag(String tag) {
        return getTags().contains(tag);
    }

    Set<String> getTags();
}
