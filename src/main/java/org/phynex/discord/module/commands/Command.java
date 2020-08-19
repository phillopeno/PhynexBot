package org.phynex.discord.module.commands;

import org.phynex.discord.controller.exceptions.InvalidRequestException;
import org.phynex.discord.routing.GuildRouting;
import org.phynex.discord.routing.PrivateRouting;
import org.phynex.discord.routing.serializable.GuildMessageEvent;
import org.phynex.discord.routing.serializable.PrivateMessageEvent;

public abstract class Command {

    protected EventType instanceType;

    /**
     * The discordId (long) of the initiator of this command.
     */
    protected long owner;

    /**
     * The time this command is set to expire (from inactivity)
     */
    protected long timeout;

    private GuildMessageEvent guildMessageEvent;

    private PrivateMessageEvent privateMessageEvent;

    protected Command() {
        //
    }

    protected Command(EventType instanceType, long owner, GuildMessageEvent event) {
        this.instanceType = instanceType;
        this.owner = owner;
        this.guildMessageEvent = event;
    }

    protected Command(EventType instanceType, long owner, PrivateMessageEvent event) {
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

    public void setup(EventType eventType, GuildRouting guildRouting) {
        this.instanceType = eventType;
        this.owner = guildRouting.getGuildEvent().getUser().getIdLong();
        this.guildMessageEvent = guildRouting.getGuildEvent();
    }

    public void setup(EventType eventType, PrivateRouting privateRouting) {
        this.instanceType = eventType;
        this.owner = privateRouting.getPrivateEvent().getUser().getIdLong();
        this.privateMessageEvent = privateRouting.getPrivateEvent();
    }

    public abstract boolean processIncomingMessage(GuildRouting guildRouting);

    public abstract boolean processIncomingMessage(PrivateRouting privateRouting);

    public abstract boolean processIncomingReaction(GuildRouting guildRouting);
}
