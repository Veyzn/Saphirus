package com.saphirus.gang;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GangManager {

    private String name;
    private File file;
    private YamlConfiguration cfg;

    public GangManager(String names) {
        name = names;
        file = new File("plugins/Saphirus/Gang/" + name + ".yml");
        cfg = YamlConfiguration.loadConfiguration(file);
    }


    public boolean exists(){return file.exists();}

    public void createGang(String uuid_owner) {
        try {
            file.createNewFile();

            cfg.set("Name", name);
            cfg.set("Owner", uuid_owner);
            cfg.set("Trophies", 0L);
            cfg.set("Members", new ArrayList<String>());
            cfg.set("Bank.money", 0L);
            cfg.set("Bank.gems", 0L);
            cfg.set("Team-Level",0);

            saveConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getName() {
        return cfg.getString("Name");
    }

    public void setName(String name) {
        cfg.set("Name", name);
        saveConfig();
    }


    public String getOwner() {
        return cfg.getString("Owner");
    }

    public void setOwner(String uuid_owner) {
        cfg.set("Owner", uuid_owner);
        saveConfig();
    }
    public long getTrophies() {
        return cfg.getLong("Trophies");
    }

    public void setTrophies(long trophies) {
        cfg.set("Trophies", trophies);
        saveConfig();
    }


    public List<String> getMembers() {
        return cfg.getStringList("Members");
    }

    public void addMember(String member) {
        List<String> members = getMembers();
        members.add(member);
        cfg.set("Members", members);
        saveConfig();
    }

    public void removeMember(String member) {
        List<String> members = getMembers();
        members.remove(member);
        cfg.set("Members", members);
        saveConfig();
    }


    public long getBankMoney() {
        return cfg.getLong("Bank.money");
    }

    public void setBankMoney(long money) {
        cfg.set("Bank.money", money);
        saveConfig();
    }


    public long getBankGems() {
        return cfg.getLong("Bank.gems");
    }

    public void setBankGems(long gems) {
        cfg.set("Bank.gems", gems);
        saveConfig();
    }


    public int getTeamLevel() {
        return cfg.getInt("Team-Level");
    }

    public void setTeamLevel(int level) {
        cfg.set("Team-Level", level);
        saveConfig();
    }

    public void saveConfig() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
