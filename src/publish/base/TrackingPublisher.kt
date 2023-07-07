package publish.base

import core.PublishTracker
import java.util.*

interface TrackingPublisher {

    fun getPublishedEvents(): PublishTracker

    fun published(id: UUID) {
        getPublishedEvents().published(id)
    }

    fun isPublished(id: UUID) = getPublishedEvents().isPublished(id)
}