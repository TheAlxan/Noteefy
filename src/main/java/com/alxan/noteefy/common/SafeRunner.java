package com.alxan.noteefy.common;

import com.alxan.noteefy.notification.NotificationCenter;

@FunctionalInterface
public interface SafeRunner {
    static void run(SafeRunner runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            NotificationCenter.error(e);
        }
    }

    void run() throws Exception;
}
