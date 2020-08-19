package org.phynex.discord.module.commands.impl;

import org.phynex.discord.module.commands.Command;
import org.phynex.discord.module.commands.EventType;
import org.phynex.discord.module.commands.annotations.CommandMeta;
import org.phynex.discord.routing.GuildRouting;
import org.phynex.discord.routing.PrivateRouting;
import org.phynex.discord.routing.serializable.GuildEvent;

@CommandMeta(
        keywords = { "clean", "cleanup", "purge" },
        description = "Purge all messages from a user / prefix set"
)
public class Cleanup extends Command {

    protected Cleanup(EventType instanceType, long owner, GuildEvent event) {
        super(instanceType, owner, event);
    }

    @Override
    public boolean processIncomingRequest(GuildRouting guildRouting) {
        return false;
    }

    @Override
    public boolean processIncomingRequest(PrivateRouting privateRouting) {
        return false;
    }


}
