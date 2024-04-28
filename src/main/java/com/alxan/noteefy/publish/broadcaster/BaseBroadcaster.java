package com.alxan.noteefy.publish.broadcaster;

import com.alxan.noteefy.common.BaseIdentifiable;
import com.alxan.noteefy.common.SafeRunner;
import com.alxan.noteefy.common.worker.AsyncWorkerImp;
import com.alxan.noteefy.common.worker.Executor;
import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.notification.exception.NoSubscriberException;
import com.alxan.noteefy.notification.exception.NullEventException;
import com.alxan.noteefy.publish.PublishTracker;
import com.alxan.noteefy.subscribe.Subscriber;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class BaseBroadcaster extends BaseIdentifiable implements BroadcastPublisher {
    private final SubscriberRegistry subscriberRegistry = new SubscriberRegistry();
    private final PublishTracker tracker = new PublishTracker(100);
    private final AsyncWorkerImp asyncWorkerImp = new AsyncWorkerImp();

    @Override
    public <T> void publish(Event<T> event) {
        SafeRunner.run(() -> safePublish(event));
    }

    public <T> void publish(T content) {
        SafeRunner.run(() -> safePublish(content));
    }

    @Override
    public void register(Subscriber subscriber) {
        subscriberRegistry.register(subscriber);
    }

    @Override
    public void unregister(Subscriber subscriber) {
        subscriberRegistry.unregister(subscriber);
    }

    public void setExecutor(Executor executor) {
        asyncWorkerImp.setExecutor(executor);
    }

    public boolean hasSubscribers() {
        return getSubscribersCount() > 0;
    }

    public int getSubscribersCount() {
        return subscriberRegistry.getSubscribersCount();
    }

    protected <T> void passEventToSubscribers(Event<T> event, Set<Subscriber> subscribers) {
        for (Subscriber subscriber : subscribers) {
            passEventToSubscriber(subscriber, event);
        }
    }

    private <T> void passEventToSubscriber(Subscriber subscriber, Event<T> event) {
        Runnable job = () -> subscriber.onEvent(event);
        asyncWorkerImp.appendJob(job);
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

    protected HashSet<Subscriber> getSubscribers() {
        return subscriberRegistry.getSubscribers();
    }

    private void safePublish(Event<?> event) throws NullEventException, NoSubscriberException {
        errorIfEventIsNull(event);
        if (isPublished(event.getUUID()))
            return;
        errorIfNoSubscriber(event.getUUID());
        Set<Subscriber> subscribers = subscriberRegistry.getSubscribers();
        passEventToSubscribers(event, subscribers);
        published(event.getUUID());
    }

    private void safePublish(Object content) throws NullEventException, NoSubscriberException {
        errorIfEventIsNull(content);
        Event<?> event = Event.createEventIfNecessary(content);
        safePublish(event);
    }

    protected void errorIfEventIsNull(Object event) throws NullEventException {
        if (event == null)
            throw new NullEventException(getUUID());
    }

    protected void errorIfNoSubscriber(UUID eventId) throws NoSubscriberException {
        if (getSubscribers().isEmpty())
            throw new NoSubscriberException(getUUID(), eventId);
    }
}
