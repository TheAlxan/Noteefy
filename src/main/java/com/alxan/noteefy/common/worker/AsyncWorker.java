package com.alxan.noteefy.common.worker;

public interface AsyncWorker {
    Executor getExecutor();

    void setExecutor(Executor executor);
}
