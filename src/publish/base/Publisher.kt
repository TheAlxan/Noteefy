package publish.base

import core.Dispatcher
import subscribe.base.Subscriber
import java.util.UUID

interface Publisher {
    fun publish(obj: Any, vararg tags: String)
    fun register(vararg subscriber: Subscriber)
    fun unregister(vararg subscriber: Subscriber)
    fun getDispatcher(): Dispatcher?
    fun getId(): UUID
}