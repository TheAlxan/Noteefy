package core

import java.util.UUID

class PublishTracker(private val capacity: Int) {
    private val set = LinkedHashSet<UUID>()

    fun published(eventId: UUID) {
        if (set.size >= capacity) {
            set.remove(set.iterator().next())
        }
        set.add(eventId)
    }

    fun isPublished(eventId: UUID) = set.contains(eventId)
}