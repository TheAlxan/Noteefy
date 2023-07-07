package event

import publish.base.Publishable
import java.time.Instant
import java.util.*
import kotlin.collections.HashSet

open class Event<out T : Any>(
    private val clazz: Class<T>,
    private val content: T? = null,
    private val tags: HashSet<String> = hashSetOf()
) : Publishable, Comparable<Event<Any>> {

    private var uuid = UUID.randomUUID()
    private val order = Instant.now().toEpochMilli()
    override fun getId(): UUID = uuid
    override fun getClazz(): Class<*> = clazz
    override fun getOrder(): Long = order
    fun getContent(): T? = content
    fun getTags() = tags.toSet()
    fun hasTag(tag: String) = tags.contains(tag)
    fun addTag(tag: String) = tags.add(tag)
    override fun compareTo(other: Event<Any>): Int = order.compareTo(other.order)
    override fun toString(): String {
        return content?.toString() ?: "Event(${this::class.java.simpleName}, $uuid)"
    }

    companion object {
        fun <T : Any> from(content: T, tags: Set<String> = setOf()): Event<T> {
            return Event(content.javaClass, content, tags.toHashSet())
        }
    }
}