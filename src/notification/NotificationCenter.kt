package notification

import event.Event
import notification.exception.NoteefyException
import notification.exception.GeneralNoteefyException
import publish.member.Member
import publish.node.SimplePublisher
import subscribe.base.Subscriber

class NotificationCenter private constructor() : SimplePublisher() {

    private val subscribers: HashSet<Member> = hashSetOf()
    override fun getMembers() = subscribers

    companion object {
        private val instance = NotificationCenter()

        fun register(subscriber: Subscriber) {
            instance.register(subscriber)
        }

        private fun notify(event: Event<Notification<*>>) {
            instance.publish(event as Event<Any>)
        }

        fun notify(e: Exception) {
            notify(GeneralNoteefyException(e))
        }

        fun notify(e: NoteefyException) {
            val event = Event(Notification::class.java, ExceptionNotification(e))
            notify(event)
        }

        fun notify(message: String) {
            val event = Event(Notification::class.java, LogNotification(message))
            notify(event)
        }

    }
}