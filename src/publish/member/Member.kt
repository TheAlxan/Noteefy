package publish.member

import core.AsyncWorker
import core.Dispatcher
import core.Work
import event.Event
import publish.criteria.RegistrationCriteria
import subscribe.base.Subscriber
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.locks.StampedLock

class Member(
    private val criteria: RegistrationCriteria,
    private val subscriber: Subscriber,
): AsyncWorker {

    private val jobQueue = LinkedBlockingQueue<Work>()
    private val lock = StampedLock()

    override fun getJobQueue() = jobQueue
    override fun getWorkerLock() = lock
    override fun getDispatcher(): Dispatcher = Dispatcher.getDefaultDispatcher()
    fun getCriteria() = criteria
    fun getSubscriber() = subscriber

    fun pushEvent(event: Event<Any>) {
        submitJob { makeJob(event) }
        attachDispatcher()
    }

    private fun makeJob(event: Event<Any>) {
        getSubscriber().onEvent(event)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Member) return false
        return subscriber == other.subscriber
    }

    override fun hashCode(): Int {
        return subscriber.getId().hashCode()
    }
    companion object {
        fun withNoCriteria(subscriber: Subscriber): Member {
            return Member(
                criteria = RegistrationCriteria.noCriteria(), subscriber = subscriber
            )
        }
    }
}