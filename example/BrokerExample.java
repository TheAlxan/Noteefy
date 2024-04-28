import com.alxan.noteefy.common.worker.ExecutorsRegistry;
import com.alxan.noteefy.publish.broker.Broker;
import com.alxan.noteefy.subscribe.Listener;

public class BrokerExample {
    public static void main(String[] args) throws InterruptedException {
        Broker broker = new Broker();
        String oddTopic = "Odds";
        String evenTopic = "Evens";

        Listener oddListener = createListener("Odd");
        Listener eventListener = createListener("Even");
        broker.register(oddTopic, oddListener);
        broker.register(evenTopic, eventListener);

        for (int i = 0; i < 20; i++) {
            if (i % 2 == 0)
                broker.publish(evenTopic, i);
            else
                broker.publish(oddTopic, i);
        }

        Thread.sleep(1000);
        ExecutorsRegistry.shutdownAll();
    }

    private static Listener createListener(String name) {
        Listener listener = new Listener();
        listener.doOnEvent(Integer.class, event -> {
            System.out.println(name + " listener got " + event.getContent());
        });
        return listener;
    }
}
