package subscribe.base

import event.EventHandler
import java.util.UUID

interface Subscriber : EventHandler<Any> {
    fun getId(): UUID
}