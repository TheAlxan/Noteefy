package com.alxan.noteefy.publish.broker.filter;

import com.alxan.noteefy.event.Event;

import java.util.function.Function;

public class EventFilterBuilder {
    private final EventFilters eventFilters = new EventFilters();

    private EventFilterBuilder() {
    }

    public static EventFilterBuilder create() {
        return new EventFilterBuilder();
    }

    public EventFilterBuilder instanceOf(Class<?> clazz) {
        EventSingleFilter classFilter = new EventSingleFilter();
        Function<Event<?>, Boolean> condition = event -> clazz.equals(event.getContentType());
        classFilter.shouldComplyTo(condition);
        eventFilters.addFilter(classFilter);
        return this;
    }

    public EventFilterBuilder hasTag(String tag) {
        EventSingleFilter tagFilter = new EventSingleFilter();
        Function<Event<?>, Boolean> condition = event -> event.hasTag(tag);
        tagFilter.shouldComplyTo(condition);
        eventFilters.addFilter(tagFilter);
        return this;
    }

    public EventFilterBuilder customCondition(Function<Event<?>, Boolean> condition) {
        EventSingleFilter customFilter = new EventSingleFilter();
        customFilter.shouldComplyTo(condition);
        eventFilters.addFilter(customFilter);
        return this;
    }

    public EventFilters build() {
        return eventFilters;
    }
}
