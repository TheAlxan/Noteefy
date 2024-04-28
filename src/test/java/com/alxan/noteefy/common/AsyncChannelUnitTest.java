package com.alxan.noteefy.common;

import com.alxan.noteefy.TestHelper;
import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.subscribe.Subscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class AsyncChannelUnitTest extends TestHelper {
    @Test
    public void shouldPassEventToSubscriber() throws InterruptedException {
        initialLatch(1);

        Subscriber subscriber = createMockSubscriber();
        countDownWhen(subscriber).onEvent(Mockito.any());
        AsyncChannel asyncChannel = new AsyncChannel(subscriber);

        Event<?> event = Mockito.mock(Event.class);
        asyncChannel.onEvent(event);
        assertLatchCount(0);
    }

    @Test
    public void shouldEqualToSubscriberBasedOnUUID() {
        UUID subscriberId = UUID.randomUUID();
        Subscriber subscriber = Mockito.mock(Subscriber.class);
        Mockito.when(subscriber.getUUID()).thenReturn(subscriberId);
        AsyncChannel asyncChannel = new AsyncChannel(subscriber);

        Assertions.assertEquals(asyncChannel, subscriber);
    }

    @Test
    public void shouldEqualToAsyncChannelBasedOnUUID() {
        UUID subscriberId = UUID.randomUUID();
        Subscriber subscriber1 = Mockito.mock(Subscriber.class);
        Subscriber subscriber2 = Mockito.mock(Subscriber.class);
        Mockito.when(subscriber1.getUUID()).thenReturn(subscriberId);
        Mockito.when(subscriber2.getUUID()).thenReturn(subscriberId);
        AsyncChannel asyncChannel1 = new AsyncChannel(subscriber1);
        AsyncChannel asyncChannel2 = new AsyncChannel(subscriber2);

        Assertions.assertEquals(asyncChannel1, asyncChannel2);
    }
}
