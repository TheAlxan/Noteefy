package com.alxan.noteefy.publish.broadcaster;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.subscribe.Subscriber;

public class Broadcaster extends BaseBroadcaster implements Subscriber {

    @Override
    public <T> void onEvent(Event<T> event) {
        publish(event);
    }
}
