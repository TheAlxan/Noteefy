package com.alxan.noteefy.publish;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

public class PublishTrackerUnitTest {
    @Test
    public void shouldTrackPublishedEvents() {
        PublishTracker tracker = new PublishTracker(20);
        UUID eventId1 = UUID.randomUUID();
        UUID eventId2 = UUID.randomUUID();
        tracker.published(eventId1);
        tracker.published(eventId2);

        Set<UUID> publishedEventsId = tracker.getPublishedEvents();
        Assertions.assertEquals(2, publishedEventsId.size());
        Assertions.assertTrue(publishedEventsId.contains(eventId1));
        Assertions.assertTrue(publishedEventsId.contains(eventId2));
        Assertions.assertTrue(tracker.isPublished(eventId1));
        Assertions.assertTrue(tracker.isPublished(eventId2));
    }

    @Test
    public void shouldLimitCapacity() {
        PublishTracker tracker = new PublishTracker(20);
        UUID firstUuid = UUID.randomUUID();
        UUID lastUuid = UUID.randomUUID();

        tracker.published(firstUuid);
        for (int i = 0; i < 30; i++)
            tracker.published(UUID.randomUUID());
        tracker.published(lastUuid);

        Set<UUID> publishedEventsId = tracker.getPublishedEvents();
        Assertions.assertEquals(20, publishedEventsId.size());
        Assertions.assertFalse(tracker.isPublished(firstUuid));
        Assertions.assertTrue(tracker.isPublished(lastUuid));
    }
}
