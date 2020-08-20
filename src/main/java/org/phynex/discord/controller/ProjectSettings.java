package org.phynex.discord.controller;

import java.io.*;
import java.util.Properties;

public class ProjectSettings {

    private final Properties properties;

    public ProjectSettings() {
        String location = "/application.properties";

        this.properties = new Properties();

        try (InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(location)) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Properties getProperties() {
        return properties;
    }

    private void defaults() {
        properties.clear();

        properties.setProperty("bot.key", "");
        properties.setProperty("mode.debug", "true");

        try (OutputStream os = new FileOutputStream("/application.properties")) {
            properties.store(os, "Default");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
