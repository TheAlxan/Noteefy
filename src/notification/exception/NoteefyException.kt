package notification.exception

import java.util.*

sealed class NoteefyException(
    message: String, val exception: Exception?
) : Exception(message) {
    override fun toString(): String {
        return message ?: this.javaClass.simpleName
    }
}

class GeneralNoteefyException(exception: Exception?) :
    NoteefyException(exception?.message ?: "Event passing error.", exception)

class NoSubscriberNoteefyException(private val publisherId: UUID, private val eventId: UUID) :
    NoteefyException("No subscriber error [publisher id: $publisherId, event id: $eventId].", null) {
    fun getPublisherId() = publisherId
    fun getEventId() = eventId
}

class NoSuchTopicNoteefyException(private val topic: String, private val publisherId: UUID) :
    NoteefyException("No such topic error [topic: $topic, publisher id: $publisherId].", null) {

    fun getTopic() = topic
    fun getPublisherId() = publisherId
}

class NoSubscriberProvidedException(private val publisherId: UUID) :
    NoteefyException("No subscriber provided [publisher id: $publisherId].", null) {

    fun getPublisherId() = publisherId
}

class CriteriaCheckNoteefyException(exception: Exception) :
    NoteefyException("Criteria check error [${exception}].", exception)