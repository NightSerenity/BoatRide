package com.aurora;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        createDefaultConfig();
        
        getCommand("ride").setExecutor(new RideCommand(this));
        getCommand("boatridereload").setExecutor(new ReloadCommand(this));

        getLogger().info("RidePlugin enabled!");

    }

    @Override
    public void onDisable() {
        getLogger().info("RidePlugin disabled.");
    }

    public static Main getInstance() {
        return instance;
    }

    public void reloadPluginConfig() {
        reloadConfig();
        createDefaultConfig();
        getLogger().info("RidePlugin config has been reloaded!");
    }

    private void createDefaultConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveResource("config.yml", false);
        }
    }
}