package com.alxan.noteefy.subscribe;

import com.alxan.noteefy.common.Identifiable;
import com.alxan.noteefy.event.Event;

public interface Subscriber extends Identifiable {
    <T> void onEvent(Event<T> event);
}
