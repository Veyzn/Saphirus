package com.saphirus.utils;

import com.saphirus.main.Main;
import com.saphirus.main.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TempPlayerCache {
    private static final HashMap<String, HashMap<String, Object>> playerDataCache = new HashMap<>();
    static MySQL sql = Main.sql;
    Connection connection = sql.getConnection();

    private String uuid;

    public TempPlayerCache(String UUID) {
        this.uuid = UUID;
        if (!playerDataCache.containsKey(UUID)) {
            loadPlayerDataFromDatabase();
        }
    }

    public void unloadPlayer() {
        if(playerDataCache.containsKey(uuid)) {
            PlayerCache pc = new PlayerCache(uuid);
            pc.updateAllValues(playerDataCache.get(uuid));
            playerDataCache.remove(uuid);
        }
    }

    public static boolean isPlayerLoaded(String uuid) {
        return playerDataCache.containsKey(uuid);
    }

    private void loadPlayerDataFromDatabase() {
        String selectQuery = "SELECT * FROM PlayerData WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("UUID", resultSet.getString("UUID"));
                    data.put("Name", resultSet.getString("Name"));
                    data.put("Team", resultSet.getString("Team"));
                    data.put("Money", resultSet.getLong("Money"));
                    data.put("Saphirus", resultSet.getLong("Saphirus"));
                    data.put("Gems", resultSet.getLong("Gems"));
                    data.put("Linked", resultSet.getBoolean("Linked"));
                    data.put("WoodCutting", resultSet.getLong("WoodCutting"));
                    data.put("Blocks", resultSet.getLong("Blocks"));
                    data.put("Fishing", resultSet.getLong("Fishing"));
                    data.put("PVE_KILLS", resultSet.getLong("PVE_KILLS"));
                    data.put("Kills", resultSet.getInt("Kills"));
                    data.put("Deaths", resultSet.getInt("Deaths"));
                    data.put("Killstreak", resultSet.getInt("Killstreak"));
                    data.put("Bans", resultSet.getInt("Bans"));
                    data.put("Mutes", resultSet.getInt("Mutes"));
                    data.put("Warns_Total", resultSet.getInt("Warns_Total"));
                    data.put("Warns_Now", resultSet.getInt("Warns_Now"));
                    data.put("JoinDate", resultSet.getString("JoinDate"));
                    data.put("Playtime", resultSet.getLong("Playtime"));
                    playerDataCache.put(uuid, data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Object> getPlayerData(String uuid) {
        return playerDataCache.get(uuid);
    }

    private Map<String, Object> getPlayerData() {
        return playerDataCache.get(uuid);
    }

    private long getLongValue(String key) {
        return (long) getPlayerData().get(key);
    }

    private int getIntValue(String key) {
        return (int) getPlayerData().get(key);
    }

    private void updateLongValue(String key, long value) {
        getPlayerData().put(key, value);
    }

    private String getStringValue(String key) {
        return (String) getPlayerData().get(key);
    }

    private void updateStringValue(String key, String value) {
        getPlayerData().put(key, value);
    }

    // Methods for Money
    public long getMoney() {
        return getLongValue("Money");
    }

    public void addMoney(long amount) {
        updateLongValue("Money", getMoney() + amount);
    }

    public void removeMoney(long amount) {
        updateLongValue("Money", getMoney() - amount);
    }

    public void setMoney(long amount) {
        updateLongValue("Money", amount);
    }

    // Methods for Saphirus
    public long getSaphirus() {
        return getLongValue("Saphirus");
    }

    public void addSaphirus(long amount) {
        updateLongValue("Saphirus", getSaphirus() + amount);
    }

    public void removeSaphirus(long amount) {
        updateLongValue("Saphirus", getSaphirus() - amount);
    }

    public void setSaphirus(long amount) {
        updateLongValue("Saphirus", amount);
    }

    // Methods for Gems
    public long getGems() {
        return getLongValue("Gems");
    }

    public void addGems(long amount) {
        updateLongValue("Gems", getGems() + amount);
    }

    public void removeGems(long amount) {
        updateLongValue("Gems", getGems() - amount);
    }

    public void setGems(long amount) {
        updateLongValue("Gems", amount);
    }

    // Methods for WoodCutting
    public long getWoodCutting() {
        return getLongValue("WoodCutting");
    }

    public void addWoodCutting(long amount) {
        updateLongValue("WoodCutting", getWoodCutting() + amount);
    }

    public void removeWoodCutting(long amount) {
        updateLongValue("WoodCutting", getWoodCutting() - amount);
    }

    public void setWoodCutting(long amount) {
        updateLongValue("WoodCutting", amount);
    }

    // Methods for Blocks
    public long getBlocks() {
        return getLongValue("Blocks");
    }

    public void addBlocks(long amount) {
        updateLongValue("Blocks", getBlocks() + amount);
    }

    public void removeBlocks(long amount) {
        updateLongValue("Blocks", getBlocks() - amount);
    }

    public void setBlocks(long amount) {
        updateLongValue("Blocks", amount);
    }

    // Methods for Fishing
    public long getFishing() {
        return getLongValue("Fishing");
    }

    public void addFishing(long amount) {
        updateLongValue("Fishing", getFishing() + amount);
    }

    public void removeFishing(long amount) {
        updateLongValue("Fishing", getFishing() - amount);
    }

    public void setFishing(long amount) {
        updateLongValue("Fishing", amount);
    }

    // Methods for PVE_Kills
    public long getPVEKills() {
        return getLongValue("PVE_KILLS");
    }

    public void addPVEKills(long amount) {
        updateLongValue("PVE_KILLS", getPVEKills() + amount);
    }

    public void removePVEKills(long amount) {
        updateLongValue("PVE_KILLS", getPVEKills() - amount);
    }

    public void setPVEKills(long amount) {
        updateLongValue("PVE_KILLS", amount);
    }

    // Methods for Kills
    public int getKills() {
        return getIntValue("Kills");
    }

    public void addKills(int amount) {
        updateLongValue("Kills", getKills() + amount);
    }

    public void removeKills(int amount) {
        updateLongValue("Kills", getKills() - amount);
    }

    public void setKills(int amount) {
        updateLongValue("Kills", amount);
    }

    // Methods for Deaths
    public int getDeaths() {
        return getIntValue("Deaths");
    }

    public void addDeaths(int amount) {
        updateLongValue("Deaths", getDeaths() + amount);
    }

    public void removeDeaths(int amount) {
        updateLongValue("Deaths", getDeaths() - amount);
    }

    public void setDeaths(int amount) {
        updateLongValue("Deaths", amount);
    }

    // Methods for Killstreak
    public int getKillstreak() {
        return getIntValue("Killstreak");
    }

    public void addKillstreak(int amount) {
        updateLongValue("Killstreak", getKillstreak() + amount);
    }

    public void removeKillstreak(int amount) {
        updateLongValue("Killstreak", getKillstreak() - amount);
    }

    public void setKillstreak(int amount) {
        updateLongValue("Killstreak", amount);
    }

    // Methods for Bans
    public int getBans() {
        return getIntValue("Bans");
    }

    public void addBans(int amount) {
        updateLongValue("Bans", getBans() + amount);
    }

    public void removeBans(int amount) {
        updateLongValue("Bans", getBans() - amount);
    }

    public void setBans(int amount) {
        updateLongValue("Bans", amount);
    }

    // Methods for Mutes
    public int getMutes() {
        return getIntValue("Mutes");
    }

    public void addMutes(int amount) {
        updateLongValue("Mutes", getMutes() + amount);
    }

    public void removeMutes(int amount) {
        updateLongValue("Mutes", getMutes() - amount);
    }

    public void setMutes(int amount) {
        updateLongValue("Mutes", amount);
    }

    // Methods for Warns_Total
    public int getWarnsTotal() {
        return getIntValue("Warns_Total");
    }

    public void addWarnsTotal(int amount) {
        updateLongValue("Warns_Total", getWarnsTotal() + amount);
    }

    public void removeWarnsTotal(int amount) {
        updateLongValue("Warns_Total", getWarnsTotal() - amount);
    }

    public void setWarnsTotal(int amount) {
        updateLongValue("Warns_Total", amount);
    }

    // Methods for Warns_Now
    public int getWarnsNow() {
        return getIntValue("Warns_Now");
    }

    public void addWarnsNow(int amount) {
        updateLongValue("Warns_Now", getWarnsNow() + amount);
    }

    public void removeWarnsNow(int amount) {
        updateLongValue("Warns_Now", getWarnsNow() - amount);
    }

    public void setWarnsNow(int amount) {
        updateLongValue("Warns_Now", amount);
    }

    // Methods for JoinDate
    public String getJoinDate() {
        return getStringValue("JoinDate");
    }

    public void setJoinDate(String date) {
        updateStringValue("JoinDate", date);
    }

    public boolean isLinked() {
        return (boolean) getPlayerData().get("Linked");
    }

    public void setLinked(boolean linked) {
        getPlayerData().put("Linked", linked);
    }

    // Methods for Team
    public String getTeam() {
            return (String) getPlayerData().get("Team");
    }

    public boolean inTeam() {
        String name = (String) getPlayerData().get("Team");
        return name.equalsIgnoreCase("none");
    }

    public void setTeam(String team) {
        getPlayerData().put("Team", team);
    }


    // Methods for Playtime
    public long getPlaytime() {
        return getLongValue("Playtime");
    }

    public void addPlaytime(long amount) {
        updateLongValue("Playtime", getPlaytime() + amount);
    }

    public void removePlaytime(long amount) {
        updateLongValue("Playtime", getPlaytime() - amount);
    }

    public void setPlaytime(long amount) {
        updateLongValue("Playtime", amount);
    }

    public static void updateAllValuesInDatabase() {
        String updateQuery = "UPDATE PlayerData SET " +
                "Name = ?, Team = ?, Money = ?, Saphirus = ?, Gems = ?, Linked = ?, WoodCutting = ?, " +
                "Blocks = ?, Fishing = ?, PVE_KILLS = ?, Kills = ?, Deaths = ?, Killstreak = ?, " +
                "Bans = ?, Mutes = ?, Warns_Total = ?, Warns_Now = ?, Playtime = ? " +
                "WHERE UUID = ?";

        try (Connection connection = sql.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            int batchSize = 1000; // Adjust the batch size as needed
            int count = 0;

            for (HashMap.Entry<String, HashMap<String, Object>> entry : playerDataCache.entrySet()) {
                String uuid = entry.getKey();
                Map<String, Object> data = entry.getValue();

                preparedStatement.setString(1, (String) data.get("Name"));
                preparedStatement.setString(2, (String) data.get("Team"));
                preparedStatement.setLong(3, (Long) data.get("Money"));
                preparedStatement.setLong(4, (Long) data.get("Saphirus"));
                preparedStatement.setLong(5, (Long) data.get("Gems"));
                preparedStatement.setBoolean(6, (Boolean) data.get("Linked"));
                preparedStatement.setLong(7, (Long) data.get("WoodCutting"));
                preparedStatement.setLong(8, (Long) data.get("Blocks"));
                preparedStatement.setLong(9, (Long) data.get("Fishing"));
                preparedStatement.setLong(10, (Long) data.get("PVE_KILLS"));
                preparedStatement.setInt(11, (Integer) data.get("Kills"));
                preparedStatement.setInt(12, (Integer) data.get("Deaths"));
                preparedStatement.setInt(13, (Integer) data.get("Killstreak"));
                preparedStatement.setInt(14, (Integer) data.get("Bans"));
                preparedStatement.setInt(15, (Integer) data.get("Mutes"));
                preparedStatement.setInt(16, (Integer) data.get("Warns_Total"));
                preparedStatement.setInt(17, (Integer) data.get("Warns_Now"));
                preparedStatement.setLong(18, (Long) data.get("Playtime"));
                preparedStatement.setString(19, uuid);

                preparedStatement.addBatch();

                if (++count % batchSize == 0) {
                    preparedStatement.executeBatch();
                }
            }
            preparedStatement.executeBatch(); // Execute the remaining batch

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

