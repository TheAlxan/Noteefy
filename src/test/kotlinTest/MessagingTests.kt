package kotlinTest

import kotlinTest.base.TestUtils
import event.Event
import event.EventHandler
import notification.ExceptionNotification
import notification.Notification
import notification.NotificationCenter
import notification.exception.NoSubscriberNoteefyException
import publish.node.Broadcaster
import publish.node.Broker
import subscribe.base.Subscriber
import subscribe.node.Listener
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import subscribe.node.NotificationListener
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

class MessagingTests : TestUtils() {

    @Test
    fun `should broadcast an event`() {
        val message = "Publishing event."
        val receiveCounter = AtomicInteger()
        val listenersCount = 2
        val publishLatch = CountDownLatch(listenersCount)
        val listeners = getDefaultListeners(listenersCount) { event ->
            Assertions.assertEquals(event.getContent(), message)
            receiveCounter.addAndGet(1)
            publishLatch.countDown()
        }
        val broadcaster = Broadcaster.from(*listeners.toTypedArray())
        runWaiting {
            broadcaster.publish(message)
            publishLatch.await(1, TimeUnit.SECONDS)
        }
        Assertions.assertEquals(listenersCount, receiveCounter.get())
    }

    @Test
    fun `should publish an event on a topic`() {
        val message = "Publishing event."
        val topic = "default"
        val receiveCounter = AtomicInteger()
        val listenersCount = 2
        val publishLatch = CountDownLatch(listenersCount)
        val listeners = getDefaultListeners(listenersCount) { event ->
            Assertions.assertEquals(event.getContent(), message)
            receiveCounter.addAndGet(1)
            publishLatch.countDown()
        }
        val broker = Broker()
        broker.register(topic, *listeners.toTypedArray())
        runWaiting {
            broker.publish(topic = topic, event = message)
            publishLatch.await(1, TimeUnit.SECONDS)
        }
        Assertions.assertEquals(listenersCount, receiveCounter.get())
    }

    @Test
    fun `should notify the notification center`() {
        val logNotification = "This is a log message."
        val exceptionNotification = IllegalStateException()
        val receiveCounter = AtomicInteger()

        notifyLog(logNotification) { notification: Event<*> ->
            Assertions.assertEquals(logNotification, notification.getContent().toString())
            receiveCounter.addAndGet(1)
        }
        notifyException(exceptionNotification) { notification: Event<*> ->
            Assertions.assertEquals(notification.getContent()?.javaClass, ExceptionNotification::class.java)
            val exception = (notification.getContent() as ExceptionNotification).getException()
            Assertions.assertEquals(exceptionNotification.javaClass, exception?.javaClass)
            receiveCounter.addAndGet(1)
        }

        Assertions.assertEquals(2, receiveCounter.get())
    }

    @Test
    fun `listeners should not block publishing process`() {
        val blockingListener = Listener()
        val listener1 = Listener()
        val listener2 = Listener()

        val listenersCount = 2
        val eventsCount = 100
        val publishLatch = CountDownLatch(listenersCount * eventsCount)
        val receivedCount = AtomicInteger()

        blockingListener.onEvent(Integer::class.java, EventHandler.run { event ->
            Thread.sleep(Long.MAX_VALUE)
            throw java.lang.IllegalStateException()
        })

        listOf(listener1, listener2).forEach { listener ->
            listener.onEvent(Integer::class.java, EventHandler.run { event ->
                receivedCount.addAndGet(1)
                publishLatch.countDown()
            })
        }

        val broadcaster = Broadcaster.from(blockingListener, listener1, listener2)
        val broker = Broker()

        val topic = "blockTest"
        broker.register(topic, blockingListener, broadcaster)

        for (i in 0 until eventsCount) {
            broker.publish(topic, i)
        }

        publishLatch.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals(listenersCount * eventsCount, receivedCount.get())
    }

    @Test
    fun `publisher should publish events in received order`() {
        val listenersCount = 3
        val eventsCount = 1000
        val publishLatch = CountDownLatch(listenersCount * eventsCount)
        val passCount = AtomicInteger(0)

        class OrderListener: Subscriber {
            private var previousValue = -1
            private val lock = Any()

            override fun getId(): UUID = UUID.randomUUID()

            override fun <T : Any> onEvent(event: Event<T>) {
                Assertions.assertEquals(Integer::class.java, event.getClazz())
                val currentValue = event.getContent() as? Int ?: throw java.lang.IllegalStateException()
                synchronized(lock) {
                    Assertions.assertEquals(1, currentValue - previousValue)
                    previousValue = currentValue
                    passCount.addAndGet(1)
                    publishLatch.countDown()
                }
            }
        }

        val listeners = (1..listenersCount).map { OrderListener() }
        val broadcaster = Broadcaster.from(*listeners.toTypedArray())

        for (i in 0 until eventsCount)
            broadcaster.publish(i)

        publishLatch.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals(eventsCount * listenersCount, passCount.get())
    }

