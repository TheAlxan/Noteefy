package com.alxan.noteefy.publish.broker;

import com.alxan.noteefy.common.BaseIdentifiable;
import com.alxan.noteefy.common.SafeRunner;
import com.alxan.noteefy.common.worker.Executor;
import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.notification.exception.NoSuchTopicException;
import com.alxan.noteefy.notification.exception.NullEventException;
import com.alxan.noteefy.publish.PublishTracker;
import com.alxan.noteefy.publish.Tracker;
import com.alxan.noteefy.publish.broker.filter.EventFilters;
import com.alxan.noteefy.subscribe.Subscriber;

import java.util.Set;
import java.util.UUID;

public class Broker extends BaseIdentifiable implements BrokingPublisher {
    private final Tracker tracker;
    private final BroadcasterRegistry broadcasterRegistry;

    public Broker() {
        tracker = new PublishTracker(100);
        broadcasterRegistry = new BroadcasterRegistry();
    }

    public Broker(Tracker aTracker, BroadcasterRegistry aBroadcasterRegistry) {
        tracker = aTracker;
        broadcasterRegistry = aBroadcasterRegistry;
    }

    @Override
    public <T> void publish(String topic, Event<T> event) {
        SafeRunner.run(() -> safePublish(topic, event));
    }

    public <T> void publish(String topic, T content) {
        Event<?> event = Event.createEventIfNecessary(content);
        publish(topic, event);
    }

    @Override
    public void register(String topic, Subscriber subscriber) {
        broadcasterRegistry.register(topic, subscriber);
    }

    public void register(String topic, Subscriber subscriber, EventFilters filters) {
        broadcasterRegistry.register(topic, subscriber);
        broadcasterRegistry.registerSubscriberEventFilters(topic, subscriber, filters);
    }

    @Override
    public void unregister(String topic, Subscriber subscriber) {
        broadcasterRegistry.unregister(topic, subscriber);
    }

    public void setExecutor(Executor executor) {
        broadcasterRegistry.setExecutor(executor);
    }

    @Override
    public Set<UUID> getPublishedEvents() {
        return tracker.getPublishedEvents();
    }

    @Override
    public boolean isPublished(UUID uuid) {
        return tracker.isPublished(uuid);
    }

    @Override
    public void published(UUID uuid) {
        tracker.published(uuid);
    }

    private void safePublish(String topic, Event<?> event) throws NoSuchTopicException, NullEventException {
        errorIfEventIsNull(event);
        if (isPublished(event.getUUID()))
            return;
        TopicPublisher topicPublisher = getTopicPublisher(topic);
        topicPublisher.publish(event);
        published(event.getUUID());
    }

    private TopicPublisher getTopicPublisher(String topic) throws NoSuchTopicException {
        TopicPublisher topicPublisher = broadcasterRegistry.getTopicPublisher(topic);
        if (topicPublisher == null) {
            throw new NoSuchTopicException(getUUID(), topic);
        }
        return topicPublisher;
    }

    private void errorIfEventIsNull(Object event) throws NullEventException {
        if (event == null)
            throw new NullEventException(getUUID());
    }
}
