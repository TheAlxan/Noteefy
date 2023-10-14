package core

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Dispatcher private constructor(threadsCount: Int) {
    private var executorService: ExecutorService? = null

    init {
        executorService = Executors.newFixedThreadPool(threadsCount)
    }

    fun submit(block: Work) {
        executorService?.submit {
            block()
        } ?: throw Exception("ExecutorService not initialized")
    }

    class OnGoingDispatcher {
        private var threadsCount: Int = 1
        fun build(): Dispatcher {
            return Dispatcher(threadsCount)
        }

        fun withThreadsCount(count: Int): OnGoingDispatcher {
            threadsCount = count
            return this
        }
    }

    companion object {
        private val defaultDispatcher = create().withThreadsCount(
            NoteefyConfig.DispatcherConfig.defaultThreadsCount
        ).build()
        fun create() = OnGoingDispatcher()
        fun getDefaultDispatcher() = defaultDispatcher

    }
}
