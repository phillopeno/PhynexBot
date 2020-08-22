package org.phynex.discord.module.dialogues.impl;

import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.phynex.discord.controller.Constants;
import org.phynex.discord.module.dialogues.Dialogue;
import org.phynex.discord.routing.serializable.GuildMessageEvent;
import org.phynex.discord.routing.serializable.GuildReactionEvent;
import org.phynex.discord.routing.serializable.PrivateMessageEvent;
import org.phynex.discord.routing.serializable.PrivateReactionEvent;

import java.util.function.Consumer;

/**
 * Provides a yes/no dialogue for the user to complete
 */
public class SimpleQuestionDialogue extends Dialogue {

    private final Consumer<Boolean> consumer;

    public SimpleQuestionDialogue(String question, TextChannel textChannel, Consumer<Boolean> consumer) {
        super(textChannel);
        this.consumer = consumer;
        sendOptions(question, Constants.CHECK_EMOJI, Constants.X_EMOJI);
    }

    public SimpleQuestionDialogue(String question, PrivateChannel privateChannel, Consumer<Boolean> consumer) {
        super(privateChannel);
        this.consumer = consumer;
        sendOptions(question, Constants.CHECK_EMOJI, Constants.X_EMOJI);
    }

    @Override
    public boolean processIncomingMessage(GuildMessageEvent event) {
        return false;
    }

    @Override
    public boolean processIncomingMessage(PrivateMessageEvent event) {
        return false;
    }

    private boolean decodeReactionEvent(String reactionCode) {
        switch (reactionCode) {
            case Constants.UNI_CHECK:
                consumer.accept(true);
                return true;
            case Constants.UNI_X:
                consumer.accept(false);
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean processIncomingReaction(GuildReactionEvent event) {
        if (validate(textChannel)) {
            return decodeReactionEvent(event.getReaction().getReactionEmote().getAsCodepoints());
        }
        return false;
    }

    @Override
    public boolean processIncomingReaction(PrivateReactionEvent event) {
        if (validate(privateChannel)) {
            return decodeReactionEvent(event.getReaction().getReactionEmote().getAsCodepoints());
        }
        return false;
    }
}
