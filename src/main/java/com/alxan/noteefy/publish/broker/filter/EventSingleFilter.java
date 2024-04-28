package com.alxan.noteefy.publish.broker.filter;

import com.alxan.noteefy.event.Event;

import java.util.function.Function;

class EventSingleFilter {
    private Function<Event<?>, Boolean> condition;

    protected EventSingleFilter() {
    }

    protected void shouldComplyTo(Function<Event<?>, Boolean> aCondition) {
        condition = aCondition;
    }

    protected boolean applies(Event<?> event) {
        return condition.apply(event);
    }
}
