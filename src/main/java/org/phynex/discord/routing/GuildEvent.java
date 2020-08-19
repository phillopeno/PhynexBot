package org.phynex.discord.routing;

import org.phynex.discord.routing.serializable.GuildMessageEvent;
import org.phynex.discord.routing.serializable.GuildReactionEvent;

import java.util.Optional;

/**
 * Routes the GuildMessage or GuildReaction event to the correct place (Commands, Logging, Etc...)
 */
public class GuildEvent {

    private final EventType eventType;

    private final GuildMessageEvent guildMessageEvent;

    private final GuildReactionEvent guildReactionEvent;


    public GuildEvent(EventType eventType, GuildMessageEvent guildMessageEvent) {
        this.eventType = eventType;
        this.guildMessageEvent = guildMessageEvent;
        guildReactionEvent = null;
    }

    public GuildEvent(EventType eventType, GuildReactionEvent guildReactionEvent) {
        this.eventType = eventType;
        this.guildReactionEvent = guildReactionEvent;
        guildMessageEvent = null;
    }

    public EventType getRoutingEvent() {
        return eventType;
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
