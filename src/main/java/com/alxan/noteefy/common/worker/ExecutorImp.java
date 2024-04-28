package com.alxan.noteefy.common.worker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExecutorImp implements Executor {
    private static final ExecutorImp defaultExecutor = new ExecutorImp();
    private final ExecutorService executorService;

    public ExecutorImp() {
        this(10); // TODO should read from config
    }

    public ExecutorImp(int threadsCount) {
        executorService = Executors.newFixedThreadPool(threadsCount);
        ExecutorsRegistry.register(this);
    }

    public static ExecutorImp getDefaultExecutor() {
        return defaultExecutor;
    }

    @Override
    public void submitJob(Runnable job) {
        executorService.submit(job);
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
