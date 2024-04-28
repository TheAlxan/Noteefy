package com.alxan.noteefy.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class EventUnitTest {
    @Test
    public void shouldHaveTags() {
        Event<?> event = new Event<>(256);
        String tag1 = "Tag1";
        String tag2 = "Tag2";
        event.addTag(tag1);
        event.addTag(tag2);

        Set<String> tags = event.getTags();
        Assertions.assertTrue(tags.contains(tag1));
        Assertions.assertTrue(tags.contains(tag2));
    }

    @Test
    public void shouldBeComparableBasedOnOrder() {
        Event<?> event1 = new Event<>(256);
        Event<?> event2 = new Event<>(256);
        Event<?> event3 = new Event<>(256);

        Assertions.assertEquals(1, event2.compareTo(event1));
        Assertions.assertEquals(1, event3.compareTo(event2));
    }

    @Test
    public void shouldCreateEventIfContentIsNotEvent() {
        Event<?> event = new Event<>(256);
        Event<?> createdFromEvent = Event.createEventIfNecessary(event);
        Event<?> createdFromInteger = Event.createEventIfNecessary(256);
        Assertions.assertEquals(event, createdFromEvent);
        Assertions.assertNotEquals(event, createdFromInteger);
    }
}
