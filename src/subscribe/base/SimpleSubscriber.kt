package subscribe.base

import event.Event
import event.EventHandler
import java.util.*

abstract class SimpleSubscriber : Subscriber {

    private var eventHandlers: MutableMap<Class<out Any>, EventHandler<Any>> = mutableMapOf()
    private val id = UUID.randomUUID()

    override fun <T : Any> onEvent(event: Event<T>) {
        val handler = eventHandlers[event.getClazz()]
        handler?.onEvent(event)
    }

    override fun getId() = id

    @Suppress("UNCHECKED_CAST")
    open fun <T : Any> onEvent(clazz: Class<T>, block: EventHandler<T>) {
        eventHandlers[clazz] = block as? EventHandler<Any> ?: return
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SimpleSubscriber) return false

        if (!getId().equals(other.getId())) return false

        return true
    }

    override fun hashCode(): Int {
        return getId().hashCode()
    }
}