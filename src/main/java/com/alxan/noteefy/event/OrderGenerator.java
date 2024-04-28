package com.alxan.noteefy.event;

import java.time.Instant;

class OrderGenerator {
    private static volatile Long latest = 0L;

    public static synchronized long getOrder() {
        Long order = Instant.now().toEpochMilli() * 1000;
        if (order <= latest) {
            latest = latest + (latest - order + 1);
        } else {
            latest = order;
        }
        return latest;
    }
}
