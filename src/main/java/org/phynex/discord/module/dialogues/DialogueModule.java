package org.phynex.discord.module.dialogues;

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

    //public boolean isHandled TODO handle incoming message, reaction for guild, private
}
