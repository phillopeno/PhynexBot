package org.phynex.discord.events.commands;

import org.phynex.discord.events.CommandRouting;

public abstract class Command {

    protected final CommandRouting router;

    protected final String[] command;

    public Command(CommandRouting router) {
        this.router = router;
        this.command = router.getMessage().getContentRaw().split(" ");
    }

    public boolean handle() {
        router.sendMessage("Sorry, this command is not handled properly!");
        return true;
    }
}
