package org.phynex.discord.routing;

import org.phynex.discord.routing.serializable.GuildMessageEvent;
import org.phynex.discord.routing.serializable.GuildReactionEvent;

import java.util.Optional;

/**
 * Routes the GuildMessage or GuildReaction event to the correct place (Commands, Logging, Etc...)
 */
public class GuildRouting {

    private final RoutingEvent routingEvent;

    private final GuildMessageEvent guildMessageEvent;

    private final GuildReactionEvent guildReactionEvent;


    public GuildRouting(RoutingEvent routingEvent, GuildMessageEvent guildMessageEvent) {
        this.routingEvent = routingEvent;
        this.guildMessageEvent = guildMessageEvent;
        guildReactionEvent = null;
    }

    public GuildRouting(RoutingEvent routingEvent, GuildReactionEvent guildReactionEvent) {
        this.routingEvent = routingEvent;
        this.guildReactionEvent = guildReactionEvent;
        guildMessageEvent = null;
    }

    public GuildMessageEvent getGuildEvent() {
        return guildMessageEvent;
    }

    public RoutingEvent getRoutingEvent() {
        return routingEvent;
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
