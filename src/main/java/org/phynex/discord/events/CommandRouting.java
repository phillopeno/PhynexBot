package org.phynex.discord.events;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import org.phynex.discord.events.commands.CleanMessages;
import org.phynex.discord.events.commands.EventType;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class CommandRouting {

    private final User user;
    private final Message message;
    private final Guild guild;
    private final Member guildMember;
    private final TextChannel guildTextChannel;
    private final EventType eventType;

    public CommandRouting(GuildMessageReceivedEvent event) {
        this.user = event.getAuthor();
        this.guildMember = event.getMember();
        this.message = event.getMessage();
        this.guildTextChannel = event.getChannel();
        this.guild = event.getGuild();
        this.eventType = EventType.GUILD;
    }

    public CommandRouting(PrivateMessageReceivedEvent event) {
        this.user = event.getAuthor();
        this.message = event.getMessage();
        this.guild = null;
        this.guildMember = null;
        this.guildTextChannel = null;
        this.eventType = EventType.PRIVATE;
    }

    public boolean isHandled() {
        System.out.println("Checking to see if command is able to be handled...");

        if (new CleanMessages(this).handle())
            return true;

        System.out.println("Command was unable to be handled. Passing back to controller.");
        return false;
    }

    public User getUser() {
        return user;
    }

    public EventType getEventType() {
        return eventType;
    }

    public Guild getGuild() {
        return guild;
    }

    public Member getGuildMember() {
        return guildMember;
    }

    public TextChannel getGuildTextChannel() {
        return guildTextChannel;
    }

    public Message getMessage() {
        return message;
    }

    public void sendMessage(String message, Object... args) {
        if (eventType == EventType.PRIVATE)
            getMessage().getChannel().sendMessage(String.format(message, args)).queue();
        else
            getGuildTextChannel().sendMessage(String.format(message, args)).queue();
    }

    /**
     * Send message with delay in seconds for when it will auto-delete.
     * @param seconds
     *          the seconds before the message is removed.
     * @param message
     *          the message
     * @param args
     *          message params
     */
    public void sendMessageDelay(long seconds, String message, Object... args) {
        if (eventType == EventType.PRIVATE)
            getMessage().getChannel().sendMessage(String.format(message, args)).queue((msg) -> msg.delete().delay(Duration.ofSeconds(seconds)).queue());
        else
            getGuildTextChannel().sendMessage(String.format(message, args)).queue((msg) -> msg.delete().queueAfter(seconds, TimeUnit.SECONDS));
    }

    public void sendMessageReaction(String reaction, String message, Object... args) {
        if (eventType == EventType.PRIVATE)
            getMessage().getChannel().sendMessage(String.format(message, args)).queue((msg) -> msg.addReaction(reaction).queue());
        else
            getGuildTextChannel().sendMessage(String.format(message, args)).queue((msg) -> msg.addReaction(reaction).queue());
    }

    public String mentionAuthor() {
        return String.format("<@%s>", getUser().getId());
    }
}
