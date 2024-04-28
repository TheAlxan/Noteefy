package com.alxan.noteefy.publish.broker;

import com.alxan.noteefy.common.worker.Executor;
import com.alxan.noteefy.common.worker.ExecutorImp;
import com.alxan.noteefy.publish.broker.filter.EventFilters;
import com.alxan.noteefy.subscribe.Subscriber;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class BroadcasterRegistry {
    private final Map<String, TopicPublisher> topicToBroadcaster = new HashMap<>();
    private Executor executor = ExecutorImp.getDefaultExecutor();

    public TopicPublisher getTopicPublisher(String topic) {
        return topicToBroadcaster.get(topic);
    }

    public void register(String topic, Subscriber subscriber) {
        createBroadcasterIfNecessary(topic);
        topicToBroadcaster.get(topic).register(subscriber);
    }

    public void unregister(String topic, Subscriber subscriber) {
        if (topicDoesNotExist(topic)) return;
        topicToBroadcaster.get(topic).unregister(subscriber);
    }

    public void registerSubscriberEventFilters(String topic, Subscriber subscriber, EventFilters filters) {
        if (topicDoesNotExist(topic)) return;
        TopicPublisher broadcaster = topicToBroadcaster.get(topic);
        broadcaster.registerSubscriberEventFilters(subscriber, filters);
    }

    public void setExecutor(Executor anExecutor) {
        executor = anExecutor;
        Collection<TopicPublisher> broadcasters = topicToBroadcaster.values();
        for (TopicPublisher broadcaster : broadcasters) {
            broadcaster.setExecutor(executor);
        }
    }

    private void createBroadcasterIfNecessary(String topic) {
        if (topicExist(topic)) return;
        TopicPublisher broadcaster = new TopicPublisher();
        broadcaster.setExecutor(executor);
        topicToBroadcaster.put(topic, broadcaster);
    }

    private boolean topicExist(String topic) {
        return topicToBroadcaster.containsKey(topic);
    }

    private boolean topicDoesNotExist(String topic) {
        return !topicExist(topic);
    }
}
