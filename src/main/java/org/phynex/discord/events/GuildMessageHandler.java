package org.phynex.discord.events;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.function.Function;

public class GuildMessageHandler extends ListenerAdapter {

    /**
     * the event author's tag code as a string.
     */
    public static Function<GuildMessageReceivedEvent, String>
            atAuthor = event -> String.format("<@%s>", event.getAuthor().getId());

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

        // ignore other bots by default
        if (event.getAuthor().isBot() || new CommandRouting(event).isHandled()) {
            return;
        }

        String message = event.getMessage().getContentRaw();
        System.out.printf("[GuildMessage] Received: %s\n", message);
    }


}
