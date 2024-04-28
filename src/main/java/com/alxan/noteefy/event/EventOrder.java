package com.alxan.noteefy.event;

class EventOrder implements Ordered {
    private final Long order = OrderGenerator.getOrder();

    @Override
    public Long getOrder() {
        return order;
    }
}
