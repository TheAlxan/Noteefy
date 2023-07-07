package publish.node

import core.Dispatcher
import core.PublishTracker
import event.Event
import publish.base.NotifyingPublisher
import publish.base.Publisher
import publish.base.TrackingPublisher
import publish.member.Member
import subscribe.base.Subscriber
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

abstract class SimplePublisher(
    private val id: UUID = UUID.randomUUID(),
    private val criteriaCheck: Boolean = false,
    private val logEnabled: Boolean = false
): Publisher, NotifyingPublisher, TrackingPublisher {
    private var dispatcher: Dispatcher? = null
    private val publishedEvents = PublishTracker(100)
    private val publishQueue: Queue<Event<Any>> = LinkedBlockingQueue()

    override fun getPublishedEvents() = publishedEvents
    override fun isLogEnabled() = logEnabled
    abstract fun getMembers(): MutableCollection<Member>
    override fun publish(obj: Any, vararg tags: String) {
        val event = obj as? Event<*> ?: Event.from(obj, tags.toSet())
        publish(event)
    }

    fun publish(event: Event<Any>) {
        if (isPublished(event.getId())) return
        published(event.getId())
        publishQueue.add(event)
        if (getMembers().isEmpty()) {
            notifyNoSubscriberException(getId(), event.getId())
            return
        }
        getMembers().forEach { member ->
            passEvent(member, event)
        }
    }

    private fun passEvent(member: Member, event: Event<Any>) {
        try {
            if (criteriaCheck) {
                if (member.getCriteria().applies(event)) member.pushEvent(event)
            } else member.pushEvent(event)
        } catch (e: Exception) {
            throw e
        }
    }

    override fun register(vararg subscriber: Subscriber) {
        if (subscriber.isEmpty()) {
            notifyNoSubscriberProvidedException(getId())
            return
        }
        val members = getMembers()
        synchronized(members) {
            val newMembers = subscriber.map { subscriber -> Member.withNoCriteria(subscriber) }
            members.addAll(newMembers)
        }
    }

    override fun unregister(vararg subscriber: Subscriber) {
        val members = getMembers()
        synchronized(members) {
            members.removeAll(subscriber.map { sub -> Member.withNoCriteria(sub) }.toSet())
        }
    }

    override fun getDispatcher() = dispatcher
    override fun getId() = id
    fun setDispatcher(dispatcher: Dispatcher) {
        this.dispatcher = dispatcher
    }
}