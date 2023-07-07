package subscribe.node

import subscribe.base.SimpleSubscriber

open class Listener : SimpleSubscriber() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Listener) return false

        if (!getId().equals(other.getId())) return false

        return true
    }

    override fun hashCode(): Int {
        return getId().hashCode()
    }
}