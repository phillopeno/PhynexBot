package org.phynex.discord.routing.serializable;

import net.dv8tion.jda.api.entities.*;

import java.io.Serializable;

public class GuildEvent implements Serializable {

    private final User user;
    private final Message message;
    private final Guild guild;
    private final Member guildMember;
    private final TextChannel guildTextChannel;


    public GuildEvent(User user, Message message, Guild guild, Member guildMember, TextChannel guildTextChannel) {
        this.user = user;
        this.message = message;
        this.guild = guild;
        this.guildMember = guildMember;
        this.guildTextChannel = guildTextChannel;
    }

    public User getUser() {
        return user;
    }

    public Message getMessage() {
        return message;
    }

    public Guild getGuild() {
        return guild;
    }

    public Member getGuildMember() {
        return guildMember;
    }

    public TextChannel getGuildTextChannel() {
        return guildTextChannel;
    }
}
