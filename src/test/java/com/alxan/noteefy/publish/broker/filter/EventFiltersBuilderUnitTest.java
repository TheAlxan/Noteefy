package com.alxan.noteefy.publish.broker.filter;

import com.alxan.noteefy.event.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EventFiltersBuilderUnitTest {
    @Test
    public void shouldBuildWithInstanceOfFilter() {
        EventFilters eventFilters = EventFilterBuilder.create()
                .instanceOf(Integer.class)
                .build();

        Event<Integer> integerEvent = new Event<>(256);
        Event<String> stringEvent = new Event<>("256");

        Assertions.assertTrue(eventFilters.filtersApplyToEvent(integerEvent));
        Assertions.assertFalse(eventFilters.filtersApplyToEvent(stringEvent));
    }

    @Test
    public void shouldBuildWithHasTagFilter() {
        String tag = "SomeTag";
        EventFilters eventFilters = EventFilterBuilder.create()
                .hasTag(tag)
                .build();

        Event<Integer> noTagEvent = new Event<>(256);
        Event<Integer> taggedEvent = new Event<>(256);
        taggedEvent.addTag(tag);

        Assertions.assertTrue(eventFilters.filtersApplyToEvent(taggedEvent));
        Assertions.assertFalse(eventFilters.filtersApplyToEvent(noTagEvent));
    }

    @Test
    public void shouldBuildWithCustomConditionFilter() {
        EventFilters eventFilters = EventFilterBuilder.create()
                .customCondition(event -> {
                    if (event.getContent() instanceof Integer num) {
                        return num % 2 == 0;
                    } else
                        return false;
                })
                .build();

        Event<Integer> eventNumberEvent = new Event<>(256);
        Event<Integer> oddNumberEvent = new Event<>(257);
        Event<String> stringEvent = new Event<>("256");

        Assertions.assertTrue(eventFilters.filtersApplyToEvent(eventNumberEvent));
        Assertions.assertFalse(eventFilters.filtersApplyToEvent(oddNumberEvent));
        Assertions.assertFalse(eventFilters.filtersApplyToEvent(stringEvent));
    }
}
