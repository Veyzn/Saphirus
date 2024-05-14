package com.saphirus.utils;

import com.saphirus.main.Main;
import com.saphirus.main.MySQL;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayerCache {
    private String uuid;
    static MySQL sql = Main.sql;
    Connection connection = sql.getConnection();

    public PlayerCache(String UUID) {
        uuid = UUID;
    }

    public boolean exists() {
        String selectQuery = "SELECT UUID FROM PlayerData WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid.toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Returns true if UUID exists in the table
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // An error occurred, return false
        }
    }

    public void create(Player p) {

        try {
            // Load the JDBC driver
            // Create the SQL INSERT statement
            String insertQuery = "INSERT INTO PlayerData (UUID, Name, Clan, Money, Credits, Gems, Linked, WoodCutting, Blocks, Fishing, PVE_KILLS, Kills, Deaths, Killstreak, Bans, Mutes, Warns_Total, Warns_Now, JoinDate, Playtime) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            // Prepare the insert statement
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Define the desired date-time format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
            // Set the values for the insert statement
            preparedStatement.setString(1, p.getUniqueId().toString());
            preparedStatement.setString(2, p.getName());
            preparedStatement.setString(3, "None");
            preparedStatement.setLong(4, 0L);
            preparedStatement.setLong(5, 0L);
            preparedStatement.setLong(6, 0L);
            preparedStatement.setBoolean(7, false);
            preparedStatement.setLong(8, 0L);
            preparedStatement.setLong(9, 0L);
            preparedStatement.setLong(10, 0L);
            preparedStatement.setLong(11, 0L);
            preparedStatement.setInt(12, 0);
            preparedStatement.setInt(13, 0);
            preparedStatement.setInt(14, 0);
            preparedStatement.setInt(15, 0);
            preparedStatement.setInt(16, 0);
            preparedStatement.setInt(17, 0);
            preparedStatement.setInt(18, 0);
            preparedStatement.setString(19, currentDateTime.format(formatter));
            preparedStatement.setLong(20, 0L);
            // Execute the INSERT statement
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Player inserted successfully!");
            }

            // Close resources
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            // Load the JDBC driver
            // Create the SQL INSERT statement
            String insertQuery = "INSERT INTO Saphirus_GenPlayer (UUID, Name, Multiplier, Gens, Level, XP, ControlStation, ControlStationPlaced, ControlStation_Collected_Gems, ControlStation_Collected_Money, ControlStation_Collected_Crates, ControlStation_Collected_XP, Friends) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, p.getName());
            preparedStatement.setDouble(3, 1.00D);
            preparedStatement.setString(4, "");
            preparedStatement.setInt(5, 0);
            preparedStatement.setLong(6, 0);
            preparedStatement.setString(7, "none");
            preparedStatement.setBoolean(8, false);
            preparedStatement.setLong(9, 0L);
            preparedStatement.setLong(10, 0L);
            preparedStatement.setInt(11, 0);
            preparedStatement.setLong(12, 0L);
            preparedStatement.setString(13, "");
            // Execute the INSERT statement
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Player inserted into GenPlayer successfully!");
            }

            // Close resources
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private String getStringValueGens( String columnName) {
        try {
            String selectQuery = "SELECT " + columnName + " FROM Saphirus_GenPlayer WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString(columnName);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int getIntValueGens( String columnName) {
        try {
            String selectQuery = "SELECT " + columnName + " FROM Saphirus_GenPlayer WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(columnName);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private Long getLongValueGens( String columnName) {
        try {
            String selectQuery = "SELECT " + columnName + " FROM Saphirus_GenPlayer WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong(columnName);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    private double getDoubleValueGens( String columnName) {
        try {
            String selectQuery = "SELECT " + columnName + " FROM Saphirus_GenPlayer WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble(columnName);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.00D;
    }

    private boolean getBooleanValueGens( String columnName) {
        try {
            String selectQuery = "SELECT " + columnName + " FROM Saphirus_GenPlayer WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean(columnName);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    private List<String> getStringListValueGens( String columnName) {
        List<String> values = new ArrayList<>();
        try {
            String selectQuery = "SELECT " + columnName + " FROM Saphirus_GenPlayer WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String data = resultSet.getString(columnName);
                if (data != null && !data.isEmpty()) {
                    String[] valueArray = data.split(",");
                    for (String value : valueArray) {
                        values.add(value);
                    }
                }
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return values;
    }



    private String getStringValue( String columnName) {
        try {
            String selectQuery = "SELECT " + columnName + " FROM PlayerData WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getString(columnName);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private int getIntValue( String columnName) {
        try {
            String selectQuery = "SELECT " + columnName + " FROM PlayerData WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(columnName);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private Long getLongValue( String columnName) {
        try {
            String selectQuery = "SELECT " + columnName + " FROM PlayerData WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getLong(columnName);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    private boolean getBooleanValue( String columnName) {
        try {
            String selectQuery = "SELECT " + columnName + " FROM PlayerData WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean(columnName);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    private List<String> getStringListValue( String columnName) {
        List<String> values = new ArrayList<>();
        try {
            String selectQuery = "SELECT " + columnName + " FROM PlayerData WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String data = resultSet.getString(columnName);
                if (data != null && !data.isEmpty()) {
                    String[] valueArray = data.split(",");
                    for (String value : valueArray) {
                        values.add(value);
                    }
                }
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return values;
    }

    public String getUUID() {
        return getStringValue( "UUID");
    }

    public String getName() {
        return getStringValue( "Name");
    }

    public String getClan() {
        return getStringValue( "Clan");
    }
    public String getJoinDate() {
        return getStringValue( "JoinDate");
    }

    public long getMoney() {
        return getLongValue( "Money");
    }

    public long getCredits() {
        return getLongValue( "Credits");
    }

    public long getGems() {
        return getLongValue( "Gems");
    }

    public boolean isLinked() {
        return getBooleanValue( "Linked");
    }

    public long getWoodCutting() {
        return getLongValue( "WoodCutting");
    }

    public long getBlocks() {
        return getLongValue( "Blocks");
    }

    public long getFishing() {
        return getLongValue( "Fishing");
    }

    public long getPVE_KILLS() {
        return getLongValue( "PVE_KILLS");
    }

    public int getKills() {
        return getIntValue( "Kills");
    }

    public int getDeaths() {
        return getIntValue( "Deaths");
    }

    public int getKillstreak() {
        return getIntValue( "Killstreak");
    }

    public int getBans() {
        return getIntValue( "Bans");
    }

    public int getMutes() {
        return getIntValue( "Mutes");
    }

    public int getWarnsTotal() {
        return getIntValue( "Warns_Total");
    }

    public int getWarnsNow() {
        return getIntValue( "Warns_Now");
    }

    public long getPlaytime() {
        return getLongValue( "Playtime");
    }



    private void setStringValueGens( String columnName, String value) {
        try {
            String updateQuery = "UPDATE Saphirus_GenPlayer SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setIntValueGens( String columnName, int value) {
        try {
            String updateQuery = "UPDATE Saphirus_GenPlayer SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setDoubleValueGens( String columnName, double value) {
        try {
            String updateQuery = "UPDATE Saphirus_GenPlayer SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setDouble(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setLongValueGens( String columnName, long value) {
        try {
            String updateQuery = "UPDATE Saphirus_GenPlayer SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setLong(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void setObjectValueGens( String columnName, Object value) {
        try {
            String updateQuery = "UPDATE Saphirus_GenPlayer SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setObject(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setBooleanValueGens( String columnName, boolean value) {
        try {
            String updateQuery = "UPDATE Saphirus_GenPlayer SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setBoolean(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setStringListValueGens( String columnName, List<String> values) {
        try {
            String updateQuery = "UPDATE Saphirus_GenPlayer SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, String.join(",", values));
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setStringValue( String columnName, String value) {
        try {
            String updateQuery = "UPDATE PlayerData SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setIntValue( String columnName, int value) {
        try {
            String updateQuery = "UPDATE PlayerData SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setLongValue( String columnName, long value) {
        try {
            String updateQuery = "UPDATE PlayerData SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setLong(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void setObjectValue( String columnName, Object value) {
        try {
            String updateQuery = "UPDATE PlayerData SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setObject(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setBooleanValue( String columnName, boolean value) {
        try {
            String updateQuery = "UPDATE PlayerData SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setBoolean(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setStringListValue( String columnName, List<String> values) {
        try {
            String updateQuery = "UPDATE PlayerData SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, String.join(",", values));
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setUUID( String value) {
        setStringValue( "UUID", value);
    }

    public void setName( String value) {
        setStringValue( "Name", value);
    }

    public  void setClan( String value) {
        setStringValue( "Clan", value);
    }

    public void setMoney( long value) {
        setLongValue( "Money", value);
    }

    public void setCredits( long value) {
        setLongValue( "Credits", value);
    }

    public void setGems( long value) {
        setLongValue( "Gems", value);
    }

    public void setLinked( boolean value) {
        setBooleanValue( "Linked", value);
    }

    public void setWoodCutting( long value) {
        setLongValue( "WoodCutting", value);
    }

    public void setBlocks( long value) {
        setLongValue( "Blocks", value);
    }

    public void setFishing( long value) {
        setLongValue( "Fishing", value);
    }

    public void setPVE_KILLS( long value) {
        setLongValue( "PVE_KILLS", value);
    }

    public void setKills( int value) {
        setIntValue( "Kills", value);
    }

    public void setDeaths( int value) {
        setIntValue( "Deaths", value);
    }

    public void setKillstreak( int value) {
        setIntValue( "Killstreak", value);
    }

    public void setBans( int value) {
        setIntValue( "Bans", value);
    }

    public void setMutes( int value) {
        setIntValue( "Mutes", value);
    }

    public void setWarnsTotal( int value) {
        setIntValue( "Warns_Total", value);
    }

    public void setWarnsNow( int value) {
        setIntValue( "Warns_Now", value);
    }

    public void setPlaytime( long value) {
        setLongValue( "Playtime", value);
    }

    public void addMoney( long valueToAdd) {
        long currentMoney = getMoney();
        long newMoney = currentMoney + valueToAdd;
        setMoney( newMoney);
    }

    public void addCredits( long valueToAdd) {
        long currentCredits = getCredits();
        long newCredits = currentCredits + valueToAdd;
        setCredits( newCredits);
    }

    public void addGems( long valueToAdd) {
        long currentGems = getGems();
        long newGems = currentGems + valueToAdd;
        setGems( newGems);
    }

    public void addKills( int valueToAdd) {
        int currentKills = getKills();
        int newKills = currentKills + valueToAdd;
        setKills( newKills);
    }

    public void addDeaths( int valueToAdd) {
        int currentDeaths = getDeaths();
        int newDeaths = currentDeaths + valueToAdd;
        setDeaths( newDeaths);
    }

    public void addKillstreak( int valueToAdd) {
        int currentKillstreak = getKillstreak();
        int newKillstreak = currentKillstreak + valueToAdd;
        setKillstreak( newKillstreak);
    }

    public void addBans(int valueToAdd) {
        int currentBans = getBans();
        int newBans = currentBans + valueToAdd;
        setBans( newBans);
    }

    public void addMutes(int valueToAdd) {
        int currentMutes = getMutes();
        int newMutes = currentMutes + valueToAdd;
        setMutes( newMutes);
    }

    public void addWarnsTotal( int valueToAdd) {
        int currentWarnsTotal = getWarnsTotal();
        int newWarnsTotal = currentWarnsTotal + valueToAdd;
        setWarnsTotal( newWarnsTotal);
    }

    public void addWarnsNow( int valueToAdd) {
        int currentWarnsNow = getWarnsNow();
        int newWarnsNow = currentWarnsNow + valueToAdd;
        setWarnsNow( newWarnsNow);
    }

    public void addPlaytime( long valueToAdd) {
        long currentPlaytime = getPlaytime();
        long newPlaytime = currentPlaytime + valueToAdd;
        setPlaytime( newPlaytime);
    }

    // Remove methods for INT and BIGINT fields
    public void removeMoney( long valueToRemove) {
        long currentMoney = getMoney();
        long newMoney = Math.max(0, currentMoney - valueToRemove); // Ensure the result is non-negative
        setMoney( newMoney);
    }

    public void removeCredits( long valueToRemove) {
        long currentCredits = getCredits();
        long newCredits = Math.max(0, currentCredits - valueToRemove);
        setCredits( newCredits);
    }

    public void removeGems( long valueToRemove) {
        long currentGems = getGems();
        long newGems = Math.max(0, currentGems - valueToRemove);
        setGems( newGems);
    }

    public void removeKills( int valueToRemove) {
        int currentKills = getKills();
        int newKills = Math.max(0, currentKills - valueToRemove);
        setKills( newKills);
    }

    public void removeDeaths( int valueToRemove) {
        int currentDeaths = getDeaths();
        int newDeaths = Math.max(0, currentDeaths - valueToRemove);
        setDeaths( newDeaths);
    }

    public void removeKillstreak( int valueToRemove) {
        int currentKillstreak = getKillstreak();
        int newKillstreak = Math.max(0, currentKillstreak - valueToRemove);
        setKillstreak( newKillstreak);
    }

    public void removeBans( int valueToRemove) {
        int currentBans = getBans();
        int newBans = Math.max(0, currentBans - valueToRemove);
        setBans( newBans);
    }

    public void removeMutes( int valueToRemove) {
        int currentMutes = getMutes();
        int newMutes = Math.max(0, currentMutes - valueToRemove);
        setMutes( newMutes);
    }

    public void removeWarnsTotal( int valueToRemove) {
        int currentWarnsTotal = getWarnsTotal();
        int newWarnsTotal = Math.max(0, currentWarnsTotal - valueToRemove);
        setWarnsTotal( newWarnsTotal);
    }

    public void removeWarnsNow( int valueToRemove) {
        int currentWarnsNow = getWarnsNow();
        int newWarnsNow = Math.max(0, currentWarnsNow - valueToRemove);
        setWarnsNow( newWarnsNow);
    }

    public HashMap<String, Object> getPlayerData() {
        return sql.getPlayerData(uuid);
    }

    public HashMap<String, Object> getGenPlayerData() {
        return sql.getGenPlayerData(uuid);
    }
    public String isLinkedString() {
        if(isLinked()) {
            return "§a§a✔";

        } else return "§c§l✖";
    }

    public static void setClan(String uuid, String clan) {
        if(TempPlayerCache.data.containsKey(uuid)) {
            TempPlayerCache tpc = TempPlayerCache.data.get(uuid);
            tpc.setClan(clan);
        } else {
            PlayerCache pc = new PlayerCache(uuid);
            pc.setClan(clan);
        }

    }

    public String getUUIDGens() {
        return uuid;
    }

    public void setUUIDGens(String value) {
        setStringValueGens("UUID", value);
        this.uuid = value;
    }

    // Getter and Setter for Name
    public String getNameGens() {
        return getStringValueGens("Name");
    }

    public void setNameGens(String value) {
        setStringValueGens("Name", value);
    }

    // Getter and Setter for Multiplier
    public double getMultiplier() {
        return getDoubleValueGens("Multiplier");
    }

    public void setMultiplier(double value) {
        setDoubleValueGens("Multiplier", value);
    }

    // Getter and Setter for Gens
    public String getGens() {
        return getStringValueGens("Gens");
    }

    public void setGens(String value) {
        setStringValueGens("Gens", value);
    }

    // Getter and Setter for Level
    public int getLevel() {
        return getIntValueGens("Level");
    }

    public void setLevel(int value) {
        setIntValueGens("Level", value);
    }

    // Getter and Setter for XP
    public long getXP() {
        return getLongValueGens("XP");
    }

    public void setXP(long value) {
        setLongValueGens("XP", value);
    }

    // Getter and Setter for ControlStation
    public String getControlStation() {
        return getStringValueGens("ControlStation");
    }

    public void setControlStation(String value) {
        setStringValueGens("ControlStation", value);
    }

    // Getter and Setter for ControlStationPlaced
    public boolean isControlStationPlaced() {
        return getBooleanValueGens("ControlStationPlaced");
    }

    public void setControlStationPlaced(boolean value) {
        setBooleanValueGens("ControlStationPlaced", value);
    }

    // Getter and Setter for ControlStation_Collected_Gems
    public long getControlStation_Collected_Gems() {
        return getLongValueGens("ControlStation_Collected_Gems");
    }

    public void setControlStation_Collected_Gems(long value) {
        setLongValueGens("ControlStation_Collected_Gems", value);
    }

    // Getter and Setter for ControlStation_Collected_Money
    public long getControlStation_Collected_Money() {
        return getLongValueGens("ControlStation_Collected_Money");
    }

    public void setControlStation_Collected_Money(long value) {
        setLongValueGens("ControlStation_Collected_Money", value);
    }

    // Getter and Setter for ControlStation_Collected_Crates
    public int getControlStation_Collected_Crates() {
        return getIntValueGens("ControlStation_Collected_Crates");
    }

    public void setControlStation_Collected_Crates(int value) {
        setIntValueGens("ControlStation_Collected_Crates", value);
    }

    // Getter and Setter for ControlStation_Collected_XP
    public long getControlStation_Collected_XP() {
        return getLongValueGens("ControlStation_Collected_XP");
    }

    public void setControlStation_Collected_XP(long value) {
        setLongValueGens("ControlStation_Collected_XP", value);
    }

    // Getter and Setter for Friends
    public List<String> getFriends() {
        return getStringListValueGens("Friends");
    }

    public void setFriends(List<String> values) {
        setStringListValueGens("Friends", values);
    }

    // Add method for Friends
    public void addFriend(String friend) {
        List<String> friends = getFriends();
        friends.add(friend);
        setFriends(friends);
    }

    // Remove method for Friends
    public void removeFriend(String friend) {
        List<String> friends = getFriends();
        friends.remove(friend);
        setFriends(friends);
    }


}
