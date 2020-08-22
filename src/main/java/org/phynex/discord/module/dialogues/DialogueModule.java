package org.phynex.discord.module.dialogues;

import org.phynex.discord.controller.exceptions.UnexpectedOutcomeException;
import org.phynex.discord.routing.GuildEvent;
import org.phynex.discord.routing.PrivateEvent;

import java.util.HashMap;

public class DialogueModule {

    private final HashMap<Long, Dialogue> privateDialogues = new HashMap<>();
    private final HashMap<Long, Dialogue> guildDialogues = new HashMap<>();

    public DialogueModule() {

    }

    public boolean putSafely(long uid, Dialogue dialogue, boolean guild) {
        if (guild && !guildDialogues.containsKey(uid)) {
            return guildDialogues.put(uid, dialogue) != null;
        } else if (!guild && !privateDialogues.containsKey(uid)) {
            return privateDialogues.put(uid, dialogue) != null;
        }
        return false;
    }

    public void forcePut(long uid, Dialogue dialogue, boolean guild) {
        if (guild)
            guildDialogues.put(uid, dialogue);
        else
            privateDialogues.put(uid, dialogue);
    }

    public void remove(boolean guild, Dialogue dialogue) {
        if (guild) {
            guildDialogues.remove(dialogue.getUser());
        } else {
            privateDialogues.remove(dialogue.getUser());
        }
    }

    public boolean handledDialogue(GuildEvent guildEvent) {
        /**
         * TODO
         */
        return false;
    }

    public boolean handledDialogue(PrivateEvent privateEvent) {
        Dialogue retrieved = privateDialogues.get(privateEvent.getChannel().getUser().getIdLong());
        if (retrieved != null) {
            switch (privateEvent.getRoutingEvent()) {
                case MESSAGE:
                    return retrieved.load(privateEvent.getChannel())
                            .processIncomingMessage(privateEvent.getMessageEvent());
                case REACTION:
                    return retrieved.load(privateEvent.getChannel())
                            .processIncomingReaction(privateEvent.getReactionEvent());
                default:
                    return false;
            }
        }
        return false;
    }
}
