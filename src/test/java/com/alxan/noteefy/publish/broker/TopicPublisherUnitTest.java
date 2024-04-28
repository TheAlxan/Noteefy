package com.alxan.noteefy.publish.broker;

import com.alxan.noteefy.TestHelper;
import com.alxan.noteefy.publish.broker.filter.EventFilterBuilder;
import com.alxan.noteefy.publish.broker.filter.EventFilters;
import com.alxan.noteefy.subscribe.Subscriber;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TopicPublisherUnitTest extends TestHelper {
    @Test
    public void shouldPublishEvent() throws InterruptedException {
        initialLatch(1);

        TopicPublisher publisher = new TopicPublisher();
        Subscriber subscriber = createMockSubscriber();
        countDownWhen(subscriber).onEvent(Mockito.any());
        publisher.register(subscriber);

        publisher.publish(256);
        assertLatchCount(0);
    }

    @Test
    public void shouldRegisterSubscriberEventFilters() throws InterruptedException {
        initialLatch(2);

        TopicPublisher publisher = new TopicPublisher();
        Subscriber subscriber = createMockSubscriber();
        countDownWhen(subscriber).onEvent(Mockito.any());
        EventFilters eventFilters = EventFilterBuilder.create()
                .instanceOf(Integer.class)
                .build();
        publisher.register(subscriber);
        publisher.registerSubscriberEventFilters(subscriber, eventFilters);

        publisher.publish(256);
        publisher.publish("256");
        assertLatchCount(1);
    }
}
