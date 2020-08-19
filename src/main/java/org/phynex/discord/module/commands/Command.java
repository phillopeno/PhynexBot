package org.phynex.discord.module.commands;

import org.phynex.discord.controller.exceptions.InvalidRequestException;
import org.phynex.discord.routing.GuildRouting;
import org.phynex.discord.routing.PrivateRouting;
import org.phynex.discord.routing.serializable.GuildEvent;
import org.phynex.discord.routing.serializable.PrivateEvent;

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

    private GuildEvent guildEvent;

    private PrivateEvent privateEvent;

    protected Command() {
        //
    }

    protected Command(EventType instanceType, long owner, GuildEvent event) {
        this.instanceType = instanceType;
        this.owner = owner;
        this.guildEvent = event;
    }

    protected Command(EventType instanceType, long owner, PrivateEvent event) {
        this.instanceType = instanceType;
        this.owner = owner;
        this.privateEvent = event;
    }

    protected GuildEvent getGuildEvent() throws InvalidRequestException {
        if (instanceType == EventType.PRIVATE)
            throw new InvalidRequestException("Attempt to pull Guild Information from Private Message Event!");
        return guildEvent;
    }

    protected PrivateEvent getPrivateEvent() throws InvalidRequestException {
        if (instanceType == EventType.GUILD)
            throw new InvalidRequestException("Attempt to pull Private Information from Guild Message Event!");
        return privateEvent;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setup(EventType eventType, GuildRouting guildRouting) {
        this.instanceType = eventType;
        this.owner = guildRouting.getGuildEvent().getUser().getIdLong();
        this.guildEvent = guildRouting.getGuildEvent();
    }

    public void setup(EventType eventType, PrivateRouting privateRouting) {
        this.instanceType = eventType;
        this.owner = privateRouting.getPrivateEvent().getUser().getIdLong();
        this.privateEvent = privateRouting.getPrivateEvent();
    }

    public abstract boolean processIncomingRequest(GuildRouting guildRouting);

    public abstract boolean processIncomingRequest(PrivateRouting privateRouting);
}
