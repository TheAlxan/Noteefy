package com.alxan.noteefy.common.worker;

import java.util.concurrent.LinkedBlockingQueue;

public class AsyncWorkerImp implements AsyncWorker {
    private final LinkedBlockingQueue<Runnable> jobQueue = new LinkedBlockingQueue<>();
    private final StateKeeper stateKeeper = new StateKeeper();
    private Executor executor = ExecutorImp.getDefaultExecutor();

    @Override
    public Executor getExecutor() {
        return executor;
    }

    @Override
    public void setExecutor(Executor anExecutor) {
        executor = anExecutor;
    }

    public void appendJob(Runnable job) {
        Runnable continuousJob = createContinuousJob(job);
        jobQueue.add(continuousJob);
        startJobsIfNecessary();
    }

    private void startJobsIfNecessary() {
        if (stateKeeper.isProcessingStopped() && hasPendingJobs()) {
            startJobs();
        }
    }

    private Runnable createContinuousJob(Runnable currentJob) {
        return () -> {
            currentJob.run();
            attachNextJobOrStop();
        };
    }

    private void attachNextJobOrStop() {
        if (hasPendingJobs()) {
            getExecutor().submitJob(jobQueue.poll());
        } else {
            stateKeeper.stoppedProcessing();
        }
    }

    private void startJobs() {
        stateKeeper.startedProcessing();
        getExecutor().submitJob(jobQueue.poll());
    }

    private synchronized boolean hasPendingJobs() {
        return !jobQueue.isEmpty();
    }
}
