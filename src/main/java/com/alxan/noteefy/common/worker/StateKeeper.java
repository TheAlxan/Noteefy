package com.alxan.noteefy.common.worker;

import java.util.concurrent.atomic.AtomicBoolean;

class StateKeeper {
    private final AtomicBoolean running = new AtomicBoolean(false);

    public synchronized boolean isProcessingStopped() {
        return !running.get();
    }

    public void stoppedProcessing() {
        setState(false);
    }

    public void startedProcessing() {
        setState(true);
    }

    private synchronized void setState(Boolean state) {
        running.set(state);
    }
}
