package org.phynex.discord.controller.exceptions;

public class InvalidRequestException extends Exception {

    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException() {
        super("An invalid request was made (Generic)");
    }
}
