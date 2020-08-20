package org.phynex.discord.module.commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.phynex.discord.Controller;
import org.phynex.discord.controller.Constants;
import org.phynex.discord.module.Command;
import org.phynex.discord.module.annotations.CommandAnnotation;
import org.phynex.discord.routing.GuildEvent;
import org.phynex.discord.routing.PrivateEvent;
import org.phynex.discord.routing.RouteType;

import javax.naming.ldap.Control;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.LongStream;

@CommandAnnotation(
        keywords = { "clean", "cleanup", "purge" },
        description = "Purge all messages from a user / prefix set"
)
public class Cleanup extends Command {

    private long[] target;
    private String prefixTarget;

    @Override
    public boolean processIncomingMessage(GuildEvent guildEvent) {
        guildEvent.getGuildMessageEvent().ifPresent(event -> {
            List<Member> mentioned = event.getMessage().getMentionedMembers();
            if (!mentioned.isEmpty()) {
                replyWithReaction("Are you sure you want to remove the messages of "
                        + mentioned.toString(), Constants.CHECK_EMOJI, Constants.X_EMOJI);
                target = mentioned.stream().flatMapToLong(member -> LongStream.of(member.getUser().getIdLong())).toArray();
                addListener(RouteType.REACTION, this);
                return;
            }
            String[] cmd = event.getMessage().getContentRaw().split(" ");
            if (cmd.length > 1) {
                replyWithReaction("Are you sure you want to remove all messages starting with "
                    + cmd[1] + "?", Constants.CHECK_EMOJI, Constants.X_EMOJI);
                prefixTarget = cmd[1];
                addListener(RouteType.REACTION, this);
                return;
            }
            reply("Sorry try mentioning someone or using a prefix!");
        });
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
            switch (reaction.getReaction().getReactionEmote().getAsCodepoints()) {
                case Constants.UNI_CHECK: // check
                    replyWithRemoval(5, "Very well sir!");
                    try {
                        Objects.requireNonNull(reaction.getReaction().getTextChannel())
                                .getHistoryFromBeginning(99).queue(messageHistory -> {
                            List<Message> history = messageHistory.getRetrievedHistory();
                            System.out.println("Size: " + history.size());
                            history.stream()
                                    .filter(message -> {
                                        if (target != null) {
                                            Controller.debug("Target: %s", Arrays.toString(target));
                                            return Arrays.stream(target)
                                                    .anyMatch(t -> message.getAuthor().getIdLong() == t);
                                        } else if (prefixTarget != null) {
                                            return message.getContentRaw().toLowerCase()
                                                    .startsWith(prefixTarget.toLowerCase());
                                        }
                                        return false;
                                    })
                                    .forEach(message -> message.delete().queue());
                        });
                    } catch (NullPointerException e) {
                        replyWithRemoval(10, "There was an error retrieving message history.");
                    }
                    break;
                case Constants.UNI_X:// X
                    replyWithRemoval(5, "Action aborted!");
                    break;
            }
        });
        removeListener();
        return true;
    }

    @Override
    public boolean processIncomingReaction(PrivateEvent privateEvent) {
        System.out.println("REACTION EVENT DETECTED");
        privateEvent.getPrivateReactionEvent().ifPresent(reaction -> {
            switch (reaction.getReaction().getReactionEmote().getAsCodepoints()) {
                case Constants.UNI_CHECK: // check
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
                case Constants.UNI_X:// X
                    replyWithRemoval(5, "Action aborted!");
                    break;
            }
            System.out.println("REACTED WITH " + reaction.getReaction().getReactionEmote().getAsCodepoints());
        });
        removeListener();
        return true;
    }


}
