package com.alxan.noteefy.common;

import java.util.UUID;

public class BaseIdentifiable implements Identifiable {
    private final UUID uuid = UUID.randomUUID();

    @Override
    public UUID getUUID() {
        return uuid;
    }
}
