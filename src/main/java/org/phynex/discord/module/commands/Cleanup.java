package org.phynex.discord.module.commands;

import net.dv8tion.jda.api.entities.Message;
import org.phynex.discord.controller.Constants;
import org.phynex.discord.module.Command;
import org.phynex.discord.module.CommandModule;
import org.phynex.discord.module.EventType;
import org.phynex.discord.module.annotations.CommandAnnotation;
import org.phynex.discord.routing.GuildEvent;
import org.phynex.discord.routing.PrivateEvent;
import org.phynex.discord.routing.RouteType;
import org.phynex.discord.routing.serializable.GuildMessageEvent;
import org.phynex.discord.routing.serializable.PrivateMessageEvent;

import java.util.List;
import java.util.Objects;

@CommandAnnotation(
        keywords = { "clean", "cleanup", "purge" },
        description = "Purge all messages from a user / prefix set"
)
public class Cleanup extends Command {

    public Cleanup(EventType instanceType, long owner, GuildMessageEvent event) {
        super(instanceType, owner, event);
    }

    public Cleanup(EventType instanceType, long owner, PrivateMessageEvent event) {
        super(instanceType, owner, event);
    }

    public Cleanup() {
        super();
    }

    @Override
    public boolean processIncomingMessage(GuildEvent guildEvent) {
        System.out.println("Incoming GuildRoute [MESSAGE]");
        return true;
    }

    @Override
    public boolean processIncomingMessage(PrivateEvent privateEvent) {
        replyWithReaction("I can only clean my own messages! Would you like me to purge everything from our DMs?", Constants.CHECK_EMOJI, Constants.X_EMOJI);
        addListener(RouteType.REACTION, this);
        return true;
    }

    @Override
    public boolean processIncomingReaction(GuildEvent guildEvent) {
        System.out.println("REACTION EVENT DETECTED");
        guildEvent.getGuildReactionEvent().ifPresent(reaction -> {
            System.out.println("REACTED WITH " + reaction.getReaction().getReactionEmote().getAsCodepoints());
            removeListener();
        });
        return true;
    }

    @Override
    public boolean processIncomingReaction(PrivateEvent privateEvent) {
        System.out.println("REACTION EVENT DETECTED");
        privateEvent.getPrivateReactionEvent().ifPresent(reaction -> {
            switch (reaction.getReaction().getReactionEmote().getAsCodepoints()) {
                case "U+2705": // check
                    replyWithRemoval(5, "Very well sir!");
                    try {
                        Objects.requireNonNull(reaction.getReaction().getPrivateChannel())
                                .getHistoryFromBeginning(99).queue(messageHistory -> {
                            List<Message> history = messageHistory.getRetrievedHistory();
                            System.out.println("Size: " + history.size());
                            history.stream()
                                    .filter(message -> message.getAuthor().isBot())
                                    .forEach(message -> message.delete().queue());
                        });
                    } catch (NullPointerException e) {
                        reply("There was an error retrieving message history.");
                    }
                    break;
                case "U+274c": // X
                    replyWithRemoval(5, "Action aborted!");
                    break;
            }
            System.out.println("REACTED WITH " + reaction.getReaction().getReactionEmote().getAsCodepoints());
            removeListener();
        });
        return true;
    }


}
