package org.phynex.discord.routing;

import org.phynex.discord.Controller;
import org.phynex.discord.controller.Constants;
import org.phynex.discord.controller.exceptions.InvalidRequestException;
import org.phynex.discord.controller.exceptions.UnexpectedOutcomeException;

import javax.naming.ldap.Control;

public class EventRouter {

    /**
     * Handles guild events.
     */
    public static void route(GuildEvent event) {
        new EventRouter(event).guild();
    }

    /**
     * Handles private events.
     */
    public static void route(PrivateEvent event) {
        new EventRouter(event).direct();
    }

    private final GuildEvent guildEvent;

    private final PrivateEvent privateEvent;

    public EventRouter(GuildEvent guildEvent) {
        this.guildEvent = guildEvent;
        this.privateEvent = null;
    }

    public EventRouter(PrivateEvent privateEvent) {
        this.privateEvent = privateEvent;
        this.guildEvent = null;
    }

    /**
     * Guild Routing
     */
    private void guild() {
        //Check to see if it's a command:
        try {
            if (guildEvent != null) {
                if (Controller.getSession().getCommandModule().processIncomingEvent(guildEvent))
                    return;
            }
        } catch (InvalidRequestException | UnexpectedOutcomeException e) {
            Controller.debug(e.getMessage());
            return;
        }
        Controller.debug("Guild route has finished, no command was utilized.");
    }

    /**
     * Private Routing
     */
    private void direct() {
        //Check to see if it's a command:
        try {
            if (privateEvent != null) {
                if (Controller.getSession().getCommandModule().processIncomingEvent(privateEvent))
                    return;
                else if (Controller.getSession().getDialogueModule().handledDialogue(privateEvent))
                    return;
            }
        } catch (InvalidRequestException | UnexpectedOutcomeException e) {
            Controller.debug(e.getMessage());
            return;
        }
        Controller.debug("Private route has finished, no command was utilized.");
    }
}
