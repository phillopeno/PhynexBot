package org.phynex.discord.module;

import net.dv8tion.jda.api.entities.Message;
import org.phynex.discord.Controller;
import org.phynex.discord.controller.exceptions.InvalidRequestException;
import org.phynex.discord.controller.exceptions.UnexpectedOutcomeException;
import org.phynex.discord.module.annotations.CommandAnnotation;
import org.phynex.discord.routing.GuildEvent;
import org.phynex.discord.routing.PrivateEvent;
import org.phynex.discord.routing.RouteType;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
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
        Reflections reflections = new Reflections("org.phynex.discord.module.commands");
        Set<Class<? extends Command>> classes = reflections.getSubTypesOf(Command.class);
        commands = classes.stream()
                .filter(c -> c.isAnnotationPresent(CommandAnnotation.class))
                .collect(Collectors.toMap(Function.identity(), c -> c.getAnnotation(CommandAnnotation.class)));
    }

    private boolean route(Command command, GuildEvent guildEvent) {
        switch (guildEvent.getRoutingEvent()) {
            case MESSAGE:
                return command.processIncomingMessage(guildEvent);
            case REACTION:
                return command.processIncomingReaction(guildEvent);
            default:
                //not supported
                return false;
        }
    }

    private boolean route(Command command, PrivateEvent privateEvent) {
        switch (privateEvent.getRoutingEvent()) {
            case MESSAGE:
                return command.processIncomingMessage(privateEvent);
            case REACTION:
                return command.processIncomingReaction(privateEvent);
            default:
                //not supported
                return false;
        }
    }

    public boolean processIncomingEvent(GuildEvent guildEvent) throws InvalidRequestException, UnexpectedOutcomeException {
        long uid;
        switch (guildEvent.getRoutingEvent()) {
            case MESSAGE:
                uid = guildEvent.getGuildMessageEvent()
                        .orElseThrow(InvalidRequestException::new).getUser().getIdLong();
                break;
            case REACTION:
                uid = guildEvent.getGuildReactionEvent()
                        .orElseThrow(InvalidRequestException::new).getUser().getIdLong();
                break;
            default:
                Controller.debug("[Unexpected %s] %s",
                        this.getClass().getName(), guildEvent.getRoutingEvent().name());
                return false;
        }
        Command command = guildCommandListeners.get(uid);
        if (command != null) {
            if (command.getTimeout() < System.currentTimeMillis()) {
                guildCommandListeners.remove(uid);
                Controller.debug("[CommandModule] Guild command timed out.");
                return processCommand(guildEvent);
            } else if (command.getExpectedResponse() != guildEvent.getRoutingEvent()) {
                guildCommandListeners.remove(uid);
                Controller.debug("[CommandModule][PUBLIC] Unexpected response event removed!");
                guildEvent.sendDefaultMessage("Unexpected Response, please try again later.");
                return false;
            }
            return route(command, guildEvent);
        }
        return processCommand(guildEvent);
    }

    public boolean processIncomingEvent(PrivateEvent privateEvent) throws InvalidRequestException, UnexpectedOutcomeException {
        long uid;
        switch (privateEvent.getRoutingEvent()) {
            case MESSAGE:
                uid = privateEvent.getPrivateMessageEvent()
                        .orElseThrow(InvalidRequestException::new).getUser().getIdLong();
                break;
            case REACTION:
                uid = privateEvent.getPrivateReactionEvent()
                        .orElseThrow(InvalidRequestException::new).getUser().getIdLong();
                break;
            default:
                Controller.debug("[Unexpected %s] %s",
                        this.getClass().getName(), privateEvent.getRoutingEvent().name());
                return false;
        }
        Command command = privateCommandListeners.get(uid);
        if (command != null) {
            if (command.getTimeout() < System.currentTimeMillis()) {
                privateCommandListeners.remove(uid);
                Controller.debug("[CommandModule] Private command timed out.");
                return processCommand(privateEvent);
            } else if (command.getExpectedResponse() != privateEvent.getRoutingEvent()) {
                privateCommandListeners.remove(uid);
                Controller.debug("[CommandModule][PRIVATE] Unexpected response event removed!");
                privateEvent.sendDefaultMessage("Unexpected Response, please try again later.");
                return false;
            }
            return route(command, privateEvent);
        }
        return processCommand(privateEvent);
    }

    private Optional<Map.Entry<Class<? extends Command>, CommandAnnotation>> find(String key) {
         return commands.entrySet()
                .stream()
                .filter(entrySet -> Arrays.stream(entrySet.getValue().keywords())
                        .anyMatch(key::equalsIgnoreCase)
                ).findFirst();
    }

    private String getKey(Message message) {
        String msg = message.getContentRaw();
        if (!msg.startsWith("!"))
            return null;
        return message.getContentRaw().split(" ")[0].replace("!", "");
    }

    private boolean processCommand(GuildEvent guildEvent) throws UnexpectedOutcomeException {
        if (guildEvent.getGuildMessageEvent().isEmpty())
            return false;
        String key = getKey(guildEvent.getGuildMessageEvent()
                .orElseThrow(UnexpectedOutcomeException::new).getMessage());
        if (key != null) {
            Class<? extends Command> command = find(key).map(Map.Entry::getKey).orElse(null);
            if (command != null) {
                try {
                    Command instance = command.getDeclaredConstructor().newInstance();
                    instance.setup(this, EventType.GUILD, guildEvent);
                    return route(instance, guildEvent);
                } catch (NoSuchMethodException | InstantiationException |
                        IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean processCommand(PrivateEvent privateEvent) throws UnexpectedOutcomeException {
        if (privateEvent.getPrivateMessageEvent().isEmpty())
            return false;
        String key = getKey(privateEvent.getPrivateMessageEvent()
                .orElseThrow(UnexpectedOutcomeException::new).getMessage());
        if (key != null) {
            Class<? extends Command> command = find(key).map(Map.Entry::getKey).orElse(null);
            if (command != null) {
                try {
                    Command instance = command.getDeclaredConstructor().newInstance();
                    instance.setup(this, EventType.PRIVATE, privateEvent);
                    return route(instance, privateEvent);
                } catch (NoSuchMethodException | InstantiationException |
                        IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * Redirect reaction event to command that is listening for it.
     */
    protected void addGuildListener(long uid, Command requester) {
        guildCommandListeners.put(uid, requester);
        Controller.debug("Listener added! %s", guildCommandListeners.toString());
    }

    /**
     * Redirect reaction event to command that is listening for it.
     */
    protected void addPrivateListener(long uid, Command requester) {
        privateCommandListeners.put(uid, requester);
        Controller.debug("Listener added! %s", privateCommandListeners.toString());
    }

    protected void removeGuildListener(long uid) {
        guildCommandListeners.remove(uid);
    }

    protected void removePrivateListener(long uid) {
        privateCommandListeners.remove(uid);
    }
}
