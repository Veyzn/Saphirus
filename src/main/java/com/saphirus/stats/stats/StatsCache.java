package com.saphirus.stats.stats;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StatsCache {
    private static final HashMap<String, HashMap<String, Object>> playerStatsCache = new HashMap<>();
    private String uuid;
    private File playerFile;
    private YamlConfiguration playerData;

    public StatsCache(String UUID) {
        this.uuid = UUID;
        this.playerFile = new File("plugins/Saphirus/stats", uuid + ".yml");
        this.playerData = YamlConfiguration.loadConfiguration(playerFile);

        if (!playerStatsCache.containsKey(UUID)) {
            loadPlayerDataFromFile();
        }
    }

    public void unloadPlayer() {
        if (playerStatsCache.containsKey(uuid)) {
            savePlayerDataToFile();
            playerStatsCache.remove(uuid);
        }
    }

    public static boolean isPlayerLoaded(String uuid) {
        return playerStatsCache.containsKey(uuid);
    }

    private void loadPlayerDataFromFile() {
        if (!playerFile.exists()) {
            playerFile.getParentFile().mkdirs();
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        HashMap<String, Object> data = new HashMap<>();
        data.put("Items_Sold", playerData.getLong("Items_Sold", 0));
        data.put("Money_Earned", playerData.getLong("Money_Earned", 0));
        data.put("Shards_Earned", playerData.getLong("Shards_Earned", 0));
        data.put("Gens_Upgrades", playerData.getLong("Gens_Upgrades", 0));
        data.put("Breakables_Broken", playerData.getLong("Breakables_Broken", 0));
        data.put("Breakables_Upgraded", playerData.getLong("Breakables_Upgraded", 0));
        data.put("Fish_Caught", playerData.getLong("Fish_Caught", 0));
        data.put("used_defences", playerData.getInt("used_defences", 0));
        data.put("core_broken", playerData.getInt("core_broken", 0));
        data.put("used_tnt", playerData.getInt("used_tnt", 0));
        data.put("broken_defences", playerData.getInt("broken_defences", 0));

        playerStatsCache.put(uuid, data);
    }

    private void savePlayerDataToFile() {
        Map<String, Object> data = playerStatsCache.get(uuid);
        playerData.set("Items_Sold", data.get("Items_Sold"));
        playerData.set("Money_Earned", data.get("Money_Earned"));
        playerData.set("Shards_Earned", data.get("Shards_Earned"));
        playerData.set("Gens_Upgrades", data.get("Gens_Upgrades"));
        playerData.set("Breakables_Broken", data.get("Breakables_Broken"));
        playerData.set("Breakables_Upgraded", data.get("Breakables_Upgraded"));
        playerData.set("Fish_Caught", data.get("Fish_Caught"));
        playerData.set("used_defences", data.get("used_defences"));
        playerData.set("core_broken", data.get("core_broken"));
        playerData.set("used_tnt", data.get("used_tnt"));
        playerData.set("broken_defences", data.get("broken_defences"));

        try {
            playerData.save(playerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Object> getPlayerStats(String uuid) {
        return playerStatsCache.get(uuid);
    }

    private Map<String, Object> getPlayerStats() {
        return playerStatsCache.get(uuid);
    }

    private long getLongValue(String key) {
        return (long) getPlayerStats().getOrDefault(key, 0L);
    }

    private int getIntValue(String key) {
        return (int) getPlayerStats().getOrDefault(key, 0);
    }

    private void updateLongValue(String key, long value) {
        getPlayerStats().put(key, value);
    }

    private void updateIntValue(String key, int value) {
        getPlayerStats().put(key, value);
    }

    // Methods for Items_Sold
    public long getItemsSold() {
        return getLongValue("Items_Sold");
    }

    public void addItemsSold(long amount) {
        updateLongValue("Items_Sold", getItemsSold() + amount);
    }

    public void removeItemsSold(long amount) {
        updateLongValue("Items_Sold", getItemsSold() - amount);
    }

    public void setItemsSold(long amount) {
        updateLongValue("Items_Sold", amount);
    }

    // Methods for Money_Earned
    public long getMoneyEarned() {
        return getLongValue("Money_Earned");
    }

    public void addMoneyEarned(long amount) {
        updateLongValue("Money_Earned", getMoneyEarned() + amount);
    }

    public void removeMoneyEarned(long amount) {
        updateLongValue("Money_Earned", getMoneyEarned() - amount);
    }

    public void setMoneyEarned(long amount) {
        updateLongValue("Money_Earned", amount);
    }

    // Methods for Shards_Earned
    public long getShardsEarned() {
        return getLongValue("Shards_Earned");
    }

    public void addShardsEarned(long amount) {
        updateLongValue("Shards_Earned", getShardsEarned() + amount);
    }

    public void removeShardsEarned(long amount) {
        updateLongValue("Shards_Earned", getShardsEarned() - amount);
    }

    public void setShardsEarned(long amount) {
        updateLongValue("Shards_Earned", amount);
    }

    // Methods for Gens_Upgrades
    public long getGensUpgrades() {
        return getLongValue("Gens_Upgrades");
    }

    public void addGensUpgrades(long amount) {
        updateLongValue("Gens_Upgrades", getGensUpgrades() + amount);
    }

    public void removeGensUpgrades(long amount) {
        updateLongValue("Gens_Upgrades", getGensUpgrades() - amount);
    }

    public void setGensUpgrades(long amount) {
        updateLongValue("Gens_Upgrades", amount);
    }

    // Methods for Breakables_Broken
    public long getBreakablesBroken() {
        return getLongValue("Breakables_Broken");
    }

    public void addBreakablesBroken(long amount) {
        updateLongValue("Breakables_Broken", getBreakablesBroken() + amount);
    }

    public void removeBreakablesBroken(long amount) {
        updateLongValue("Breakables_Broken", getBreakablesBroken() - amount);
    }

    public void setBreakablesBroken(long amount) {
        updateLongValue("Breakables_Broken", amount);
    }

    // Methods for Breakables_Upgraded
    public long getBreakablesUpgraded() {
        return getLongValue("Breakables_Upgraded");
    }

    public void addBreakablesUpgraded(long amount) {
        updateLongValue("Breakables_Upgraded", getBreakablesUpgraded() + amount);
    }

    public void removeBreakablesUpgraded(long amount) {
        updateLongValue("Breakables_Upgraded", getBreakablesUpgraded() - amount);
    }

    public void setBreakablesUpgraded(long amount) {
        updateLongValue("Breakables_Upgraded", amount);
    }

    // Methods for Fish_Caught
    public long getFishCaught() {
        return getLongValue("Fish_Caught");
    }

    public void addFishCaught(long amount) {
        updateLongValue("Fish_Caught", getFishCaught() + amount);
    }

    public void removeFishCaught(long amount) {
        updateLongValue("Fish_Caught", getFishCaught() - amount);
    }

    public void setFishCaught(long amount) {
        updateLongValue("Fish_Caught", amount);
    }

    // Methods for used_defences
    public int getUsedDefences() {
        return getIntValue("used_defences");
    }

    public void addUsedDefences(int amount) {
        updateIntValue("used_defences", getUsedDefences() + amount);
    }

    public void removeUsedDefences(int amount) {
        updateIntValue("used_defences", getUsedDefences() - amount);
    }

    public void setUsedDefences(int amount) {
        updateIntValue("used_defences", amount);
    }

    // Methods for core_broken
    public int getCoreBroken() {
        return getIntValue("core_broken");
    }

    public void addCoreBroken(int amount) {
        updateIntValue("core_broken", getCoreBroken() + amount);
    }

    public void removeCoreBroken(int amount) {
        updateIntValue("core_broken", getCoreBroken() - amount);
    }

    public void setCoreBroken(int amount) {
        updateIntValue("core_broken", amount);
    }

    // Methods for used_tnt
    public int getUsedTNT() {
        return getIntValue("used_tnt");
    }

    public void addUsedTNT(int amount) {
        updateIntValue("used_tnt", getUsedTNT() + amount);
    }

    public void removeUsedTNT(int amount) {
        updateIntValue("used_tnt", getUsedTNT() - amount);
    }

    public void setUsedTNT(int amount) {
        updateIntValue("used_tnt", amount);
    }

    // Methods for broken_defences
    public int getBrokenDefences() {
        return getIntValue("broken_defences");
    }

    public void addBrokenDefences(int amount) {
        updateIntValue("broken_defences", getBrokenDefences() + amount);
    }

    public void removeBrokenDefences(int amount) {
        updateIntValue("broken_defences", getBrokenDefences() - amount);
    }

    public void setBrokenDefences(int amount) {
        updateIntValue("broken_defences", amount);
    }

    public static void saveAllPlayersDataToFile() {
        for (String uuid : playerStatsCache.keySet()) {
            StatsCache StatsCache = new StatsCache(uuid);
            StatsCache.savePlayerDataToFile();
        }
    }
}
