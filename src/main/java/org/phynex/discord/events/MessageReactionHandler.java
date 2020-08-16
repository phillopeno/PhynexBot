package org.phynex.discord.events;

import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class MessageReactionHandler extends ListenerAdapter {

    public void onPrivateMessageReactionAdd(PrivateMessageReactionAddEvent event) {
        if (event.getReactionEmote().getName().equals("❌") && !Objects.requireNonNull(event.getUser()).isBot()) {
            event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue((message -> {
                message.delete().queue();
                //message.editMessageFormat("hello!").queue();
                System.out.println("DELETION QUEUED");
            }));
        }

    }

    public void onPrivateMessageReactionRemove(PrivateMessageReactionRemoveEvent event) {
        System.out.println("Private message reaction removed.");
    }
}
