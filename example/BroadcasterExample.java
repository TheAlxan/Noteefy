import com.alxan.noteefy.common.worker.ExecutorsRegistry;
import com.alxan.noteefy.publish.broadcaster.Broadcaster;
import com.alxan.noteefy.subscribe.Listener;

public class BroadcasterExample {
    public static void main(String[] args) throws InterruptedException {
        Broadcaster broadcaster = new Broadcaster();

        Listener listener1 = createListener("First");
        Listener listener2 = createListener("Second");
        Listener blockingListener = new Listener(); // This listener won't block the publishing process
        blockingListener.doOnEvent(Integer.class, event -> {
            try {
                Thread.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        broadcaster.register(listener1);
        broadcaster.register(listener2);
        broadcaster.register(blockingListener);

        for (int i = 0; i < 100; i++) {
            broadcaster.publish(i);
        }

        Thread.sleep(1000);
        ExecutorsRegistry.shutdownAll();
    }

    private static Listener createListener(String name) {
        Listener listener = new Listener();
        listener.doOnEvent(Integer.class, event -> {
            Integer content = event.getContent();
            System.out.println(name + " listener got " + content);
        });
        return listener;
    }
}
