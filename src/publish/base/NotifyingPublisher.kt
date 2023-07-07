package publish.base

import notification.Notification
import notification.NotificationCenter
import notification.exception.NoSubscriberNoteefyException
import notification.exception.NoSubscriberProvidedException
import notification.exception.NoSuchTopicNoteefyException
import notification.exception.NoteefyException
import java.util.*

interface NotifyingPublisher {

    fun isLogEnabled(): Boolean
    fun notifyNoSubscriberException(publisherId: UUID, eventId: UUID) {
        notify(NoSubscriberNoteefyException(publisherId, eventId))
    }

    fun notifyNoSuchTopicException(topic: String, publisherId: UUID) {
        notify(NoSuchTopicNoteefyException(topic, publisherId))
    }

    fun notifyNoSubscriberProvidedException(publisherId: UUID) {
        notify(NoSubscriberProvidedException(publisherId))
    }

    private fun notify(exception: NoteefyException) {
        if (!isLogEnabled()) return
        NotificationCenter.notify(exception)
    }
}