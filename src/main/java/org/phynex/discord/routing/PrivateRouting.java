package org.phynex.discord.routing;

import org.phynex.discord.routing.serializable.PrivateEvent;

public class PrivateRouting {

    private final PrivateEvent privateEvent;

    public PrivateRouting(PrivateEvent privateEvent) {
        this.privateEvent = privateEvent;
    }

    public PrivateEvent getPrivateEvent() {
        return privateEvent;
    }
}
