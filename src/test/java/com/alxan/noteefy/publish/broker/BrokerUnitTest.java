package com.alxan.noteefy.publish.broker;

import com.alxan.noteefy.TestHelper;
import com.alxan.noteefy.common.worker.Executor;
import com.alxan.noteefy.notification.exception.NoSuchTopicException;
import com.alxan.noteefy.publish.Tracker;
import com.alxan.noteefy.publish.broker.filter.EventFilters;
import com.alxan.noteefy.subscribe.Subscriber;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class BrokerUnitTest extends TestHelper {
    @Test
    public void shouldReturnPublishedEvents() throws InterruptedException {
        initialLatch(1);

        Tracker tracker = Mockito.mock(Tracker.class);
        countDownWhen(tracker).getPublishedEvents();
        BroadcasterRegistry broadcasterRegistry = Mockito.mock(BroadcasterRegistry.class);
        Broker broker = new Broker(tracker, broadcasterRegistry);

        broker.getPublishedEvents();
        assertLatchCount(0);
    }

    @Test
    public void shouldCheckIfEventIsPublished() throws InterruptedException {
        initialLatch(1);

        Tracker tracker = Mockito.mock(Tracker.class);
        countDownWhen(tracker).isPublished(Mockito.any());
        BroadcasterRegistry broadcasterRegistry = Mockito.mock(BroadcasterRegistry.class);
        Broker broker = new Broker(tracker, broadcasterRegistry);

        UUID eventId = Mockito.mock(UUID.class);
        broker.isPublished(eventId);
        assertLatchCount(0);
    }

    @Test
    public void shouldMarEventIdAsPublished() throws InterruptedException {
        initialLatch(1);

        Tracker tracker = Mockito.mock(Tracker.class);
        countDownWhen(tracker).published(Mockito.any());
        BroadcasterRegistry broadcasterRegistry = Mockito.mock(BroadcasterRegistry.class);
        Broker broker = new Broker(tracker, broadcasterRegistry);

        UUID eventId = Mockito.mock(UUID.class);
        broker.published(eventId);
        assertLatchCount(0);
    }

    @Test
    public void shouldSetExecutor() throws InterruptedException {
        initialLatch(1);

        Tracker tracker = Mockito.mock(Tracker.class);
        BroadcasterRegistry broadcasterRegistry = Mockito.mock(BroadcasterRegistry.class);
        countDownWhen(broadcasterRegistry).setExecutor(Mockito.any());
        Broker broker = new Broker(tracker, broadcasterRegistry);

        Executor executor = Mockito.mock(Executor.class);
        broker.setExecutor(executor);
        assertLatchCount(0);
    }

    @Test
    public void shouldRegisterSubscriber() throws InterruptedException {
        initialLatch(1);

        Tracker tracker = Mockito.mock(Tracker.class);
        BroadcasterRegistry broadcasterRegistry = Mockito.mock(BroadcasterRegistry.class);
        countDownWhen(broadcasterRegistry).register(Mockito.anyString(), Mockito.any());
        Broker broker = new Broker(tracker, broadcasterRegistry);

        String topic = "SomeTopic";
        Subscriber subscriber = Mockito.mock(Subscriber.class);
        broker.register(topic, subscriber);
        assertLatchCount(0);
    }

    @Test
    public void shouldRegisterSubscriberWithFilters() throws InterruptedException {
        initialLatch(1);

        Tracker tracker = Mockito.mock(Tracker.class);
        BroadcasterRegistry broadcasterRegistry = Mockito.mock(BroadcasterRegistry.class);
        Mockito.doNothing().when(broadcasterRegistry).register(Mockito.anyString(), Mockito.any());
        countDownWhen(broadcasterRegistry).registerSubscriberEventFilters(Mockito.anyString(), Mockito.any(), Mockito.any());
        Broker broker = new Broker(tracker, broadcasterRegistry);

        String topic = "SomeTopic";
        Subscriber subscriber = Mockito.mock(Subscriber.class);
        EventFilters eventFilters = Mockito.mock(EventFilters.class);
        broker.register(topic, subscriber, eventFilters);
        assertLatchCount(0);
    }

    @Test
    public void shouldUnRegisterSubscriber() throws InterruptedException {
        initialLatch(1);

        Tracker tracker = Mockito.mock(Tracker.class);
        BroadcasterRegistry broadcasterRegistry = Mockito.mock(BroadcasterRegistry.class);
        countDownWhen(broadcasterRegistry).unregister(Mockito.anyString(), Mockito.any());
        Broker broker = new Broker(tracker, broadcasterRegistry);

        String topic = "SomeTopic";
        Subscriber subscriber = Mockito.mock(Subscriber.class);
        broker.unregister(topic, subscriber);
        assertLatchCount(0);
    }

    @Test
    public void shouldPublishEvent() throws InterruptedException {
        initialLatch(1);

        Tracker tracker = Mockito.mock(Tracker.class);
        BroadcasterRegistry broadcasterRegistry = Mockito.mock(BroadcasterRegistry.class);
        TopicPublisher topicPublisher = Mockito.mock(TopicPublisher.class);
        countDownWhen(topicPublisher).publish(Mockito.any());
        Mockito.when(broadcasterRegistry.getTopicPublisher(Mockito.anyString())).thenReturn(topicPublisher);
        Broker broker = new Broker(tracker, broadcasterRegistry);

        String topic = "SomeTopic";
        broker.publish(topic, 256);
        assertLatchCount(0);
    }

    @Test
    public void shouldNotifyNoSuchTopicException() throws InterruptedException {
        initialLatch(1);

        countDownOnException(NoSuchTopicException.class);

        Tracker tracker = Mockito.mock(Tracker.class);
        BroadcasterRegistry broadcasterRegistry = Mockito.mock(BroadcasterRegistry.class);
        Mockito.when(broadcasterRegistry.getTopicPublisher(Mockito.anyString())).thenReturn(null);
        Broker broker = new Broker(tracker, broadcasterRegistry);

        String topic = "SomeTopic";
        broker.publish(topic, 256);
        assertLatchCount(0);
    }
}
