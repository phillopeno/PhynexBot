package org.phynex.discord.controller;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.phynex.discord.listener.GuildReactionListener;

import javax.security.auth.login.LoginException;

public class Session {

    private JDA jda;

    /**
     * todo some type of reflection solution to make this dynamically pull from events package
     */
    Object[] handlers = {
            new GuildReactionListener()
    };

    public Session(String token) {
        try {
            jda = JDABuilder.createDefault(
                    token,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_VOICE_STATES,
                    GatewayIntent.GUILD_EMOJIS,
                    GatewayIntent.GUILD_PRESENCES,
                    GatewayIntent.GUILD_BANS,
                    GatewayIntent.GUILD_MESSAGE_REACTIONS,
                    GatewayIntent.GUILD_INVITES,
                    //GatewayIntent.GUILD_MESSAGE_TYPING,

                    GatewayIntent.DIRECT_MESSAGES,
                    //GatewayIntent.DIRECT_MESSAGE_TYPING,
                    GatewayIntent.DIRECT_MESSAGE_REACTIONS
            )
                    .disableCache(CacheFlag.EMOTE, CacheFlag.VOICE_STATE)
                    .addEventListeners(handlers)
                    .setActivity(Activity.watching("MenuDocs"))
                    .build();

        } catch (LoginException e) {
            System.out.println("Token: " + token);
            System.err.println("Session failed with error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public JDA getJDA() {
        return jda;
    }
}
