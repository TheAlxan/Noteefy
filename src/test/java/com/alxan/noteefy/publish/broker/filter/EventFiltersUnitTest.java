package com.alxan.noteefy.publish.broker.filter;

import com.alxan.noteefy.event.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class EventFiltersUnitTest {
    @Test
    public void shouldCheckIfFiltersApply() {
        EventFilters filters = new EventFilters();
        EventSingleFilter singleFilter = Mockito.mock(EventSingleFilter.class);
        Mockito.when(singleFilter.applies(Mockito.any())).thenReturn(true);
        filters.addFilter(singleFilter);
        Event<?> event = Mockito.mock(Event.class);
        Assertions.assertTrue(filters.filtersApplyToEvent(event));
    }
}
