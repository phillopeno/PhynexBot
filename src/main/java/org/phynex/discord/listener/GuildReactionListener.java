package org.phynex.discord.listener;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.phynex.discord.Controller;
import org.phynex.discord.routing.EventRouter;
import org.phynex.discord.routing.RouteType;
import org.phynex.discord.routing.GuildEvent;
import org.phynex.discord.routing.serializable.EventAction;
import org.phynex.discord.routing.serializable.GuildReactionEvent;

public class GuildReactionListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        if (event.getUser().isBot())
            return;

        Controller.debug("[GuildReactionListener][ADD] Reaction Event Start");

        event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> {
            GuildReactionEvent reactionEvent = new GuildReactionEvent(
                    EventAction.ADDED,
                    event.getMember(),
                    event.getUser(),
                    event.getReaction(),
                    message,
                    event.getResponseNumber()
            );
            GuildEvent guildEvent = new GuildEvent(RouteType.REACTION, reactionEvent);

            EventRouter.route(guildEvent);
        });
        Controller.debug("[GuildReactionListener][ADD] Reaction Event End");
    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
        Controller.debug("[GuildReactionListener][REMOVE] Reaction Event Start");
        event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> {
            new GuildReactionEvent(
                    EventAction.ADDED,
                    event.getMember(),
                    event.getUser(),
                    event.getReaction(),
                    message,
                    event.getResponseNumber()
            );
        });
        Controller.debug("[GuildReactionListener][REMOVE] Reaction Event End");
    }
}
