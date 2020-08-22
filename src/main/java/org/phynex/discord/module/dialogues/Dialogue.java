package org.phynex.discord.module.dialogues;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import org.phynex.discord.routing.serializable.GuildMessageEvent;
import org.phynex.discord.routing.serializable.GuildReactionEvent;
import org.phynex.discord.routing.serializable.PrivateMessageEvent;
import org.phynex.discord.routing.serializable.PrivateReactionEvent;

import java.util.Arrays;

public abstract class Dialogue {

    protected int stage;

    protected TextChannel textChannel;

    protected PrivateChannel privateChannel;

    public Dialogue(TextChannel textChannel) {
        this.stage = 0;
        this.textChannel = textChannel;
    }

    public Dialogue(PrivateChannel privateChannel) {
        this.stage = 0;
        this.privateChannel = privateChannel;
    }

    public Dialogue load(TextChannel textChannel) {
        this.textChannel = textChannel;
        return this;
    }

    public Dialogue load(PrivateChannel privateChannel) {
        this.privateChannel = privateChannel;
        return this;
    }

    public abstract boolean processIncomingMessage(GuildMessageEvent event);

    public abstract boolean processIncomingMessage(PrivateMessageEvent event);

    public abstract boolean processIncomingReaction(GuildReactionEvent event);

    public abstract boolean processIncomingReaction(PrivateReactionEvent event);

    public int getStage() {
        return stage;
    }

    protected boolean validate(TextChannel textChannel) {
        return textChannel != null;
    }

    protected boolean validate(PrivateChannel privateChannel) {
        return privateChannel != null;
    }

    protected void sendOptions(String message, String... reactions) {
        if (validate(textChannel)) {
            textChannel.sendMessage(message)
                    .queue(c -> Arrays.stream(reactions)
                            .forEach(r -> c.addReaction(r).queue()));
        } else if (validate(privateChannel)) {
            privateChannel.sendMessage(message)
                    .queue(c -> Arrays.stream(reactions)
                            .forEach(r -> c.addReaction(r).queue()));
        }
    }

    protected void sendOptions(MessageEmbed messageEmbed, String... reactions) {
        if (validate(textChannel)) {
            textChannel.sendMessage(messageEmbed)
                    .queue(c -> Arrays.stream(reactions)
                            .forEach(r -> c.addReaction(r).queue()));
        } else if (validate(privateChannel)) {
            privateChannel.sendMessage(messageEmbed)
                    .queue(c -> Arrays.stream(reactions)
                            .forEach(r -> c.addReaction(r).queue()));
        }

    }

    public long getUser() {
        return validate(privateChannel) ? privateChannel.getUser().getIdLong() : -1;//todo
    }

}
