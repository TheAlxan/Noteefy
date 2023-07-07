package kotlinTest.base

import event.Event
import event.EventHandler
import notification.ExceptionNotification
import notification.LogNotification
import notification.NotificationCenter
import subscribe.node.Listener
import subscribe.node.NotificationListener
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

open class TestUtils {
    fun notifyLog(log: String, handler: (Event<*>) -> Unit) {
        val publishLatch = CountDownLatch(1)
        val listener = NotificationListener(LogNotification::class.java)
        listener.onNotification { message ->
            handler(message)
            publishLatch.countDown()
        }
        NotificationCenter.register(listener)
        runWaiting {
            NotificationCenter.notify(log)
            publishLatch.await(1, TimeUnit.SECONDS)
        }
    }

    fun notifyException(exception: Exception, handler: (Event<*>) -> Unit) {
        val publishLatch = CountDownLatch(1)
        val listener = NotificationListener(ExceptionNotification::class.java)
        listener.onNotification { message ->
            handler(message)
            publishLatch.countDown()
        }
        NotificationCenter.register(listener)
        runWaiting {
            NotificationCenter.notify(exception)
            publishLatch.await(1, TimeUnit.SECONDS)
        }
    }

    fun getDefaultListeners(count: Int, callback: (event: Event<*>) -> Unit): List<Listener> {
        val listeners = (1..count).map { Listener() }
        listeners.forEach { subscriber ->
            subscriber.onEvent(String::class.java, EventHandler.run(callback))
        }
        return listeners
    }

    fun runWaiting(block: () -> Unit) {
        val runnerThread = Thread {
            block()
        }
        runnerThread.start()
        runnerThread.join()
    }
}