package publish.base

import java.util.*

interface Publishable {
    fun getId(): UUID
    fun getClazz(): Class<*>

    fun getOrder(): Long
}