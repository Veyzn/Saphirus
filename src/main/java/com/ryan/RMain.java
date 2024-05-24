package com.ryan;

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
    }

}