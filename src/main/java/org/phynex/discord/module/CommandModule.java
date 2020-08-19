package org.phynex.discord.module;

import org.phynex.discord.Controller;
import org.phynex.discord.controller.exceptions.InvalidRequestException;
import org.phynex.discord.controller.exceptions.UnexpectedOutcomeException;
import org.phynex.discord.module.commands.Command;
import org.phynex.discord.module.commands.EventType;
import org.phynex.discord.module.commands.annotations.CommandAnnotation;
import org.phynex.discord.routing.GuildRouting;
import org.phynex.discord.routing.PrivateRouting;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.rmi.UnexpectedException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CommandModule {

    private final HashMap<Long, Command> guildCommandListeners, privateCommandListeners;
    private Map<Class<? extends Command>, CommandAnnotation> commands;

    public CommandModule() {
        this.guildCommandListeners = new HashMap<>();
        this.privateCommandListeners = new HashMap<>();
        init();
    }

    private void init() {
        Reflections reflections = new Reflections("org.phynex.discord.module.commands.impl");
        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);
        commands = classes.stream()
                .filter(c -> c.isAnnotationPresent(CommandAnnotation.class))
                .collect(Collectors.toMap(Function.identity(), c -> c.getAnnotation(CommandAnnotation.class)));
    }

    private boolean route(Command command, GuildRouting guildRouting) {
        switch (guildRouting.getRoutingEvent()) {
            case MESSAGE:
                return command.processIncomingMessage(guildRouting);
            case REACTION:
                return command.processIncomingReaction(guildRouting);
            default:
                //not supported
                return false;
        }
    }

    private boolean route(Command command, PrivateRouting privateRouting) {
        switch (privateRouting.getRoutingEvent()) {
            case MESSAGE:
                return command.processIncomingMessage(privateRouting);
            case REACTION:
                return command.processIncomingReaction(privateRouting);
            default:
                //not supported
                return false;
        }
    }

    public boolean processIncomingMessage(GuildRouting guildRouting) throws InvalidRequestException, UnexpectedOutcomeException {
        long uid;
        switch (guildRouting.getRoutingEvent()) {
            case MESSAGE:
                uid = guildRouting.getGuildMessageEvent()
                        .orElseThrow(InvalidRequestException::new).getUser().getIdLong();
                break;
            case REACTION:
                uid = guildRouting.getGuildReactionEvent()
                        .orElseThrow(InvalidRequestException::new).getUser().getIdLong();
                break;
            default:
                Controller.debug("[Unexpected %s] %s",
                        this.getClass().getName(), guildRouting.getRoutingEvent().name());
                return false;
        }
        Command command = guildCommandListeners.get(uid);
        if (command != null) {
            if (command.getTimeout() < System.currentTimeMillis()) {
                guildCommandListeners.remove(uid);
                Controller.debug("[CommandModule] Guild command timed out.");
                return processCommand(guildRouting);
            }
            return route(command, guildRouting);
        }
        return processCommand(guildRouting);
    }

    public boolean processIncomingMessage(PrivateRouting privateRouting) throws InvalidRequestException, UnexpectedOutcomeException {
        long uid;
        switch (privateRouting.getRoutingEvent()) {
            case MESSAGE:
                uid = privateRouting.getPrivateMessageEvent()
                        .orElseThrow(InvalidRequestException::new).getUser().getIdLong();
                break;
            case REACTION:
                uid = privateRouting.getPrivateReactionEvent()
                        .orElseThrow(InvalidRequestException::new).getUser().getIdLong();
                break;
            default:
                Controller.debug("[Unexpected %s] %s",
                        this.getClass().getName(), privateRouting.getRoutingEvent().name());
                return false;
        }
        Command command = privateCommandListeners.get(uid);
        if (command != null) {
            if (command.getTimeout() < System.currentTimeMillis()) {
                privateCommandListeners.remove(uid);
                Controller.debug("[CommandModule] Private command timed out.");
                return processCommand(privateRouting);
            }
            return route(command, privateRouting);
        }
        return processCommand(privateRouting);
    }

    private Optional<Map.Entry<Class<? extends Command>, CommandAnnotation>> find(String key) {
         return commands.entrySet()
                .stream()
                .filter(entrySet -> Arrays.stream(entrySet.getValue().keywords())
                        .anyMatch(key::equalsIgnoreCase)
                ).findFirst();
    }

    private boolean processCommand(GuildRouting guildRouting) throws UnexpectedOutcomeException {
        if (guildRouting.getGuildMessageEvent().isEmpty())
            return false;

        String key = guildRouting.getGuildMessageEvent().orElseThrow(UnexpectedOutcomeException::new)
                .getMessage().getContentRaw().split(" ")[0].replace("!", "");

        Class<? extends Command> command = find(key).map(Map.Entry::getKey).orElse(null);
        if (command != null) {
            try {
                Command instance = command.getDeclaredConstructor().newInstance();
                instance.setup(EventType.GUILD, guildRouting);
                //instance.processIncomingMessage(guildRouting);
                return route(instance, guildRouting);
            } catch (NoSuchMethodException | InstantiationException |
                    IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean processCommand(PrivateRouting privateRouting) throws UnexpectedOutcomeException {
        if (privateRouting.getPrivateMessageEvent().isEmpty())
            return false;

        String key = privateRouting.getPrivateMessageEvent().orElseThrow(UnexpectedOutcomeException::new)
                .getMessage().getContentRaw().split(" ")[0].replace("!", "");

        Class<? extends Command> command = find(key).map(Map.Entry::getKey).orElse(null);
        if (command != null) {
            try {
                Command instance = command.getDeclaredConstructor().newInstance();
                instance.setup(EventType.PRIVATE, privateRouting);

                instance.setup(EventType.GUILD, privateRouting);
                //instance.processIncomingMessage(privateRouting);
                return route(instance, privateRouting);
            } catch (NoSuchMethodException | InstantiationException |
                    IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Redirect reaction event to command that is listening for it.
     */
    protected void addGuildReactionListener() {

    }

    /**
     * Redirect reaction event to command that is listening for it.
     */
    protected void addPrivateReactionListener() {

    }

    /**
     * Redirect message event to command that is listening for it.
     */
    protected void addGuildMessageListener() {

    }

    /**
     * Redirect message event to command that is listening for it.
     */
    protected void addPrivateMessageListener() {

    }
}
