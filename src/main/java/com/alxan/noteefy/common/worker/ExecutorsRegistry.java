package com.alxan.noteefy.common.worker;

import java.util.HashSet;
import java.util.Set;

public class ExecutorsRegistry {
    private static Set<Executor> executors = new HashSet<>();

    public static void register(Executor executor) {
        executors.add(executor);
    }

    public static void shutdownAll() {
        for (Executor executor : executors) {
            executor.shutdown();
        }
    }
}
