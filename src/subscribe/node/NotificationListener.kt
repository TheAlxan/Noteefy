package subscribe.node

import event.Event
import notification.Notification
import subscribe.base.Subscriber
import java.util.UUID

typealias NotificationHandler = (Event<*>) -> Unit
class NotificationListener<T: Notification<*>>(private val clazz: Class<T>) : Subscriber {
    private var notificationHandler: NotificationHandler? = null
    private val id = UUID.randomUUID()

    fun onNotification(block: NotificationHandler) {
        notificationHandler = block
    }

    private fun onNotification(event: Event<*>) {
        notificationHandler?.invoke(event)
    }

    override fun <T: Any> onEvent(event: Event<T>) {
        if (event.getContent()?.javaClass != clazz)
            return
        onNotification(event)
    }

    override fun getId(): UUID = id
}