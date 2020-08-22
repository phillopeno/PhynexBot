package org.phynex.discord.module.commands.impl;

import net.dv8tion.jda.api.entities.PrivateChannel;
import org.phynex.discord.Controller;
import org.phynex.discord.module.annotations.CommandAnnotation;
import org.phynex.discord.module.commands.Command;
import org.phynex.discord.module.dialogues.impl.SimpleQuestionDialogue;
import org.phynex.discord.routing.GuildEvent;
import org.phynex.discord.routing.PrivateEvent;

@CommandAnnotation(keywords = {"hello", "hi"}, description = "Greetings!")
public class Greetings extends Command {

    @Override
    public boolean processIncomingMessage(GuildEvent guildEvent) {
        reply("Wazzup!");
        return true;
    }

    @Override
    public boolean processIncomingMessage(PrivateEvent privateEvent) {
        //reply("WAZZUPPPPP");
        PrivateChannel channel;
        if ((channel = privateEvent.getChannel()) != null) {
            Controller.getSession().getDialogueModule().putSafely(
                    privateEvent.getMessageEvent().getUser().getIdLong(),
                    new SimpleQuestionDialogue("Simple Question Test!", channel, b -> {
                        if (b) {
                            System.out.println("RESULT WAS TRUE!");
                        } else {
                            System.out.println("RESULT WAS FALSE!");
                        }
                    }),
                    false
            );
        }
        return true;
    }

    @Override
    public boolean processIncomingReaction(GuildEvent guildEvent) {
        return true;
    }

    @Override
    public boolean processIncomingReaction(PrivateEvent privateEvent) {
        return true;
    }
}
