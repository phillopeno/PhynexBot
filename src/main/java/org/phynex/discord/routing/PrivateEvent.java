package org.phynex.discord.routing;

import net.dv8tion.jda.api.entities.PrivateChannel;
import org.phynex.discord.routing.serializable.PrivateMessageEvent;
import org.phynex.discord.routing.serializable.PrivateReactionEvent;

import java.util.Optional;

public class PrivateEvent {

    private final RouteType routeType;

    private final PrivateMessageEvent privateMessageEvent;

    private final PrivateReactionEvent privateReactionEvent;

    public PrivateEvent(RouteType routeType, PrivateMessageEvent privateMessageEvent) {
        this.routeType = routeType;
        this.privateMessageEvent = privateMessageEvent;
        privateReactionEvent = null;
    }

    public PrivateEvent(RouteType routeType, PrivateReactionEvent privateReactionEvent) {
        this.routeType = routeType;
        this.privateReactionEvent = privateReactionEvent;
        privateMessageEvent = null;
    }

    public void sendDefaultMessage(String message) {
        getPrivateMessageEvent().ifPresent(e -> e.getMessage().getPrivateChannel().sendMessage(message).queue());
        getPrivateReactionEvent().ifPresent(e -> e.getMessage().getPrivateChannel().sendMessage(message).queue());
    }

    public RouteType getRoutingEvent() {
        return routeType;
    }

    public Optional<PrivateMessageEvent> getPrivateMessageEvent() {
        return Optional.ofNullable(privateMessageEvent);
    }

    public Optional<PrivateReactionEvent> getPrivateReactionEvent() {
        return Optional.ofNullable(privateReactionEvent);
    }

    public PrivateMessageEvent getMessageEvent() {
        return privateMessageEvent;
    }

    public PrivateReactionEvent getReactionEvent() {
        return privateReactionEvent;
    }

    public PrivateChannel getChannel() {
        if (privateMessageEvent != null)
            return privateMessageEvent.getMessage().getPrivateChannel();
        else if (privateReactionEvent != null)
            return privateReactionEvent.getMessage().getPrivateChannel();
        return null;
    }
}
