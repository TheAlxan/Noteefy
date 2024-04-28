package com.alxan.noteefy.publish.broker;

import com.alxan.noteefy.common.Identifiable;
import com.alxan.noteefy.common.SafeRunner;
import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.notification.exception.NoSubscriberException;
import com.alxan.noteefy.notification.exception.NullEventException;
import com.alxan.noteefy.publish.broadcaster.BaseBroadcaster;
import com.alxan.noteefy.publish.broker.filter.EventFilters;
import com.alxan.noteefy.publish.broker.filter.FilterManager;
import com.alxan.noteefy.subscribe.Subscriber;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class TopicPublisher extends BaseBroadcaster {
    private final FilterManager filterManager = new FilterManager();

    @Override
    public <T> void publish(Event<T> event) {
        SafeRunner.run(() -> safePublish(event));
    }

    @Override
    public <T> void publish(T content) {
        SafeRunner.run(() -> safePublish(content));
    }

    public void registerSubscriberEventFilters(Subscriber subscriber, EventFilters filters) {
        filterManager.register(subscriber.getUUID(), filters);
    }

    private void safePublish(Event<?> event) throws NoSubscriberException {
        errorIfNoSubscriber(event.getUUID());
        Set<Subscriber> filteredReceivers = getFilteredReceivers(event);
        passEventToSubscribers(event, filteredReceivers);
    }

    private void safePublish(Object content) throws NullEventException, NoSubscriberException {
        errorIfEventIsNull(content);
        Event<?> event = Event.createEventIfNecessary(content);
        safePublish(event);
    }

    private Set<Subscriber> getFilteredReceivers(Event<?> event) {
        Set<Subscriber> allSubscribers = getSubscribers();
        Set<UUID> allSubscribersUuids = getSubscribersUuids(allSubscribers);
        Set<UUID> filteredReceiversUuid = filterManager.getFilteredReceiversUuid(allSubscribersUuids, event);
        return filterReceivers(allSubscribers, filteredReceiversUuid);
    }

    private boolean subscriberFiltered(Subscriber subscriber, Set<UUID> filteredReceiversUuid) {
        return filteredReceiversUuid.contains(subscriber.getUUID());
    }

    private Set<Subscriber> filterReceivers(Set<Subscriber> allSubscribers, Set<UUID> validReceiversUuid) {
        Predicate<Subscriber> predicate = subscriber -> subscriberFiltered(subscriber, validReceiversUuid);
        Stream<Subscriber> receiversStream = allSubscribers.stream().filter(predicate);
        return receiversStream.collect(Collectors.toSet());
    }

    private Set<UUID> getSubscribersUuids(Set<Subscriber> subscribers) {
        Stream<UUID> allSubscribersUuidsStream = subscribers.stream().map(Identifiable::getUUID);
        return allSubscribersUuidsStream.collect(Collectors.toSet());
    }
}
