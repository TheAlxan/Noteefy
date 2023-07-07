package publish.node

import event.Event
import subscribe.base.Subscriber


abstract class SimpleMiddlePublisher(
    logEnabled: Boolean = false, criteriaCheck: Boolean = false
) : Subscriber, SimplePublisher(
    logEnabled = logEnabled, criteriaCheck = criteriaCheck
) {

    override fun <T: Any> onEvent(event: Event<T>) {
        publish(event)
    }
}