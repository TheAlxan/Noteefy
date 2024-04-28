package com.alxan.noteefy.publish.broadcaster;

import com.alxan.noteefy.common.AsyncChannel;
import com.alxan.noteefy.publish.Registrable;
import com.alxan.noteefy.subscribe.Subscriber;

import java.util.HashSet;

public class SubscriberRegistry implements Registrable {

    private final HashSet<Subscriber> subscribers = new HashSet<>();

    @Override
    public void register(Subscriber subscriber) {
        if (subscriber instanceof AsyncChannel)
            subscribers.add(subscriber);
        else {
            AsyncChannel channel = new AsyncChannel(subscriber);
            subscribers.add(channel);
        }
    }

    @Override
    public void unregister(Subscriber subscriber) {
        subscribers.remove(subscriber);
    }

    public HashSet<Subscriber> getSubscribers() {
        return subscribers;
    }

    public int getSubscribersCount() {
        return subscribers.size();
    }
}
