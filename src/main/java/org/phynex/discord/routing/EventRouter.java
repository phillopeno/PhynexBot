package org.phynex.discord.routing;

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

    }

    /**
     * Private Routing
     */
    private void direct() {

    }
}
