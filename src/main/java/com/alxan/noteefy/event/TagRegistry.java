package com.alxan.noteefy.event;

import java.util.HashSet;
import java.util.Set;

class TagRegistry implements Taggable {
    private final Set<String> tags = new HashSet<>();

    @Override
    public Set<String> getTags() {
        return tags;
    }
}
