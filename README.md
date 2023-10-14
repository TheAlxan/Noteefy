### ATTENTION
- **THIS IS A TEST PROJECT, NOT RECOMMENDED TO USE**

# Noteefy

Noteefy is a highly customizable Kotlin pub/sub library that maintains event order. It consists of two main components: "Broadcaster" and "Broker." The library enables asynchronous event publishing and offers a dispatcher component to control the number of threads used for event processing. Users can create custom dispatchers and attach them to components, providing flexibility and control over the event handling process.

## Features

- **Broadcaster:** Publishes events to all registered subscribers.
- **Broker:** Publishes events on specific topics and supports custom criteria for publishing set by subscribers during registration.
- **Dispatcher:** Controls the total number of threads used for event publishing and processing. Default dispatchers config can be set by `NoteefyConfig` file.
- **Customization:** All library components can be overridden or extended to suit your needs.
- **Notification Center:** Collects all notifications from the program, including error and log notifications.
- **Logger Interface:** Users can choose between the default logger, which logs to the notification center, or use the log4j2 logger for customized logging.

## Usage

### Subscribers

Subscribers are components that listen for and handle events. They can be created using the `Listener` class and registered with either the `Broadcaster` or the `Broker`. Users can also create their own custom subscribers by implementing the `Subscriber` interface. Here's an example of creating and registering a subscriber:

```kotlin
import subscribe.node.Listener
import event.Event
import event.EventHandler

// Create a Listener subscriber
val listener = Listener()

// Define event handling logic for the subscriber
listener.onEvent(String::class.java, EventHandler.run { event ->
    // Your event handling logic here
})

// Register the subscriber with a Broadcaster or Broker
val broadcaster = Broadcaster.from(listener)
val broker = Broker()
broker.register("topic", listener)
```

### Event Handlers

Event handlers are responsible for processing events when they are received by subscribers. You can define custom event handlers by implementing the `EventHandler` interface. Here's an example:

```kotlin
import event.EventHandler
import event.Event

class CustomEventHandler : EventHandler<String> {
    override fun onEvent(event: Event<String>) {
        val eventData = event.getContent()
        // Your custom event handling logic here
    }
}

// Create a custom event handler
val customHandler = CustomEventHandler()

// Register the event handler with a subscriber
listener.onEvent(String::class.java, customHandler)
```

### Registration Criteria

The `Broker` component allows you to set criteria for event publishing. You can specify conditions that an event must meet to be delivered to a subscriber. Here's an example of how to do it:

```kotlin
import publish.node.Broker
import event.Event

// Create a Broker with criteria check enabled
val broker = Broker(criteriaCheck = true)

// Register subscribers with criteria
val topic = "criteriaChecks"
val intListener = TypedSubscriber(Integer::class.java)

// Register a subscriber with criteria
broker.register(topic, intListener) {
    having { instanceOf(Integer::class.java) }
}

// Publish events that meet the criteria
broker.publish(topic, 42) // This event meets the criteria
broker.publish(topic, "Not an Integer") // This event won't be delivered to the subscriber
```

### Notification Listeners

Notification listeners can register with the `NotificationCenter` to receive and handle notifications. Notifications are a way to communicate important information, such as log messages or exceptions, within your application. Here's an example:

```kotlin
import notification.NotificationCenter
import notification.Notification
import event.Event

// Create a notification listener
val notificationListener = NotificationListener(LogNotification::class.java)

// Define notification handling logic for the listener
notificationListener.onNotification { notification: Event<*> ->
    val message = notification.getContent().toString()
    // Your notification handling logic here
}

// Register the notification listener
NotificationCenter.register(notificationListener)

// Notify a log message
val logMessage = "This is a log message."
val logNotification = Event(logMessage)
NotificationCenter.notify(logNotification)
```