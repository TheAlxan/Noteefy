package com.alxan.noteefy.publish.broker.filter;

import com.alxan.noteefy.event.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.function.Function;

public class EventSingleFilterUnitTest {
    @Test
    public void shouldCheckIfApplies() {
        EventSingleFilter singleFilter = new EventSingleFilter();
        Function<Event<?>, Boolean> function = (Event<?> event) -> true;
        singleFilter.shouldComplyTo(function);
        Event<?> event = Mockito.mock(Event.class);
        Assertions.assertTrue(singleFilter.applies(event));
    }
}
