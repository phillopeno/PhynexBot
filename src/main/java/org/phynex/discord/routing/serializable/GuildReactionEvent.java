package org.phynex.discord.routing.serializable;

import net.dv8tion.jda.api.entities.*;

public class GuildReactionEvent {

    private final Member member;
    private final User user;
    private final MessageReaction reaction;
    private final Message message;
    private final long responseNumber;
    private final EventAction eventAction;

    /**
     * A full reaction event.
     *
     * @param eventAction   the event action [ADD or REMOVE]
     * @param member    the guild member object
     * @param user  the user object
     * @param reaction  the reaction object
     * @param message   the message object
     * @param responseNumber    the response uid (session)
     */
    public GuildReactionEvent(EventAction eventAction, Member member, User user, MessageReaction reaction, Message message, long responseNumber) {
        this.member = member;
        this.user = user;
        this.reaction = reaction;
        this.message = message;
        this.responseNumber = responseNumber;
        this.eventAction = eventAction;
    }

    public Member getMember() {
        return member;
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
