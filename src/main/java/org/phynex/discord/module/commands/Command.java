package org.phynex.discord.module.commands;

import org.phynex.discord.controller.exceptions.InvalidRequestException;
import org.phynex.discord.controller.exceptions.UnexpectedOutcomeException;
import org.phynex.discord.module.EventType;
import org.phynex.discord.routing.GuildEvent;
import org.phynex.discord.routing.PrivateEvent;
import org.phynex.discord.routing.RouteType;
import org.phynex.discord.routing.serializable.GuildMessageEvent;
import org.phynex.discord.routing.serializable.PrivateMessageEvent;

import java.util.concurrent.TimeUnit;

public abstract class Command {

    protected CommandModule module;

    protected EventType instanceType;

    /**
     * The discordId (long) of the initiator of this command.
     */
    protected long owner;

    /**
     * The expected response to this command
     *      Default: MESSAGE
     */
    protected RouteType expectedResponse = RouteType.MESSAGE;

    /**
     * The time this command is set to expire (from inactivity)
     */
    protected long timeout;

    private GuildMessageEvent guildMessageEvent;

    private PrivateMessageEvent privateMessageEvent;

    public Command() {

    }

    public Command(EventType instanceType, long owner, GuildMessageEvent event) {
        this.instanceType = instanceType;
        this.owner = owner;
        this.guildMessageEvent = event;
    }

    public Command(EventType instanceType, long owner, PrivateMessageEvent event) {
        this.instanceType = instanceType;
        this.owner = owner;
        this.privateMessageEvent = event;
    }

    protected GuildMessageEvent getGuildEvent() throws InvalidRequestException {
        if (instanceType == EventType.PRIVATE)
            throw new InvalidRequestException("Attempt to pull Guild Information from Private Message Event!");
        return guildMessageEvent;
    }

    protected PrivateMessageEvent getPrivateEvent() throws InvalidRequestException {
        if (instanceType == EventType.GUILD)
            throw new InvalidRequestException("Attempt to pull Private Information from Guild Message Event!");
        return privateMessageEvent;
    }

    public long getTimeout() {
        return timeout;
    }

    public RouteType getExpectedResponse() {
        return expectedResponse;
    }

    public void setup(CommandModule module, EventType eventType, GuildEvent guildEvent) throws UnexpectedOutcomeException {
        this.instanceType = eventType;
        this.owner = guildEvent.getGuildMessageEvent()
                .orElseThrow(UnexpectedOutcomeException::new).getUser().getIdLong();
        this.guildMessageEvent = guildEvent.getGuildMessageEvent()
                .orElseThrow(UnexpectedOutcomeException::new);
        this.module = module;
    }

    public void setup(CommandModule module, EventType eventType, PrivateEvent privateEvent) throws UnexpectedOutcomeException {
        this.instanceType = eventType;
        this.owner = privateEvent.getPrivateMessageEvent()
                .orElseThrow(UnexpectedOutcomeException::new).getUser().getIdLong();
        this.privateMessageEvent = privateEvent.getPrivateMessageEvent()
                .orElseThrow(UnexpectedOutcomeException::new);
        this.module = module;
    }

    public abstract boolean processIncomingMessage(GuildEvent guildEvent);

    public abstract boolean processIncomingMessage(PrivateEvent privateEvent);

    public abstract boolean processIncomingReaction(GuildEvent guildEvent);

    public abstract boolean processIncomingReaction(PrivateEvent privateEvent);

    protected void reply(String message) {
        try {
            if (instanceType == EventType.GUILD)
                getGuildEvent().getMessage().getTextChannel().sendMessage(message).queue();
            else
                getPrivateEvent().getMessage().getPrivateChannel().sendMessage(message).queue();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
    }

    protected void replyWithRemoval(int seconds, String message, Object... args) {
        try {
            if (instanceType == EventType.GUILD)
                getGuildEvent().getMessage().getTextChannel().sendMessage(message).queue(msg -> {
                    msg.delete().queueAfter(seconds, TimeUnit.SECONDS);
                });
            else
                getPrivateEvent().getMessage().getPrivateChannel().sendMessage(message).queue(msg -> {
                    msg.delete().queueAfter(seconds, TimeUnit.SECONDS);
                });
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
    }

    protected void replyWithReaction(String message, String... reactions) {
        try {
            if (instanceType == EventType.GUILD)
                getGuildEvent().getMessage().getTextChannel().sendMessage(message).queue(msg -> {
                    for (String reaction : reactions)
                        msg.addReaction(reaction).queue();
                });
            else
                getPrivateEvent().getMessage().getPrivateChannel().sendMessage(message).queue(msg -> {
                    for (String reaction : reactions)
                        msg.addReaction(reaction).queue();
                });
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
    }

    protected void addListener(RouteType expectedResponse, Command command) {
        this.expectedResponse = expectedResponse;
        this.timeout = System.currentTimeMillis() + (300 * 1000);//5 minutes
        switch (instanceType) {
            case GUILD:
                module.addGuildListener(owner, command);
                break;
            case PRIVATE:
                module.addPrivateListener(owner, command);
                break;
        }
    }

    protected void removeListener() {
        switch (instanceType) {
            case GUILD:
                module.removeGuildListener(owner);
                break;
            case PRIVATE:
                module.removePrivateListener(owner);
                break;
        }
    }
}
