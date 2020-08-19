package org.phynex.discord.routing;

import org.phynex.discord.routing.serializable.PrivateMessageEvent;

public class PrivateRouting {

    private final PrivateMessageEvent privateMessageEvent;

    public PrivateRouting(PrivateMessageEvent privateMessageEvent) {
        this.privateMessageEvent = privateMessageEvent;
    }

    public PrivateMessageEvent getPrivateEvent() {
        return privateMessageEvent;
    }
}
