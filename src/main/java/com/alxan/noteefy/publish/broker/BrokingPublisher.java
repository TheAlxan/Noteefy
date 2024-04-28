package com.alxan.noteefy.publish.broker;

import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.publish.Tracker;

interface BrokingPublisher extends TopicRegistrable, Tracker {
    <T> void publish(String topic, Event<T> event);
}
