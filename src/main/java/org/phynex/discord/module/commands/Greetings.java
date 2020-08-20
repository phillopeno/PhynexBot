package org.phynex.discord.module.commands;

import org.phynex.discord.module.Command;
import org.phynex.discord.module.annotations.CommandAnnotation;
import org.phynex.discord.routing.GuildEvent;
import org.phynex.discord.routing.PrivateEvent;

@CommandAnnotation(keywords = {"hello", "hi"}, description = "Greetings!")
public class Greetings extends Command {

    @Override
    public boolean processIncomingMessage(GuildEvent guildEvent) {
        reply("Wazzup!");
        return true;
    }

    @Override
    public boolean processIncomingMessage(PrivateEvent privateEvent) {
        reply("WAZZUPPPPP");
        return true;
    }

    @Override
    public boolean processIncomingReaction(GuildEvent guildEvent) {
        return true;
    }

    @Override
    public boolean processIncomingReaction(PrivateEvent privateEvent) {
        return true;
    }
}
