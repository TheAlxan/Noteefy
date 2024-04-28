package com.alxan.noteefy.publish.broadcaster;

import com.alxan.noteefy.common.AsyncChannel;
import com.alxan.noteefy.subscribe.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;

public class SubscriberRegistryUnitTest {
    @Test
    public void shouldRegisterSubscriber() {
        SubscriberRegistry registry = new SubscriberRegistry();
        Subscriber subscriber = Mockito.mock(Subscriber.class);
        registry.register(subscriber);
        Assertions.assertEquals(1, registry.getSubscribersCount());
    }

    @Test
    public void shouldUnRegisterSubscriber() {
        SubscriberRegistry registry = new SubscriberRegistry();
        Subscriber subscriber = Mockito.mock(AsyncChannel.class);
        registry.register(subscriber);
        registry.unregister(subscriber);
        Assertions.assertEquals(0, registry.getSubscribersCount());
    }

    @Test
    public void shouldUnReturnSubscribers() {
        SubscriberRegistry registry = new SubscriberRegistry();
        Subscriber subscriber1 = Mockito.mock(Subscriber.class);
        Subscriber subscriber2 = Mockito.mock(Subscriber.class);
        registry.register(subscriber1);
        registry.register(subscriber2);
        HashSet<Subscriber> subscribers = registry.getSubscribers();
        Assertions.assertEquals(2, subscribers.size());
    }
}
