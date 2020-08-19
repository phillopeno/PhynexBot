package org.phynex.discord.module.commands;

import org.phynex.discord.module.commands.annotations.CommandMeta;

public class CommandEntry {

    private final Class<Command> command;

    private final CommandMeta metadata;

    public CommandEntry(Class<Command> command, CommandMeta metadata) {
        this.command = command;
        this.metadata = metadata;
    }

    public Class<Command> getCommand() {
        return command;
    }

    public CommandMeta getMetadata() {
        return metadata;
    }
}
