package com.saphirus.utils;

import com.saphirus.main.Main;
import com.saphirus.main.MySQL;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import java.util.Map;

public class PlayerCache {
    private String uuid;
    static MySQL sql = Main.sql;
    Connection connection = sql.getConnection();

    public PlayerCache(String UUID) {
        this.uuid = UUID;
    }

    public void create(Player p) {
        String insertQuery = "INSERT INTO PlayerData (UUID, Name, Team, Money, Saphirus, Gems, Linked, WoodCutting, Blocks, Fishing, PVE_KILLS, Kills, Deaths, Killstreak, Bans, Mutes, Warns_Total, Warns_Now, JoinDate, Playtime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, p.getUniqueId().toString());
            preparedStatement.setString(2, p.getName());
            preparedStatement.setString(3, "none");
            preparedStatement.setLong(4, 0); // Money
            preparedStatement.setLong(5, 0); // Saphirus
            preparedStatement.setLong(6, 0); // Gems
            preparedStatement.setBoolean(7, false); // Linked
            preparedStatement.setLong(8, 0); // WoodCutting
            preparedStatement.setLong(9, 0); // Blocks
            preparedStatement.setLong(10, 0); // Fishing
            preparedStatement.setLong(11, 0); // PVE_KILLS
            preparedStatement.setInt(12, 0); // Kills
            preparedStatement.setInt(13, 0); // Deaths
            preparedStatement.setInt(14, 0); // Killstreak
            preparedStatement.setInt(15, 0); // Bans
            preparedStatement.setInt(16, 0); // Mutes
            preparedStatement.setInt(17, 0); // Warns_Total
            preparedStatement.setInt(18, 0); // Warns_Now
            preparedStatement.setString(19, ""); // JoinDate
            preparedStatement.setLong(20, 0); // Playtime
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public boolean exists() {
        String selectQuery = "SELECT UUID FROM PlayerData WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private long getLongValue(String column) {
        String selectQuery = "SELECT " + column + " FROM PlayerData WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong(column);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void updateLongValue(String column, long value) {
        String updateQuery = "UPDATE PlayerData SET " + column + " = ? WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setLong(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String getStringValue(String column) {
        String selectQuery = "SELECT " + column + " FROM PlayerData WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(column);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void updateStringValue(String column, String value) {
        String updateQuery = "UPDATE PlayerData SET " + column + " = ? WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isLinked() {
        String selectQuery = "SELECT Linked FROM PlayerData WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean("Linked");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default value if not found or error occurs
    }

    public void setLinked(boolean linked) {
        String updateQuery = "UPDATE PlayerData SET Linked = ? WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setBoolean(1, linked);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Methods for Money
    public long getMoney() {
        return getLongValue("Money");
    }
    public String getName() {
        return getStringValue("Name");
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
    public void setName(String name) {
        updateStringValue("Name", name);
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
        return (int) getLongValue("Kills");
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
        return (int) getLongValue("Deaths");
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
        return (int) getLongValue("Killstreak");
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
        return (int) getLongValue("Bans");
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
        return (int) getLongValue("Mutes");
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
        return (int) getLongValue("Warns_Total");
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
        return (int) getLongValue("Warns_Now");
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
    public String getTeam() {
        return getStringValue("Team");
    }

    public void setTeam(String date) {
        updateStringValue("Team", date);
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

    // Method to get all player data as a HashMap
    public HashMap<String, Object> getAllData() {
        HashMap<String, Object> data = new HashMap<>();
        String selectQuery = "SELECT * FROM PlayerData WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
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
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Method to update all values in one query
    public void updateAllValues(HashMap<String, Object> values) {
        String updateQuery = "UPDATE PlayerData SET Name = ?, Team = ?, Money = ?, Saphirus = ?, Gems = ?, Linked = ?, WoodCutting = ?, Blocks = ?, Fishing = ?, PVE_KILLS = ?, Kills = ?, Deaths = ?, Killstreak = ?, Bans = ?, Mutes = ?, Warns_Total = ?, Warns_Now = ?, JoinDate = ?, Playtime = ? WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, (String) values.get("Name"));
            preparedStatement.setString(2, (String) values.get("Team"));
            preparedStatement.setLong(3, (Long) values.get("Money"));
            preparedStatement.setLong(4, (Long) values.get("Saphirus"));
            preparedStatement.setLong(5, (Long) values.get("Gems"));
            preparedStatement.setBoolean(6, (Boolean) values.get("Linked"));
            preparedStatement.setLong(7, (Long) values.get("WoodCutting"));
            preparedStatement.setLong(8, (Long) values.get("Blocks"));
            preparedStatement.setLong(9, (Long) values.get("Fishing"));
            preparedStatement.setLong(10, (Long) values.get("PVE_KILLS"));
            preparedStatement.setInt(11, (Integer) values.get("Kills"));
            preparedStatement.setInt(12, (Integer) values.get("Deaths"));
            preparedStatement.setInt(13, (Integer) values.get("Killstreak"));
            preparedStatement.setInt(14, (Integer) values.get("Bans"));
            preparedStatement.setInt(15, (Integer) values.get("Mutes"));
            preparedStatement.setInt(16, (Integer) values.get("Warns_Total"));
            preparedStatement.setInt(17, (Integer) values.get("Warns_Now"));
            preparedStatement.setString(18, (String) values.get("JoinDate"));
            preparedStatement.setLong(19, (Long) values.get("Playtime"));
            preparedStatement.setString(20, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getPlayerData() {
        HashMap<String, Object> playerData = new HashMap<>();

        if (TempPlayerCache.isPlayerLoaded(uuid)) {
            // If player is loaded in TempPlayerCache, get data from there
            playerData = TempPlayerCache.getPlayerData(uuid);
        } else {
            // If player is not loaded in TempPlayerCache, retrieve data from the database
            String selectQuery = "SELECT * FROM PlayerData WHERE UUID = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, uuid);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        playerData.put("UUID", resultSet.getString("UUID"));
                        playerData.put("Name", resultSet.getString("Name"));
                        playerData.put("Team", resultSet.getString("Team"));
                        playerData.put("Money", resultSet.getLong("Money"));
                        playerData.put("Saphirus", resultSet.getLong("Saphirus"));
                        playerData.put("Gems", resultSet.getLong("Gems"));
                        playerData.put("Linked", resultSet.getBoolean("Linked"));
                        playerData.put("WoodCutting", resultSet.getLong("WoodCutting"));
                        playerData.put("Blocks", resultSet.getLong("Blocks"));
                        playerData.put("Fishing", resultSet.getLong("Fishing"));
                        playerData.put("PVE_KILLS", resultSet.getLong("PVE_KILLS"));
                        playerData.put("Kills", resultSet.getInt("Kills"));
                        playerData.put("Deaths", resultSet.getInt("Deaths"));
                        playerData.put("Killstreak", resultSet.getInt("Killstreak"));
                        playerData.put("Bans", resultSet.getInt("Bans"));
                        playerData.put("Mutes", resultSet.getInt("Mutes"));
                        playerData.put("Warns_Total", resultSet.getInt("Warns_Total"));
                        playerData.put("Warns_Now", resultSet.getInt("Warns_Now"));
                        playerData.put("JoinDate", resultSet.getString("JoinDate"));
                        playerData.put("Playtime", resultSet.getLong("Playtime"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return playerData;
    }
}
