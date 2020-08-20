package org.phynex;

import org.phynex.discord.controller.ProjectSettings;

public class Discord {

    private static Discord discord;

    private final ProjectSettings projectSettings;

    public static void main(String[] args) {
        discord = new Discord();
    }

    private Discord() {
        this.projectSettings = new ProjectSettings();
    }


}
