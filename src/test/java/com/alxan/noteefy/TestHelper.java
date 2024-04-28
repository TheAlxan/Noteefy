package com.alxan.noteefy;

import com.alxan.noteefy.notification.NotificationCenter;
import com.alxan.noteefy.notification.NotificationListener;
import com.alxan.noteefy.subscribe.Subscriber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestHelper {
    private final Set<NotificationListener> notificationListeners = new HashSet<>();
    private CountDownLatch latch;

    protected void initialLatch(int initialValue) {
        latch = new CountDownLatch(initialValue);
    }

    protected void assertLatchCount(int shouldRemain) throws InterruptedException {
        latch.await(1, TimeUnit.SECONDS);
        Assertions.assertEquals(shouldRemain, latch.getCount());
    }

    protected void countDownLatch() {
        latch.countDown();
    }

    protected <T> void countDownOnNotification(Class<T> notificationType) {
        NotificationListener notificationListener = new NotificationListener();
        notificationListener.doOnNotification(notificationType, event -> countDownLatch());
        NotificationCenter.registerListener(notificationListener);
    }

    protected <T extends Exception> void countDownOnException(Class<T> notificationType) {
        NotificationListener notificationListener = new NotificationListener();
        notificationListener.doOnException(notificationType, event -> countDownLatch());
        NotificationCenter.registerListener(notificationListener);
    }

    protected Subscriber createMockSubscriber() {
        UUID subscriberId = UUID.randomUUID();
        Subscriber subscriber = Mockito.mock(Subscriber.class);
        Mockito.when(subscriber.getUUID()).thenReturn(subscriberId);
        return subscriber;
    }

    protected <T> T countDownWhen(T mockObject) {
        return Mockito.doAnswer(i -> {
            countDownLatch();
            return null;
        }).when(mockObject);
    }

    @AfterEach
    private void cleanUpNotificationListeners() {
        for (NotificationListener listener : notificationListeners)
            NotificationCenter.unregisterListener(listener);
        notificationListeners.clear();
    }
}
