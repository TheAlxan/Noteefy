import com.alxan.noteefy.common.worker.ExecutorsRegistry;
import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.publish.broker.Broker;
import com.alxan.noteefy.publish.broker.filter.EventFilterBuilder;
import com.alxan.noteefy.publish.broker.filter.EventFilters;
import com.alxan.noteefy.subscribe.Listener;

public class EventFiltersExample {
    public static void main(String[] args) throws InterruptedException {
        Broker broker = new Broker();
        String topic = "Integers";

        Listener evenNumberListener = createListener("EvenNumber");
        Listener evenTagListener = createListener("EvenTag");

        EventFilters evenNumberFilters = createEvenNumberFilters();
        EventFilters tagFilters = createEventTagFilters();

        broker.register(topic, evenNumberListener, evenNumberFilters);
        broker.register(topic, evenTagListener, tagFilters);

        for (int i = 0; i < 20; i++) {
            Event<Integer> event = new Event<>(i);
            if (i % 2 == 0) event.addTag("Even");
            else event.addTag("Odd");
            broker.publish(topic, event);
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

    private static EventFilters createEvenNumberFilters() {
        return EventFilterBuilder.create() // Filter for even numbers
                .customCondition(event -> {
                    Object content = event.getContent();
                    if (content instanceof Integer number) {
                        return number % 2 == 0;
                    }
                    return false;
                }).build();
    }

    private static EventFilters createEventTagFilters() {
        return EventFilterBuilder.create().hasTag("Even").build();
    }
}
