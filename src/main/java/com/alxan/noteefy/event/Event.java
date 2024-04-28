package com.alxan.noteefy.event;

import com.alxan.noteefy.common.BaseIdentifiable;

import java.util.Set;

public class Event<T> extends BaseIdentifiable implements Taggable, Publishable, Comparable<Event<?>> {
    private final T content;
    private final Class<T> contentType;
    private final EventOrder eventOrder = new EventOrder();
    private final TagRegistry tagRegistry = new TagRegistry();

    public Event(T aContent) {
        contentType = (Class<T>) aContent.getClass(); // TODO Fix this
        content = aContent;
    }

    public static <T> Event<?> createEventIfNecessary(T content) {
        if (content instanceof Event) {
            return (Event<?>) content;
        } else {
            return new Event<T>(content);
        }
    }

    public T getContent() {
        return content;
    }

    public Class<T> getContentType() {
        return contentType;
    }

    public Long getOrder() {
        return eventOrder.getOrder();
    }

    @Override
    public int compareTo(Event<?> other) {
        return getOrder().compareTo(other.getOrder());
    }

    @Override
    public Set<String> getTags() {
        return tagRegistry.getTags();
    }
}
