package com.alxan.noteefy.publish.broadcaster;

import com.alxan.noteefy.TestHelper;
import com.alxan.noteefy.common.AsyncChannel;
import com.alxan.noteefy.common.worker.Executor;
import com.alxan.noteefy.common.worker.ExecutorImp;
import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.notification.exception.NoSubscriberException;
import com.alxan.noteefy.notification.exception.NullEventException;
import com.alxan.noteefy.subscribe.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Set;
import java.util.UUID;

public class BaseBroadcasterUnitTest extends TestHelper {
    @Test
    public void shouldBeRegistrable() {
        BaseBroadcaster broadcaster = new BaseBroadcaster() {};
        Subscriber subscriber = Mockito.mock(AsyncChannel.class);
        broadcaster.register(subscriber);

        Assertions.assertTrue(broadcaster.hasSubscribers());
        Assertions.assertEquals(1, broadcaster.getSubscribersCount());
        Set<Subscriber> subscribers = broadcaster.getSubscribers();
        Assertions.assertTrue(subscribers.contains(subscriber));
    }

    @Test
    public void shouldBeUnRegistrable() {
        BaseBroadcaster broadcaster = new BaseBroadcaster() {};
        Subscriber subscriber = Mockito.mock(AsyncChannel.class);
        broadcaster.register(subscriber);
        Assertions.assertTrue(broadcaster.hasSubscribers());
        broadcaster.unregister(subscriber);
        Assertions.assertFalse(broadcaster.hasSubscribers());
    }

    @Test
    public void shouldTrackPublishedEvents() {
        UUID eventId1 = UUID.randomUUID();
        UUID eventId2 = UUID.randomUUID();
        BaseBroadcaster broadcaster = new BaseBroadcaster() {};
        broadcaster.published(eventId1);
        broadcaster.published(eventId2);
        Set<UUID> publishedEventsId = broadcaster.getPublishedEvents();
        Assertions.assertTrue(broadcaster.isPublished(eventId1));
        Assertions.assertTrue(broadcaster.isPublished(eventId2));
        Assertions.assertTrue(publishedEventsId.contains(eventId1));
        Assertions.assertTrue(publishedEventsId.contains(eventId2));
    }

    @Test
    public void shouldPublishEvent() throws InterruptedException {
        initialLatch(1);
        Subscriber subscriber = createMockSubscriber();
        countDownWhen(subscriber).onEvent(Mockito.any());
        BaseBroadcaster broadcaster = new BaseBroadcaster() {};
        broadcaster.register(subscriber);

        broadcaster.publish(256);
        assertLatchCount(0);
    }

    @Test
    public void shouldNotifyNoSubscriberException() throws InterruptedException {
        initialLatch(1);
        countDownOnException(NoSubscriberException.class);
        BaseBroadcaster broadcaster = new BaseBroadcaster() {};
        broadcaster.publish(256);
        assertLatchCount(0);
    }

    @Test
    public void shouldNotifyNullEventException() throws InterruptedException {
        initialLatch(2);
        countDownOnException(NullEventException.class);
        BaseBroadcaster broadcaster = new BaseBroadcaster() {};
        Event<?> event = null;
        broadcaster.publish(event);
        broadcaster.publish(null);
        assertLatchCount(0);
    }

    @Test
    public void shouldSetExecutor() throws InterruptedException {
        initialLatch(1);
        Executor executor = new ExecutorImp(1);
        BaseBroadcaster broadcaster = new BaseBroadcaster() {};
        broadcaster.setExecutor(executor);

        Subscriber subscriber = createMockSubscriber();
        countDownWhen(subscriber).onEvent(Mockito.any());
        broadcaster.register(subscriber);
        broadcaster.publish(256);
        assertLatchCount(0);
    }
}
