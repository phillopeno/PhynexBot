package org.phynex.discord.events;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildMessageReactionHandler extends ListenerAdapter {

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {

    }

    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
        System.out.println("Removed.");
    }
}
