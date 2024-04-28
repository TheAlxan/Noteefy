package com.alxan.noteefy.publish.broker.filter;

import com.alxan.noteefy.event.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class FilterManagerUnitTest {
    @Test
    public void shouldRegisterFilters() {
        FilterManager filterManager = new FilterManager();
        UUID id = UUID.randomUUID();
        EventFilters filters = Mockito.mock(EventFilters.class);
        filterManager.register(id, filters);
        EventFilters fetchedFilters = filterManager.getEventFilters(id);
        Assertions.assertEquals(filters, fetchedFilters);
    }

    @Test
    public void shouldCheckIfFiltersApplyToEvent() {
        UUID applyingId = UUID.randomUUID();
        EventFilters applyingFilters = Mockito.mock(EventFilters.class);
        Mockito.when(applyingFilters.filtersApplyToEvent(Mockito.any())).thenReturn(true);

        UUID nonApplyingId = UUID.randomUUID();
        EventFilters nonApplyingFilters = Mockito.mock(EventFilters.class);
        Mockito.when(nonApplyingFilters.filtersApplyToEvent(Mockito.any())).thenReturn(false);

        FilterManager filterManager = new FilterManager();
        filterManager.register(applyingId, applyingFilters);
        filterManager.register(nonApplyingId, nonApplyingFilters);
        Set<UUID> ids = new HashSet<>();
        ids.add(applyingId);
        ids.add(nonApplyingId);
        Event<?> event = Mockito.mock(Event.class);
        Set<UUID> appliedIds = filterManager.getFilteredReceiversUuid(ids, event);
        Assertions.assertTrue(appliedIds.contains(applyingId));
        Assertions.assertFalse(appliedIds.contains(nonApplyingId));
    }
}
