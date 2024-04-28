package com.alxan.noteefy.log;

import com.alxan.noteefy.TestHelper;
import com.alxan.noteefy.notification.Level;
import com.alxan.noteefy.notification.exception.NoteefyException;
import com.alxan.noteefy.subscribe.Subscriber;
import org.junit.jupiter.api.Test;

public class DefaultLoggerUnitTest extends TestHelper {
    @Test
    public void shouldLogWithLevel() throws InterruptedException {
        initialLatch(1);
        Subscriber subscriber = createMockSubscriber();

        Level logLevel = Level.DEBUG;
        countDownOnNotification(String.class);
        DefaultLogger logger = new DefaultLogger(subscriber);

        logger.log("SomeLog", logLevel);
        assertLatchCount(0);
    }

    @Test
    public void shouldErrorWithLevel() throws InterruptedException {
        initialLatch(1);
        Subscriber subscriber = createMockSubscriber();

        countDownOnException(NoteefyException.class);
        DefaultLogger logger = new DefaultLogger(subscriber);

        logger.log("SomeError", Level.ERROR);
        assertLatchCount(0);
    }

    @Test
    public void shouldLogError() throws InterruptedException {
        initialLatch(1);
        Logger logger = createTestLogger();

        logger.error("SomeLog");
        assertLatchCount(0);
    }

    @Test
    public void shouldLogDebug() throws InterruptedException {
        initialLatch(1);
        Logger logger = createTestLogger();

        logger.debug("SomeLog");
        assertLatchCount(0);
    }

    @Test
    public void shouldLogTrace() throws InterruptedException {
        initialLatch(1);
        Logger logger = createTestLogger();

        logger.trace("SomeLog");
        assertLatchCount(0);
    }

    @Test
    public void shouldLogWarn() throws InterruptedException {
        initialLatch(1);
        Logger logger = createTestLogger();

        logger.warn("SomeLog");
        assertLatchCount(0);
    }

    @Test
    public void shouldLogInfo() throws InterruptedException {
        initialLatch(1);
        Logger logger = createTestLogger();

        logger.info("SomeLog");
        assertLatchCount(0);
    }

    private Logger createTestLogger() {
        Subscriber subscriber = createMockSubscriber();

        countDownOnNotification(String.class);
        countDownOnException(Exception.class);
        return new DefaultLogger(subscriber);
    }
}
