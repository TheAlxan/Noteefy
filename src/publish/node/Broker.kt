package publish.node

import publish.member.Member
import publish.criteria.RegistrationBlock
import publish.criteria.RegistrationCriteria
import subscribe.base.Subscriber

open class Broker(
    private val criteriaCheck: Boolean = false,
    logEnabled: Boolean = false
) : SimpleBroker(criteriaCheck = criteriaCheck, logEnabled = logEnabled) {
    private val topics: MutableMap<String, SimpleBroker> = mutableMapOf()
    override fun getMembers() = hashSetOf<Member>()

    fun register(topic: String, vararg subscriber: Subscriber, block: RegistrationBlock) {
        if (subscriber.isEmpty()) {
            notifyNoSubscriberProvidedException(getId())
            return
        }
        topics[topic]?.registerWithCriteria(block(RegistrationCriteria.Companion), *subscriber) ?: let {
            topics[topic] = internalInstance(criteriaCheck = criteriaCheck).apply {
                registerWithCriteria(block(RegistrationCriteria.Companion), *subscriber)
            }
        }
    }

    fun register(topic: String, vararg subscriber: Subscriber) {
        register(topic, *subscriber) {
            noCriteria()
        }
    }

    fun unregister(topic: String, vararg subscriber: Subscriber) {
        topics[topic]?.unregister(*subscriber)
    }

    override fun publish(obj: Any, vararg tags: String) {
        return
    }

    fun publish(topic: String, event: Any, vararg tags: String) {
        topics[topic]?.publish(event, *tags) ?: notifyNoSuchTopicException(topic, getId())
    }
}