package com.alxan.noteefy.common;

import com.alxan.noteefy.common.worker.AsyncWorkerImp;
import com.alxan.noteefy.common.worker.Executor;
import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.notification.exception.NullEventException;
import com.alxan.noteefy.subscribe.Listener;
import com.alxan.noteefy.subscribe.Subscriber;

import java.util.Objects;
import java.util.UUID;

public class AsyncChannel implements Subscriber {
    private final AsyncWorkerImp asyncWorkerImp = new AsyncWorkerImp();

    private final Subscriber subscriber;

    public AsyncChannel(Subscriber aSubscriber) {
        subscriber = aSubscriber;
        if (aSubscriber instanceof Listener listener)
            setExecutor(listener.getExecutor());
    }

    @Override
    public UUID getUUID() {
        return subscriber.getUUID();
    }

    @Override
    public <T> void onEvent(Event<T> event) {
        SafeRunner.run(() -> safeProcess(event));
    }

    public void setExecutor(Executor executor) {
        asyncWorkerImp.setExecutor(executor);
    }

    private void safeProcess(Event<?> event) throws NullEventException {
        errorIfEventIsNull(event);
        Runnable job = () -> SafeRunner.run(() -> subscriber.onEvent(event));
        asyncWorkerImp.appendJob(job);
    }

    private void errorIfEventIsNull(Object event) throws NullEventException {
        if (event == null)
            throw new NullEventException(getUUID());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof AsyncChannel other)
            return Objects.equals(getUUID(), other.getUUID());
        else if (o instanceof Subscriber other)
            return Objects.equals(getUUID(), other.getUUID());
        return false;
    }

    @Override
    public int hashCode() {
        return subscriber.hashCode();
    }
}
