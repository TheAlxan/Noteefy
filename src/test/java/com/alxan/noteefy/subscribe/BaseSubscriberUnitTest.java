package com.alxan.noteefy.subscribe;

import com.alxan.noteefy.TestHelper;
import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.notification.exception.NullEventException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

public class BaseSubscriberUnitTest extends TestHelper {
    @Test
    public void shouldRegisterEventHandler() throws InterruptedException {
        initialLatch(1);
        BaseSubscriber subscriber = new BaseSubscriber() {};
        subscriber.doOnEvent(Integer.class, event -> countDownLatch());

        Event<?> event = new Event<>(256);
        subscriber.onEvent(event);
        assertLatchCount(0);
    }

    @Test
    public void shouldNotifyNullEventException() throws InterruptedException {
        initialLatch(1);
        countDownOnException(NullEventException.class);

        BaseSubscriber subscriber = new BaseSubscriber() {};
        subscriber.onEvent(null);
        assertLatchCount(0);
    }

    @Test
    public void shouldNotifyHandlingException() throws InterruptedException {
        initialLatch(1);
        countDownOnException(Exception.class);

        BaseSubscriber subscriber = new BaseSubscriber() {};
        subscriber.doOnEvent(Integer.class, event -> {throw new IllegalStateException();});
        Event<Integer> event = new Event<>(256);
        subscriber.onEvent(event);
        assertLatchCount(0);
    }

    @Test
    public void shouldGenerateHashCode() {
        BaseSubscriber subscriber = new BaseSubscriber() {};
        UUID subscriberUuid = subscriber.getUUID();

        Assertions.assertEquals(subscriber.hashCode(), subscriberUuid.hashCode());
    }

    @Test
    public void shouldEqualBasedOnUUID() {
        BaseSubscriber subscriber = new BaseSubscriber() {};
        UUID subscriberUuid = subscriber.getUUID();
        Subscriber other = Mockito.mock(Subscriber.class);
        Mockito.when(other.getUUID()).thenReturn(subscriberUuid);

        Assertions.assertEquals(subscriber, other);
    }
}
