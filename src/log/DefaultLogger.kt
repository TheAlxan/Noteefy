package log

import notification.NotificationCenter
import org.apache.logging.log4j.Level

class DefaultLogger: Logger {
    override fun log(level: Level, message: String) {
        NotificationCenter.notify(message)
    }

    override fun info(message: String) = log(Level.INFO, message)
    override fun warn(message: String) = log(Level.WARN, message)
    override fun debug(message: String) = log(Level.DEBUG, message)
    override fun trace(message: String) = log(Level.TRACE, message)
    override fun error(message: String) = log(Level.ERROR, message)
}