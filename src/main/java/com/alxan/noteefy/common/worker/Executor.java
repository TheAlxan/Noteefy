package com.alxan.noteefy.common.worker;

public interface Executor {
    public void submitJob(Runnable job);

    public void shutdown();
}
