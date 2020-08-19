package org.phynex.discord.module.commands;

import org.phynex.discord.module.commands.annotations.CommandAnnotation;

public class CommandEntry {

    private final Class<Command> command;

    private final CommandAnnotation metadata;

    public CommandEntry(Class<Command> command, CommandAnnotation metadata) {
        this.command = command;
        this.metadata = metadata;
    }

    public Class<Command> getCommand() {
        return command;
    }

    public CommandAnnotation getMetadata() {
        return metadata;
    }
}
