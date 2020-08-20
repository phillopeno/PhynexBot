package org.phynex.discord.controller;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.phynex.discord.Controller;
import org.phynex.discord.listener.GuildMessageListener;
import org.phynex.discord.listener.GuildReactionListener;
import org.phynex.discord.listener.PrivateMessageListener;
import org.phynex.discord.listener.PrivateMessageReactionListener;
import org.phynex.discord.module.CommandModule;
import org.phynex.discord.module.GameModule;

import javax.security.auth.login.LoginException;

public class Session {

    private JDA api;

    private CommandModule commandModule;
    private GameModule gameModule;

    /**
     * todo some type of reflection solution to make this dynamically pull from events package
     */
    Object[] handlers = {
            new GuildReactionListener(),
            new PrivateMessageReactionListener(),
            new GuildMessageListener(),
            new PrivateMessageListener()
    };

    public Session(String token) {
        try {
            api = JDABuilder.createDefault(
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
            return;
        }

        initializeModules();
    }

    private void initializeModules() {
        Controller.debug("Loading Command Module...");
        commandModule = new CommandModule();
        Controller.debug("Command Module Loaded.");
        Controller.debug("Loading Game Module...");
        gameModule = new GameModule();
        Controller.debug("Game Module Loaded.");
    }

    public CommandModule getCommandModule() {
        return commandModule;
    }

    public GameModule getGameModule() {
        return gameModule;
    }

    public JDA getAPI() {
        return api;
    }
}
