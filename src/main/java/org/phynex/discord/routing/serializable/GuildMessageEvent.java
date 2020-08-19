package org.phynex.discord.routing.serializable;

import net.dv8tion.jda.api.entities.*;

import java.io.Serializable;

public class GuildMessageEvent implements Serializable {

    private final User user;
    private final Message message;
    private final Member guildMember;


    public GuildMessageEvent(User user, Member guildMember, Message message) {
        this.user = user;
        this.message = message;
        this.guildMember = guildMember;
    }

    public User getUser() {
        return user;
    }

    public Message getMessage() {
        return message;
    }

    public Member getGuildMember() {
        return guildMember;
    }
}
