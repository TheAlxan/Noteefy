package core

import notification.NotificationCenter
import java.util.*
import java.util.concurrent.locks.StampedLock

typealias Work = () -> Unit

interface AsyncWorker {
    fun getJobQueue(): Queue<Work>
    fun getDispatcher(): Dispatcher

    fun getWorkerLock(): StampedLock
    fun submitJob(block: Work) {
        synchronized(getJobQueue()) {
            getJobQueue().add(block)
        }
    }

    fun attachDispatcher() {
        if (getWorkerLock().isWriteLocked) return
        val stamp = getWorkerLock().writeLock()
        getDispatcher().submit {
            try {
                getJobQueue().poll()?.invoke()
            } catch (e: Exception) {
                NotificationCenter.notify(e)
            } finally {
                getWorkerLock().unlockWrite(stamp)
                synchronized(getJobQueue()) {
                    if (getJobQueue().isNotEmpty()) attachDispatcher()
                }
            }
        }
    }
}