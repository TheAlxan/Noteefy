package com.alxan.noteefy.publish;

import com.alxan.noteefy.subscribe.Subscriber;

public interface Registrable {
    void register(Subscriber subscriber);

    void unregister(Subscriber subscriber);
}
