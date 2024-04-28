package com.alxan.noteefy.publish.broadcaster;

import com.alxan.noteefy.TestHelper;
import com.alxan.noteefy.subscribe.Subscriber;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class BroadcasterUnitTest extends TestHelper {
    @Test
    public void shouldActAsMiddleHub() throws InterruptedException {
        initialLatch(1);

        Broadcaster broadcaster = new Broadcaster();
        Broadcaster hub = new Broadcaster();
        broadcaster.register(hub);
        Subscriber subscriber = createMockSubscriber();
        countDownWhen(subscriber).onEvent(Mockito.any());
        hub.register(subscriber);

        broadcaster.publish(256);
        assertLatchCount(0);
    }
}
