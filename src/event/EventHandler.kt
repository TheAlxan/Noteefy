package event

interface EventHandler<E: Any> {
    fun <T: E> onEvent(event: Event<T>)

    companion object {
        fun <E: Any> run(block: (Event<out E>) -> Unit): EventHandler<E> {
            return object : EventHandler<E> {
                override fun <T: E> onEvent(event: Event<T>) {
                    block.invoke(event)
                }
            }
        }
    }
}