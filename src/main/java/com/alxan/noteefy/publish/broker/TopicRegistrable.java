package com.alxan.noteefy.publish.broker;

import com.alxan.noteefy.subscribe.Subscriber;

interface TopicRegistrable {
    void register(String topic, Subscriber subscriber);

    void unregister(String topic, Subscriber subscriber);
}
