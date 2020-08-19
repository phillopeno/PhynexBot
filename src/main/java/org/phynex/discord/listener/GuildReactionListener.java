package org.phynex.discord.listener;

import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.phynex.discord.routing.EventRouter;
import org.phynex.discord.routing.EventType;
import org.phynex.discord.routing.GuildEvent;
import org.phynex.discord.routing.PrivateEvent;
import org.phynex.discord.routing.serializable.EventAction;
import org.phynex.discord.routing.serializable.GuildReactionEvent;

public class GuildReactionListener extends ListenerAdapter {

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {
        event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> {
            GuildReactionEvent reactionEvent = new GuildReactionEvent(
                    EventAction.ADDED,
                    event.getMember(),
                    event.getUser(),
                    event.getReaction(),
                    message,
                    event.getResponseNumber()
            );
            GuildEvent guildEvent = new GuildEvent(EventType.REACTION, reactionEvent);

            EventRouter.route(guildEvent);
        });
    }

    @Override
    public void onGuildMessageReactionRemove(GuildMessageReactionRemoveEvent event) {
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
    }
}
