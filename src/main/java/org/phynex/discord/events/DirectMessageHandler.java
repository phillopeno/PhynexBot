package org.phynex.discord.events;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.function.Function;

public class DirectMessageHandler extends ListenerAdapter {

    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {

        // ignore other bots by default
        if (event.getAuthor().isBot() || new CommandRouting(event).isHandled()) {
            return;
        }

        String message = event.getMessage().getContentRaw();

        System.out.printf("[DirectMessage] Received: %s\n", message);
    }
}
