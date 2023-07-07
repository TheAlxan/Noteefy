package log

import org.apache.logging.log4j.Level

interface Logger {
    fun log(level: Level, message: String)
    fun info(message: String)
    fun trace(message: String)
    fun warn(message: String)
    fun debug(message: String)
    fun error(message: String)
}