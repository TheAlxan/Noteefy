package publish.node

import publish.member.Member
import subscribe.base.Subscriber

class Broadcaster private constructor(
    private val listeners: HashSet<Member> = hashSetOf(),
    logEnabled: Boolean = false,
    criteriaCheck: Boolean = false
) : SimpleMiddlePublisher(
    logEnabled = logEnabled, criteriaCheck = criteriaCheck
) {
    override fun getMembers(): MutableCollection<Member> = listeners

    companion object {
        fun from(vararg publisher: Subscriber): Broadcaster {
            return Broadcaster(
                listeners = publisher.map { member -> Member.withNoCriteria(member) }.toHashSet(),
                logEnabled = false,
            )
        }

        fun loggingFrom(vararg publisher: Subscriber): Broadcaster {
            return Broadcaster(
                listeners = publisher.map { member -> Member.withNoCriteria(member) }.toHashSet(),
                logEnabled = true
            )
        }
    }
}