package com.alxan.noteefy.publish.broker.filter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class FilterRegistryUnitTest {
    @Test
    public void shouldRegisterFilter() {
        FilterRegistry registry = new FilterRegistry();
        UUID id = UUID.randomUUID();
        EventFilters filters = Mockito.mock(EventFilters.class);
        registry.register(id, filters);

        EventFilters fetchedFilters = registry.getFilters(id);
        Assertions.assertEquals(filters, fetchedFilters);
        Assertions.assertTrue(registry.hasFiltersRegistered(id));
    }
}
