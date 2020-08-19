package org.phynex.discord.listener;

import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.phynex.discord.routing.serializable.EventAction;
import org.phynex.discord.routing.serializable.PrivateReactionEvent;

public class PrivateMessageReactionListener extends ListenerAdapter {

    @Override
    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
        event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> {
            new PrivateReactionEvent(
                    event.getUser(),
                    event.getReaction(),
                    message,
                    event.getResponseNumber(),
                    EventAction.ADDED
            );
        });
    }

    @Override
    public void onPrivateMessageReactionRemove(PrivateMessageReactionRemoveEvent event) {
        event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> {
            new PrivateReactionEvent(
                    event.getUser(),
                    event.getReaction(),
                    message,
                    event.getResponseNumber(),
                    EventAction.REMOVED
            );
        });
    }
}
