package org.phynex.discord.routing;

import org.phynex.discord.routing.serializable.GuildMessageEvent;
import org.phynex.discord.routing.serializable.GuildReactionEvent;

import java.util.Optional;

/**
 * Routes the GuildMessage or GuildReaction event to the correct place (Commands, Logging, Etc...)
 */
public class GuildEvent {

    private final RouteType routeType;

    private final GuildMessageEvent guildMessageEvent;

    private final GuildReactionEvent guildReactionEvent;


    public GuildEvent(RouteType routeType, GuildMessageEvent guildMessageEvent) {
        this.routeType = routeType;
        this.guildMessageEvent = guildMessageEvent;
        guildReactionEvent = null;
    }

    public GuildEvent(RouteType routeType, GuildReactionEvent guildReactionEvent) {
        this.routeType = routeType;
        this.guildReactionEvent = guildReactionEvent;
        guildMessageEvent = null;
    }

    public RouteType getRoutingEvent() {
        return routeType;
    }

    public void sendDefaultMessage(String message) {
        getGuildMessageEvent().ifPresent(e -> e.getMessage().getTextChannel().sendMessage(message).queue());
        getGuildReactionEvent().ifPresent(e -> e.getMessage().getTextChannel().sendMessage(message).queue());
    }

    /**
     *
     * @return
     *      Optional {@link GuildMessageEvent}
     */
    public Optional<GuildMessageEvent> getGuildMessageEvent() {
        return Optional.ofNullable(guildMessageEvent);
    }

    /**
     *
     * @return
     *      Optional {@link GuildReactionEvent}
     */
    public Optional<GuildReactionEvent> getGuildReactionEvent() {
        return Optional.ofNullable(guildReactionEvent);
    }
}
