package notification

import event.Event
import notification.exception.NoteefyException
import org.apache.logging.log4j.Level

sealed class Notification<T : Any>(clazz: Class<T>, content: T) : Event<T>(clazz, content)
class ExceptionNotification(private val noteefyException: NoteefyException) :
    Notification<NoteefyException>(NoteefyException::class.java, noteefyException) {
    fun getNoteefyException(): NoteefyException {
        return noteefyException
    }

    fun getException(): Exception? {
        return noteefyException.exception
    }
}

class LogNotification(val log: String, val level: Level = Level.DEBUG) : Notification<String>(String::class.java, log) {
    override fun toString(): String {
        return log
    }
}

open class CustomNotification<T : Any>(clazz: Class<T>, content: T) : Notification<T>(clazz, content)