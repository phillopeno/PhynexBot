package org.phynex.discord.listener;

import net.dv8tion.jda.api.events.channel.priv.PrivateChannelCreateEvent;
import net.dv8tion.jda.api.events.channel.priv.PrivateChannelDeleteEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageDeleteEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageEmbedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.phynex.discord.Controller;
import org.phynex.discord.routing.EventRouter;
import org.phynex.discord.routing.RouteType;
import org.phynex.discord.routing.PrivateEvent;
import org.phynex.discord.routing.serializable.PrivateMessageEvent;

public class PrivateMessageListener extends ListenerAdapter {

    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;
        Controller.debug("[PrivateMessageListener] Message Event Start");
        PrivateMessageEvent messageEvent = new PrivateMessageEvent(
                event.getAuthor(), event.getMessage());
        PrivateEvent privateEvent = new PrivateEvent(RouteType.MESSAGE, messageEvent);
        EventRouter.route(privateEvent);
        Controller.debug("[PrivateMessageListener] Message Event End");
    }

    /**
     * UNUSED
     */

    @Override
    public void onPrivateChannelCreate(@NotNull PrivateChannelCreateEvent event) {
        super.onPrivateChannelCreate(event);
    }

    @Override
    public void onPrivateChannelDelete(@NotNull PrivateChannelDeleteEvent event) {
        super.onPrivateChannelDelete(event);
    }

    @Override
    public void onPrivateMessageDelete(@NotNull PrivateMessageDeleteEvent event) {
        super.onPrivateMessageDelete(event);
    }

    @Override
    public void onPrivateMessageEmbed(@NotNull PrivateMessageEmbedEvent event) {
        super.onPrivateMessageEmbed(event);
    }

    @Override
    public void onPrivateMessageUpdate(@NotNull PrivateMessageUpdateEvent event) {
        super.onPrivateMessageUpdate(event);
    }
}
