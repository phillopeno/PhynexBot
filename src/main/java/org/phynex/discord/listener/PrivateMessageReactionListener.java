package org.phynex.discord.listener;

import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.phynex.discord.Controller;
import org.phynex.discord.routing.EventRouter;
import org.phynex.discord.routing.RouteType;
import org.phynex.discord.routing.PrivateEvent;
import org.phynex.discord.routing.serializable.EventAction;
import org.phynex.discord.routing.serializable.PrivateReactionEvent;

import java.util.Optional;

public class PrivateMessageReactionListener extends ListenerAdapter {

    @Override
    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
        if (Optional.ofNullable(event.getUser()).orElse(Controller.getSession().getAPI().getSelfUser()).isBot())
            return;

        Controller.debug("[PrivateReactionListener][ADD] Reaction Event Start");
        event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> {
            PrivateReactionEvent reactionEvent = new PrivateReactionEvent(
                    event.getUser(),
                    event.getReaction(),
                    message,
                    event.getResponseNumber(),
                    EventAction.ADDED
            );
            PrivateEvent privateEvent = new PrivateEvent(RouteType.REACTION, reactionEvent);

            EventRouter.route(privateEvent);
        });
        Controller.debug("[PrivateReactionListener][ADD] Reaction Event End");
    }

    @Override
    public void onPrivateMessageReactionRemove(PrivateMessageReactionRemoveEvent event) {
        Controller.debug("[PrivateReactionListener][ADD] Reaction Event Start");
        event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> {
            new PrivateReactionEvent(
                    event.getUser(),
                    event.getReaction(),
                    message,
                    event.getResponseNumber(),
                    EventAction.REMOVED
            );
        });
        Controller.debug("[PrivateReactionListener][REMOVE] Reaction Event End");
    }
}
