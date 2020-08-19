package org.phynex.discord.routing.serializable;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;

public class PrivateReactionEvent {

    private final User user;
    private final MessageReaction reaction;
    private final Message message;
    private final long responseNumber;
    private final EventAction eventAction;

    /**
     * A full reaction event
     *
     * @param user  the user object
     * @param reaction  the reaction object
     * @param message   the message object
     * @param responseNumber    the response uid (long)
     * @param eventAction   the action taken [ADD | REMOVE]
     */
    public PrivateReactionEvent(User user, MessageReaction reaction, Message message, long responseNumber, EventAction eventAction) {
        this.user = user;
        this.reaction = reaction;
        this.message = message;
        this.responseNumber = responseNumber;
        this.eventAction = eventAction;
    }

    public User getUser() {
        return user;
    }

    public MessageReaction getReaction() {
        return reaction;
    }

    public Message getMessage() {
        return message;
    }

    public long getResponseNumber() {
        return responseNumber;
    }

    public EventAction getEventAction() {
        return eventAction;
    }
}
