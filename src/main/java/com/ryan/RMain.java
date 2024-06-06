package com.ryan;

import com.ryan.features.generators.listeners.GenPlayerListener;
import com.ryan.features.generators.managers.GenPlayerManager;
import com.saphirus.main.Main;
import com.saphirus.main.MySQL;
import org.bukkit.plugin.java.JavaPlugin;

public class RMain {

    public static JavaPlugin PLUGIN;
    public static RMain RMAIN;
    public static Main SAPHIRUS_MAIN;
    public static MySQL sql;

    // I will be treating this as the onEnable method
    public RMain( Main saphirusMain ) {
        RMAIN = this;
        PLUGIN = saphirusMain;
        SAPHIRUS_MAIN = saphirusMain;
        sql = Main.sql;

        // Initializing the GenPlayer cache
        GenPlayerManager.initialize();

        // Register events
        registerEvents();
        // Register commands
        registerCommands();
    }

    // This will be treated as the onDisable method
    public void onDisable() {
        // Saving the GenPlayer cache
        GenPlayerManager.saveAllSynchronously();
    }

    public void registerCommands() {

    }

    public void registerEvents() {
        PLUGIN.getServer().getPluginManager().registerEvents( new GenPlayerListener(), PLUGIN );
    }
}