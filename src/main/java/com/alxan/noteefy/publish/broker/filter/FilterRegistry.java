package com.alxan.noteefy.publish.broker.filter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class FilterRegistry {
    private final Map<UUID, EventFilters> filtersMap = new HashMap<>();

    public void register(UUID uuid, EventFilters filters) {
        filtersMap.put(uuid, filters);
    }

    public EventFilters getFilters(UUID uuid) {
        return filtersMap.get(uuid);
    }

    public boolean hasFiltersRegistered(UUID uuid) {
        return filtersMap.containsKey(uuid);
    }

}
