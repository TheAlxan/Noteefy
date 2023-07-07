package publish.node

import publish.base.BrokingPublisher
import publish.criteria.RegistrationCriteria
import publish.member.Member
import subscribe.base.Subscriber
import java.util.*

abstract class SimpleBroker(
    id: UUID = UUID.randomUUID(),
    criteriaCheck: Boolean = false,
    logEnabled: Boolean = false
) : BrokingPublisher, SimplePublisher(
    id = id, criteriaCheck = criteriaCheck, logEnabled = logEnabled
) {
    override fun registerWithCriteria(criteria: RegistrationCriteria, vararg subscriber: Subscriber) {
        val members = getMembers()
        synchronized(members) {
            val newMembers = subscriber.map { subscriber -> Member(criteria, subscriber) }
            members.addAll(newMembers)
        }
    }

    companion object {
        internal fun internalInstance(
            criteriaCheck: Boolean = false,
            vararg subscriber: Subscriber
        ): SimpleBroker {
            return object: SimpleBroker(criteriaCheck = criteriaCheck, logEnabled = true) {
                private val members = subscriber.map { member -> Member.withNoCriteria(member) }.toMutableSet()
                override fun getMembers() = members
            }
        }
    }
}