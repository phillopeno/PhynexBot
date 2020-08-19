package org.phynex.discord.listener;

import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageEmbedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.phynex.discord.routing.EventRouter;
import org.phynex.discord.routing.EventType;
import org.phynex.discord.routing.GuildEvent;
import org.phynex.discord.routing.serializable.GuildMessageEvent;

public class GuildMessageListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        GuildMessageEvent messageEvent = new GuildMessageEvent(
                event.getAuthor(), event.getMember(), event.getMessage());
        GuildEvent guildEvent = new GuildEvent(EventType.MESSAGE, messageEvent);

        EventRouter.route(guildEvent);
    }

    /**
     * Unhandled Events.
     */

    @Override
    public void onGuildMessageDelete(@NotNull GuildMessageDeleteEvent event) {
        super.onGuildMessageDelete(event);
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event) {
        super.onGuildMessageUpdate(event);
    }

    @Override
    public void onGuildMessageEmbed(@NotNull GuildMessageEmbedEvent event) {
        super.onGuildMessageEmbed(event);
    }
}
