package com.alxan.noteefy.subscribe;

import com.alxan.noteefy.common.BaseIdentifiable;
import com.alxan.noteefy.common.SafeRunner;
import com.alxan.noteefy.common.worker.Executor;
import com.alxan.noteefy.common.worker.ExecutorImp;
import com.alxan.noteefy.event.Event;
import com.alxan.noteefy.event.Handler;
import com.alxan.noteefy.notification.exception.NullEventException;

abstract class BaseSubscriber extends BaseIdentifiable implements Subscriber {
    private final HandlerRegistry handlers = new HandlerRegistry();
    private Executor executor = ExecutorImp.getDefaultExecutor();

    public <T> void doOnEvent(Class<T> clazz, Handler<? extends Event<T>> block) {
        handlers.register(clazz, block);
    }

    @Override
    public <T> void onEvent(Event<T> event) {
        SafeRunner.run(() -> safeProcess(event));
    }

    private <T> void safeProcess(Event<T> event) throws Exception {
        errorIfEventIsNull(event);
        Handler<Event<T>> handler = handlers.getHandler(event.getContentType());
        if (handler == null)
            return;
        handler.handle(event);
    }

    public Executor getExecutor() {
        return executor;
    }

    public void setExecutor(Executor anExecutor) {
        executor = anExecutor;
    }

    private void errorIfEventIsNull(Object event) throws NullEventException {
        if (event == null)
            throw new NullEventException(getUUID());
    }

    @Override
    public int hashCode() {
        return getUUID().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subscriber that)) return false;
        return getUUID().equals(that.getUUID());
    }
}
