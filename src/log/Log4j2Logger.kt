package log

import notification.ExceptionNotification
import notification.LogNotification
import notification.NotificationCenter
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import subscribe.node.NotificationListener

class Log4j2Logger: Logger {
    private val logger = LogManager.getLogger()

    init {
        NotificationListener(LogNotification::class.java).apply {
            onNotification { event ->
                val notification = event.getContent() as LogNotification
                log(notification.level, notification.log)
            }
            NotificationCenter.register(this)
        }

        NotificationListener(ExceptionNotification::class.java).apply {
            onNotification { event ->  error(event.getContent().toString()) }
            NotificationCenter.register(this)
        }
    }
    override fun log(level: Level, message: String) {
        logger.log(level, message)
    }

    override fun info(message: String) = logger.info(message)
    override fun warn(message: String) = logger.warn(message)
    override fun debug(message: String) = logger.debug(message)
    override fun trace(message: String) = logger.trace(message)
    override fun error(message: String) = logger.error(message)
}