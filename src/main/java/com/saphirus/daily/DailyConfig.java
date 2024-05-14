package com.saphirus.daily;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class DailyConfig {

    public static File file = new File("./plugins/Saphirus/daily.yml");
    public static YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public static void createCFG() {
        if(!file.exists()) {
            try {
                file.createNewFile();
                cfg.set("dummy", "data");
                save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Set<String> getPlayers() {return cfg.getKeys(false);}

    public static boolean canClaim(String uuid) {
        if(getPlayers().contains(uuid)) {
            if(cfg.getLong(uuid) > System.currentTimeMillis()) {
                return false;
            } else return true;
        } else return true;
    }

    public static void addPlayer(String uuid, Long end) {
        cfg.set(uuid, end);
        save();
    }

    public static void save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
