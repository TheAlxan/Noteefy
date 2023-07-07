package publish.base

import publish.criteria.RegistrationCriteria
import subscribe.base.Subscriber

interface BrokingPublisher: Publisher {
    fun registerWithCriteria(criteria: RegistrationCriteria, vararg subscriber: Subscriber)
}