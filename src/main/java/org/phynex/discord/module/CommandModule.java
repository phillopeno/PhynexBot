package org.phynex.discord.module;

import org.phynex.discord.Controller;
import org.phynex.discord.module.commands.Command;
import org.phynex.discord.module.commands.CommandEntry;
import org.phynex.discord.module.commands.EventType;
import org.phynex.discord.module.commands.annotations.CommandMeta;
import org.phynex.discord.routing.GuildRouting;
import org.phynex.discord.routing.PrivateRouting;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;

public class CommandModule {

    private final HashMap<Long, Command> pendingGuildCommands, pendingPrivateCommands;
    private final List<CommandEntry> commands;

    public CommandModule() {
        this.pendingGuildCommands = new HashMap<>();
        this.pendingPrivateCommands = new HashMap<>();
        this.commands = new ArrayList<>();
        Reflections reflections = new Reflections("org.phynex.discord.module.commands.impl");
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);
        classes.stream()
                .filter(Predicate.not(Class::isAnonymousClass))
                .filter(c -> c.isAnnotationPresent(CommandMeta.class))
                .forEach(c -> {
                    try {
                        Object instance = c.getDeclaredConstructor().newInstance();
                        if (instance instanceof Command) {
                            CommandMeta metadata = c.getAnnotation(CommandMeta.class);
                            commands.add(new CommandEntry((Class<Command>) c, metadata));
                        }
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
    }

    public boolean processIncomingMessage(GuildRouting guildRouting) {
        long uid = guildRouting.getGuildEvent().getUser().getIdLong();
        Command command = pendingGuildCommands.get(uid);
        if (command != null) {
            if (command.getTimeout() < System.currentTimeMillis()) {
                pendingGuildCommands.remove(uid);
                Controller.debug("[CommandModule] Guild command timed out.");
                return processCommand(guildRouting);
            }
            return command.processIncomingRequest(guildRouting);
        }
        return processCommand(guildRouting);
    }

    public boolean processIncomingMessage(PrivateRouting privateRouting) {
        long uid = privateRouting.getPrivateEvent().getUser().getIdLong();
        Command command = pendingPrivateCommands.get(uid);
        if (command != null) {
            if (command.getTimeout() < System.currentTimeMillis()) {
                pendingPrivateCommands.remove(uid);
                Controller.debug("[CommandModule] Private command timed out.");
                return processCommand(privateRouting);
            }
            return command.processIncomingRequest(privateRouting);
        }
        return processCommand(privateRouting);
    }

    private CommandEntry find(String key) {
        for (CommandEntry command : commands) {
            for (String keyword : command.getMetadata().keywords()) {
                if (keyword.equalsIgnoreCase(key)) {
                    return command;
                }
            }
        }
        return null;
    }

    private boolean processCommand(GuildRouting guildRouting) {
        CommandEntry entry = find(guildRouting.getGuildEvent().getMessage().getContentRaw().split(" ")[0].replace("!", ""));
        if (entry != null) {
            try {
                Command command = entry.getCommand().getDeclaredConstructor().newInstance();
                command.setup(EventType.GUILD, guildRouting);
                command.processIncomingRequest(guildRouting);
                return true;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean processCommand(PrivateRouting privateRouting) {
        return false;
    }
}
