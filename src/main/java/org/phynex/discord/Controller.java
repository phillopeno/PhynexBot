package org.phynex.discord;

import org.phynex.discord.controller.Session;

public class Controller {

    private static Session session;

    private static final String BOT = "NzQ0NTg4NzYzNjM1Nzc3NjI2.XzlaUw.dZZxYWZO4CBaUghlwjTeKf88FzE";

    public static void main(String... args) {
        System.out.println("Discord Bot v1.01 by Phillip");
        System.out.println("Credits: DiscordAPI creators - 2020\n\n");
        session = new Session(BOT);
    }

    public static Session getSession() {
        return session;
    }

    private static boolean DEBUG_MODE = true;

    public static void debug(String string, Object... args) {
        if (DEBUG_MODE)
            System.out.printf("[SYSTEM DEBUG] " + string + "%n", args);
    }
}
