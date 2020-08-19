package org.phynex.discord.routing;

import org.phynex.discord.routing.serializable.PrivateMessageEvent;
import org.phynex.discord.routing.serializable.PrivateReactionEvent;

import java.util.Optional;

public class PrivateEvent {

    private final EventType eventType;

    private final PrivateMessageEvent privateMessageEvent;

    private final PrivateReactionEvent privateReactionEvent;

    public PrivateEvent(EventType eventType, PrivateMessageEvent privateMessageEvent) {
        this.eventType = eventType;
        this.privateMessageEvent = privateMessageEvent;
        privateReactionEvent = null;
    }

    public PrivateEvent(EventType eventType, PrivateReactionEvent privateReactionEvent) {
        this.eventType = eventType;
        this.privateReactionEvent = privateReactionEvent;
        privateMessageEvent = null;
    }

    public EventType getRoutingEvent() {
        return eventType;
    }

    public Optional<PrivateMessageEvent> getPrivateMessageEvent() {
        return Optional.ofNullable(privateMessageEvent);
    }

    public Optional<PrivateReactionEvent> getPrivateReactionEvent() {
        return Optional.ofNullable(privateReactionEvent);
    }
}
