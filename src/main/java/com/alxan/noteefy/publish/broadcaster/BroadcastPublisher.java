package com.alxan.noteefy.publish.broadcaster;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.publish.Publisher;
import com.alxan.noteefy.publish.Registrable;

interface BroadcastPublisher extends Publisher, Registrable {
    <T> void publish(Event<T> event);
}
