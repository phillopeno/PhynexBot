package org.phynex.discord.routing;

import org.phynex.discord.routing.serializable.GuildEvent;

/**
 * Routes the GuildMessage or GuildReaction event to the correct place (Commands, Logging, Etc...)
 */
public class GuildRouting {

    private final GuildEvent guildEvent;


    public GuildRouting(GuildEvent guildEvent) {
        this.guildEvent = guildEvent;
    }

    public GuildEvent getGuildEvent() {
        return guildEvent;
    }
}
