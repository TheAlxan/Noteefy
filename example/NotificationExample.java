import com.alxan.noteefy.common.worker.ExecutorsRegistry;
import com.alxan.noteefy.notification.Level;
import com.alxan.noteefy.notification.Notification;
import com.alxan.noteefy.notification.NotificationCenter;
import com.alxan.noteefy.notification.NotificationListener;
import com.alxan.noteefy.publish.broadcaster.Broadcaster;
import com.alxan.noteefy.subscribe.Listener;

public class NotificationExample {
    public static void main(String[] args) throws InterruptedException {
        NotificationListener notificationListener = createNotificationListener();
        NotificationListener exceptionListener = createExceptionListener();

        NotificationCenter.registerListener(notificationListener);
        NotificationCenter.registerListener(exceptionListener);

        Listener listener = new Listener();
        listener.doOnEvent(String.class, event -> {throw new IllegalStateException();});
        listener.doOnEvent(Integer.class, event -> {
            Notification<String> notification = new Notification<>("SomeLog", Level.INFO);
            NotificationCenter.notify(notification);
        });

        Broadcaster broadcaster = new Broadcaster();
        broadcaster.register(listener);
        broadcaster.publish(256);
        broadcaster.publish("256");

        Thread.sleep(1000);
        ExecutorsRegistry.shutdownAll();
    }

    private static NotificationListener createNotificationListener() {
        NotificationListener notificationListener = new NotificationListener();
        notificationListener.doOnNotification(String.class, event -> {
            System.out.println("NotificationLevel: " + event.getLevel());
            System.out.println("Notification: " + event.getContent());
        });
        return notificationListener;
    }

    private static NotificationListener createExceptionListener() {
        NotificationListener notificationListener = new NotificationListener();
        notificationListener.doOnException(Exception.class, event -> {
            Exception exception = event.getContent();
            System.out.println("Exception: " + exception.getClass());
        });
        return notificationListener;
    }
}
