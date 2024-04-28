package com.alxan.noteefy.publish.broker.filter;

import com.alxan.noteefy.event.Event;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilterManager {
    private final FilterRegistry filterRegistry = new FilterRegistry();

    public void register(UUID uuid, EventFilters filters) {
        filterRegistry.register(uuid, filters);
    }

    public EventFilters getEventFilters(UUID uuid) {
        return filterRegistry.getFilters(uuid);
    }

    public <T> Set<UUID> getFilteredReceiversUuid(Set<UUID> uuids, Event<T> event) {
        Stream<UUID> validSubscriberStream = uuids.parallelStream()
                .filter(subscriber -> filtersApply(subscriber, event));
        return validSubscriberStream.collect(Collectors.toSet());
    }

    private boolean filtersApply(UUID uuid, Event<?> event) {
        return hasNoFiltersRegistered(uuid) || hasFiltersAndApplyToEvent(uuid, event);
    }

    private boolean hasNoFiltersRegistered(UUID uuid) {
        return !filterRegistry.hasFiltersRegistered(uuid);
    }

    private boolean hasFiltersAndApplyToEvent(UUID uuid, Event<?> event) {
        return filterRegistry.hasFiltersRegistered(uuid) && filtersApplyToEvent(uuid, event);
    }

    private boolean filtersApplyToEvent(UUID uuid, Event<?> event) {
        return getEventFilters(uuid).filtersApplyToEvent(event);
    }
}
