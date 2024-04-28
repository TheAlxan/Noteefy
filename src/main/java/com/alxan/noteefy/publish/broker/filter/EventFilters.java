package com.alxan.noteefy.publish.broker.filter;

import com.alxan.noteefy.event.Event;

import java.util.ArrayList;

public class EventFilters {
    private final ArrayList<EventSingleFilter> filters = new ArrayList<>();

    protected EventFilters() {
    }

    public boolean filtersApplyToEvent(Event<?> event) {
        return filters.stream().allMatch(filter -> singleFilterAppliesToEvent(filter, event));
    }

    private boolean singleFilterAppliesToEvent(EventSingleFilter filter, Event<?> event) {
        return filter.applies(event);
    }

    protected void addFilter(EventSingleFilter aFilter) {
        filters.add(aFilter);
    }
}
