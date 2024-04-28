package com.alxan.noteefy.publish.broker;

import com.alxan.noteefy.TestHelper;
import com.alxan.noteefy.common.AsyncChannel;
import com.alxan.noteefy.subscribe.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BroadcasterRegistryUnitTest extends TestHelper {
    @Test
    public void shouldRegisterSubscriber() {
        String topic = "SomeTopic";
        BroadcasterRegistry registry = new BroadcasterRegistry();
        Subscriber subscriber = createMockSubscriber();
        registry.register(topic, subscriber);

        TopicPublisher publisher = registry.getTopicPublisher(topic);
        Assertions.assertTrue(publisher.hasSubscribers());
    }

    @Test
    public void shouldUnRegisterSubscriber() {
        String topic = "SomeTopic";
        String otherTopic = "SomeOtherTopic";
        BroadcasterRegistry registry = new BroadcasterRegistry();
        Subscriber subscriber = Mockito.mock(AsyncChannel.class);
        registry.register(topic, subscriber);
        registry.unregister(topic, subscriber);
        registry.register(otherTopic, subscriber);

        TopicPublisher topicPublisher = registry.getTopicPublisher(topic);
        TopicPublisher otherTopicPublisher = registry.getTopicPublisher(otherTopic);
        Assertions.assertFalse(topicPublisher.hasSubscribers());
        Assertions.assertTrue(otherTopicPublisher.hasSubscribers());
    }
}