    @Test
    fun `broker should check registration criteria`(){
        // (10 intListener) + (5 oddListener) + (5 evenListener) + (2 stringListener) + (1 TagListener)
        val eventsCount = 23
        val publishLatch = CountDownLatch(eventsCount)
        val passCount = AtomicInteger(0)

        class TypedSubscriber<E: Any>(
            private val clazz: Class<E>,
            private val handler: EventHandler<E>? = null
        ): Subscriber {
            override fun getId(): UUID = UUID.randomUUID()

            override fun <T : Any> onEvent(event: Event<T>) {
                Assertions.assertEquals(clazz, event.getClazz())
                val castedEvent = event as? Event<E> ?: return
                handler?.onEvent(castedEvent) ?: let {
                    passCount.addAndGet(1)
                    publishLatch.countDown()
                }
            }
        }

        val intListener = TypedSubscriber(Integer::class.java)
        val stringListener = TypedSubscriber(String::class.java)

        val oddListener = TypedSubscriber(Integer::class.java, EventHandler.run { event ->
            val value = event.getContent() ?: throw java.lang.IllegalStateException()
            Assertions.assertEquals(1, value.toInt() % 2)
            passCount.addAndGet(1)
            publishLatch.countDown()
        })
        val evenListener = TypedSubscriber(Integer::class.java, EventHandler.run { event ->
            val value = event.getContent() ?: throw java.lang.IllegalStateException()
            Assertions.assertEquals(0, value.toInt() % 2)
            passCount.addAndGet(1)
            publishLatch.countDown()
        })

        val tag = "taggedEvent"
        val tagListener = TypedSubscriber(String::class.java, EventHandler.run { event ->
            Assertions.assertEquals(tag, event.getTags().first())
            passCount.addAndGet(1)
            publishLatch.countDown()
        })

        val broker = Broker(criteriaCheck = true)
        val topic = "criteriaChecks"
        broker.register(topic, intListener) {
            having { instanceOf(Integer::class.java) }
        }
        broker.register(topic, stringListener) {
            having { instanceOf(String::class.java) }
        }
        broker.register(topic, oddListener) {
            having { check(Integer::class.java) { c -> c.getContent()?.toInt()?.mod(2) == 1 } }
        }
        broker.register(topic, evenListener) {
            having { check(Integer::class.java) { c -> c.getContent()?.toInt()?.mod(2) == 0 } }
        }
        broker.register(topic, tagListener) {
            having { withTag(tag) and instanceOf(String::class.java) }
        }

        for (i in 1..10)
            broker.publish(topic, i)
        broker.publish(topic, event = "Some string")
        broker.publish(topic, event = "Tag checker", tag)

        publishLatch.await(1, TimeUnit.SECONDS)
        Assertions.assertEquals(eventsCount, passCount.get())
    }

    @Test
    fun `should be able to unregister subscribers`() {
        val receiveCounter = AtomicInteger()
        val listenersCount = 2
        val publishLatch = CountDownLatch(4)
        val listeners = getDefaultListeners(listenersCount) { event ->
            receiveCounter.addAndGet(1)
            publishLatch.countDown()
        }
        NotificationListener(ExceptionNotification::class.java).apply {
            onNotification { event ->
                val content = event.getContent()
                if (content !is ExceptionNotification) return@onNotification
                if (content.getNoteefyException() !is NoSubscriberNoteefyException) return@onNotification
                publishLatch.countDown()
            }
            NotificationCenter.register(this)
        }
        val broadcaster = Broadcaster.loggingFrom(*listeners.toTypedArray())
        runWaiting {
            broadcaster.publish("Unregistered 0 subscribers")
            broadcaster.unregister(listeners.first())
            broadcaster.publish("Unregistered 1 subscribers")
            broadcaster.unregister(*listeners.toTypedArray())
            broadcaster.publish("Unregistered all subscribers")
            publishLatch.await(1, TimeUnit.SECONDS)
        }
        Assertions.assertEquals(3, receiveCounter.get())
        Assertions.assertEquals(0, publishLatch.count)
    }
}