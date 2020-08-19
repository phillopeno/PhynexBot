package org.phynex.discord.routing.serializable;

import net.dv8tion.jda.api.entities.*;

import java.io.Serializable;

public class PrivateMessageEvent implements Serializable {

    private final User user;
    private final Message message;


    public PrivateMessageEvent(User user, Message message) {
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public Message getMessage() {
        return message;
    }
}
