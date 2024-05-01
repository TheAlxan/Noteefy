# Noteefy: A Publisher-Subscriber Pattern Library

`Noteefy` is a robust publisher-subscriber pattern library designed to simplify event handling in Java applications.
With its focus on maintaining event order and supporting asynchronous publishing, Noteefy empowers developers to
create efficient and reliable event-driven systems.

## Table of Contents

- [Download](#download)
- [Milestones](#milestones)
- [Components](#components)
- [Usage](#usage)
- [Contributing](#contributing)
- [License](#license)

## Download

### Maven
```
<dependency>
    <groupId>io.github.thealxan</groupId>
    <artifactId>Noteefy</artifactId>
    <version>0.1</version>
</dependency>
```

### Gradle
```
implementation group: 'io.github.thealxan', name: 'Noteefy', version: '0.1'
```


## Milestones

- [ ] **Add Documentation**
- [ ] **Implement Configuration File**

## Components

`Noteefy` offers a range of components to facilitate seamless event management:

- **Broadcaster**: Publishes events to all registered subscribers.
- **Broker**: Publishes events on specific topics and enables subscribers to register to topics. It also supports event
  filtering based on subscriber preferences.
- **Listener**: Registers to publishers and defines event handlers for received events.
- **NotificationCenter**: Centralizes notifications, logs, and exceptions, distributing them to subscribers.
- **NotificationListener**: Subscribes to NotificationCenter for notifications, logs, and exceptions.
- **Executor**: An executor service that runs submitted jobs. Custom Executor implementations are supported, allowing
  users to tailor execution behavior as needed.
- **AsyncWorker**: Utilizes an Executor to execute assigned tasks asynchronously.

## Usage

**Check out the example package for some usage examples.**

### Listener

```java
Listener listener = new Listener();

listener.doOnEvent(Integer.class, event -> someActions());
listener.doOnEvent(String.class, new SomeEventHandler());
```

### Broadcaster

```java
Broadcaster broadcaster = new Broadcaster();
broadcaster.register(listener);

broadcaster.publish(256);
broadcaster.publish("256");
```

### Broker

```java
Broker broker = new Broker();
broker.register("Ints", listener);
broker.register("Strings", listener);

broker.publish("Ints", 256);
broker.publish("Strings", "256");
```

### Filtered Events

```java
EventFilters eventFilters = EventFilterBuilder.create()
        .instanceOf(Integer.class)
        .build();

Broker broker = new Broker();
broker.register("Objects", listener, eventFilters);

broker.publish("Objects", 256); // Will be received by listener
broker.publish("Objects", "256"); // Will not be received by listener
```

### Custom Publishers

Develop custom publishers by implementing the `Publisher` interface.

### Custom Subscribers

Create custom subscribers by implementing the `Subscriber` interface.

### Custom Executors

Implement custom executors by implementing the `Executor` interface.

## Contributing

We welcome contributions to `Noteefy`! Here's how you can contribute:

- Report bugs or suggest features by opening an issue.
- Submit pull requests to address issues or implement new features.
- Help improve documentation, code quality, and test coverage.

## License

`Noteefy` is licensed under the MIT License.