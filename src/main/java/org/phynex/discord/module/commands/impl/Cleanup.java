package org.phynex.discord.module.commands.impl;

import org.phynex.discord.module.commands.Command;
import org.phynex.discord.module.commands.EventType;
import org.phynex.discord.module.commands.annotations.CommandAnnotation;
import org.phynex.discord.routing.GuildEvent;
import org.phynex.discord.routing.PrivateEvent;
import org.phynex.discord.routing.serializable.GuildMessageEvent;

@CommandAnnotation(
        keywords = { "clean", "cleanup", "purge" },
        description = "Purge all messages from a user / prefix set"
)
public class Cleanup extends Command {

    protected Cleanup(EventType instanceType, long owner, GuildMessageEvent event) {
        super(instanceType, owner, event);
    }

    @Override
    public boolean processIncomingMessage(GuildEvent guildEvent) {
        System.out.println("Incoming GuildRoute [MESSAGE]");
        return true;
    }

    @Override
    public boolean processIncomingMessage(PrivateEvent privateEvent) {
        System.out.println("Incoming PrivateRoute [MESSAGE]");
        return true;
    }

    @Override
    public boolean processIncomingReaction(GuildEvent guildEvent) {
        System.out.println("Incoming GuildRoute [REACTION]");
        return true;
    }

    @Override
    public boolean processIncomingReaction(PrivateEvent privateEvent) {
        System.out.println("Incoming PrivateRoute [REACTION]");
        return true;
    }


}
