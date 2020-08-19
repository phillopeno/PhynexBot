package org.phynex.discord.listener;

import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.phynex.discord.routing.EventRouter;
import org.phynex.discord.routing.EventType;
import org.phynex.discord.routing.PrivateEvent;
import org.phynex.discord.routing.serializable.EventAction;
import org.phynex.discord.routing.serializable.PrivateReactionEvent;

public class PrivateMessageReactionListener extends ListenerAdapter {

    @Override
    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
        event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> {
            PrivateReactionEvent reactionEvent = new PrivateReactionEvent(
                    event.getUser(),
                    event.getReaction(),
                    message,
                    event.getResponseNumber(),
                    EventAction.ADDED
            );
            PrivateEvent privateEvent = new PrivateEvent(EventType.REACTION, reactionEvent);

            EventRouter.route(privateEvent);
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
