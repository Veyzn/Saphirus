package com.saphirus.main;

import com.saphirus.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Config {

    public static File file = new File("plugins/Saphirus/config.yml");
    public static File ornder = new File("plugins/Saphirus/");
    public static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);


    public static void createConfig() {
        if(!ornder.exists()) {
            ornder.mkdir();
        }

        if(!file.exists()) {
            try {
                file.createNewFile();

                cfg.set("Joins", 0);
                cfg.set("Spawn", "null");
                cfg.set("PvP", true);
                cfg.set("Maintenance", false);
                cfg.set("MySQL.host", "HOST");
                cfg.set("MySQL.username", "username");
                cfg.set("MySQL.password", "password");

                cfg.save(file);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setSpawn(Player p) {
        cfg.set("Spawn", LocationUtil.locationToString(p.getLocation()));
        save();
    }

    public static Location getSpawn() {return LocationUtil.stringToLocation(cfg.getString("Spawn"));}

    public static void setJoins(int amount) {
        cfg.set("Joins", amount);
        save();
    }

    public static int getJoins() {return cfg.getInt("Joins");}


    public static void setPvP(boolean status) {
        cfg.set("PvP",status);
        save();
    }

    public static boolean isPvPEnabled() {return cfg.getBoolean("PvP");}


    public static void setMaintenance(boolean status) {
        cfg.set("Maintenance",status);
        save();
    }

    public static boolean isMaintenanceEnabled() {return cfg.getBoolean("Maintenance");}

    public static void save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
