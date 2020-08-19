package org.phynex.discord.module.commands;

import org.phynex.discord.module.commands.annotations.CommandAnnotation;

public class CommandEntry {

    private final Class<? extends Command> command;

    private final CommandAnnotation metadata;

    public CommandEntry(Class<? extends Command> command, CommandAnnotation metadata) {
        this.command = command;
        this.metadata = metadata;
    }

    public Class<? extends Command> getCommand() {
        return command;
    }

    public CommandAnnotation getMetadata() {
        return metadata;
    }
}
