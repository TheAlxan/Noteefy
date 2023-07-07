package publish.criteria

import event.Event
import notification.NotificationCenter
import notification.exception.CriteriaCheckNoteefyException

typealias RegistrationBlock = RegistrationCriteria.Companion.() -> RegistrationCriteria

class RegistrationCriteria private constructor(
    private val criteriaSet: List<Criteria>
) {
    private var next: RegistrationCriteria? = null

    fun applies(event: Event<*>): Boolean {
        if (criteriaSet.isEmpty()) return true
        var state = false
        try {
            state = criteriaSet.all { criteria -> criteria.applies(event) }
            if (!state) return false
            var current = next
            while (current != null) {
                state = next?.applies(event) ?: false
                if (!state) return false
                current = next?.next
            }
        } catch (e: Exception) {
            NotificationCenter.notify(CriteriaCheckNoteefyException(e))
        }
        return state
    }

    infix fun and(criteria: RegistrationCriteria): RegistrationCriteria {
        next = criteria
        return this
    }

    companion object {
        fun noCriteria(): RegistrationCriteria {
            return RegistrationCriteria(mutableListOf())
        }

        fun having(criteria: Criteria.Companion.() -> Criteria): RegistrationCriteria {
            return RegistrationCriteria(listOf(criteria(Criteria)))
        }
    }
}

class Criteria private constructor(
    private val block: (Event<*>) -> Boolean
) {

    private var next: Criteria? = null
    private var nextOp: ((Boolean, Boolean) -> Boolean)? = null
    fun applies(event: Event<*>): Boolean {
        var state = false
        try {
            state = block(event)
            var current = next
            while (current != null) {
                state = nextOp?.invoke(state,(next?.applies(event) ?: false)) ?: false
                current = next?.next
            }
        } catch (e: Exception) {
            NotificationCenter.notify(CriteriaCheckNoteefyException(e))
        }
        return state
    }

    infix fun or(criteria: Criteria): Criteria {
        next = criteria
        nextOp = { a, b -> a or b }
        return this
    }

    infix fun and(criteria: Criteria): Criteria {
        next = criteria
        nextOp = { a, b -> a and b }
        return this
    }

    companion object {
        fun <T: Any> check(clazz: Class<T>, block: (Event<T>) -> Boolean): Criteria {
            return instanceOf(clazz) and Criteria(block as (Event<*>) -> Boolean)
        }

        fun <T> instanceOf(clazz: Class<T>): Criteria {
            return Criteria { it.getClazz() == clazz }
        }

        fun withTag(tag: String): Criteria {
            return Criteria { event -> event.hasTag(tag) }
        }
    }
}