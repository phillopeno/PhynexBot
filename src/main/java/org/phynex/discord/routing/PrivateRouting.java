package org.phynex.discord.routing;

import org.phynex.discord.routing.serializable.PrivateMessageEvent;
import org.phynex.discord.routing.serializable.PrivateReactionEvent;

import java.util.Optional;

public class PrivateRouting {

    private final RoutingEvent routingEvent;

    private final PrivateMessageEvent privateMessageEvent;

    private final PrivateReactionEvent privateReactionEvent;

    public PrivateRouting(RoutingEvent routingEvent, PrivateMessageEvent privateMessageEvent) {
        this.routingEvent = routingEvent;
        this.privateMessageEvent = privateMessageEvent;
        privateReactionEvent = null;
    }

    public PrivateRouting(RoutingEvent routingEvent, PrivateReactionEvent privateReactionEvent) {
        this.routingEvent = routingEvent;
        this.privateReactionEvent = privateReactionEvent;
        privateMessageEvent = null;
    }

    public RoutingEvent getRoutingEvent() {
        return routingEvent;
    }

    public Optional<PrivateMessageEvent> getPrivateMessageEvent() {
        return Optional.ofNullable(privateMessageEvent);
    }

    public Optional<PrivateReactionEvent> getPrivateReactionEvent() {
        return Optional.ofNullable(privateReactionEvent);
    }
}
