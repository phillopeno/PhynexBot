package org.phynex.discord.module.commands.impl;

import org.phynex.discord.module.commands.Command;
import org.phynex.discord.module.commands.EventType;
import org.phynex.discord.module.commands.annotations.CommandAnnotation;
import org.phynex.discord.routing.GuildRouting;
import org.phynex.discord.routing.PrivateRouting;
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
    public boolean processIncomingMessage(GuildRouting guildRouting) {
        return false;
    }

    @Override
    public boolean processIncomingMessage(PrivateRouting privateRouting) {
        return false;
    }

    @Override
    public boolean processIncomingReaction(GuildRouting guildRouting) {
        return false;
    }


}
