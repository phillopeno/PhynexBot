package org.phynex.discord.events.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.phynex.discord.events.CommandRouting;

import java.awt.*;

public class CleanMessages extends Command {

    public CleanMessages(CommandRouting router) {
        super(router);
    }

    /**
     * CleanMessages:
     *    if you @member it will ask if you want to purge all messages by that member
     *      if you respond yes in the next line it will delete your message (yes), and edit it's own saying:
     *          "**Action Complete**"
     *      else if your next message is not a yes (i.e: no, iafjidsojg, etc...) it will edit message saying:
     *          "*Action Aborted*"
     *
     *   if you say "!" it will ask "**Do you wish to purge all messages starting with "!"?**"
     *      same deal as before...
     */

    @Override
    public boolean handle() {
        if (!command[0].equalsIgnoreCase("!clean"))
            return false;
        if (router.getEventType() == EventType.PRIVATE) {
            router.sendMessage("Sorry, this command only works in a text-channel!");
            return true;
        }
        if (command.length != 2 || (command[1].contains("@") || command[1].contains("\""))) {
            router.sendMessageDelay(10, "Wrong syntax! [!clear @user] or [!clear \"!\"]");
            /*EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.RED);
            embedBuilder.setTitle("Invalid Syntax!");
            embedBuilder.addField("Example 1", String.format("!clean %s (Purge all messages from a member)", router.mentionAuthor()), false);
            embedBuilder.addField("Example 2", "!clean \"!\" (Purge all messages starting with !)", false);
            router.getMessage().getTextChannel().sendMessage(embedBuilder.build()).queue();
            router.getMessage().addReaction("👍").queue();*/
            return true;
        }


        router.getMessage().getMentionedMembers();


        router.getGuildTextChannel().sendMessage("Working on it...").queue();
        return true;
    }
}
