package com.saphirus.main;

import org.bukkit.Bukkit;

import java.sql.*;
import java.util.HashMap;

public class MySQL {

    private Connection connection;
    private String host, database, username, password;

    public MySQL() {
        /*this.host = "jdbc:mysql://dfw-01-5950x.pufferfish.host:3306/s1811_Saphirus";
        this.database = "s1811_Saphirus";
        this.username = "u1811_BGTiAJ4sJ5";
        this.password = "PMa0xF9+Us!b+aRNOThOccIR";*/

        this.host = Config.cfg.getString("MySQL.host");
        this.username = Config.cfg.getString("MySQL.username");
        this.password = Config.cfg.getString("MySQL.password");
        connectToDatabase();
    }

    public void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(host, username, password);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            System.err.println("Error while connecting to the database: " + e.getMessage());
        }
    }

    public void ensureConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("Database connection lost. Reconnecting...");
                connectToDatabase();
            }
        } catch (SQLException e) {
            System.err.println("Error while checking connection status: " + e.getMessage());
        }
    }

    public void disconnectFromDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("Error while disconnecting from the database: " + e.getMessage());
        }
    }
    public void deletePlayerDataTable() {
        try {
            String dropTableQuery = "DROP TABLE IF EXISTS PlayerData";
            PreparedStatement preparedStatement = connection.prepareStatement(dropTableQuery);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            System.out.println("PlayerData table dropped successfully.");
            Bukkit.broadcastMessage("Dropped");
        } catch (SQLException e) {
            System.err.println("Error while dropping PlayerData table: " + e.getMessage());
        }
    }
    public void setColumn(String uuid, String columnName, Object value) {
        try {
            String updateQuery = "UPDATE PlayerData SET " + columnName + " = ? WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setObject(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error while updating column " + columnName + " for UUID " + uuid + ": " + e.getMessage());
        }
    }

    public String getColumn(String uuid, String columnName) {
        try {
            String query = "SELECT " + columnName + " FROM PlayerData WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, uuid);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString(columnName);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error while retrieving column " + columnName + " for UUID " + uuid + ": " + e.getMessage());
        }
        return null; // Return null if data is not available
    }
    public void createTablePlayerData() {
        try {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS PlayerData (" +
                    "UUID CHAR(36) PRIMARY KEY," +
                    "Name VARCHAR(255)," +
                    "Team VARCHAR(255),"+
                    "Money BIGINT,"+
                    "Saphirus BIGINT," +
                    "Gems BIGINT," +
                    "Linked BOOLEAN," +
                    "WoodCutting BIGINT," +
                    "Blocks BIGINT," +
                    "Fishing BIGINT," +
                    "PVE_KILLS BIGINT," +
                    "Kills INT,"+
                    "Deaths INT,"+
                    "Killstreak INT,"+
                    "Bans INT,"+
                    "Mutes INT,"+
                    "Warns_Total INT,"+
                    "Warns_Now INT,"+
                    "JoinDate VARCHAR(255)," +
                    "Playtime BIGINT" +
                    ")";

            PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error while creating PlayerData table: " + e.getMessage());
        }
    }


    public void createTableBanTable() {
        try {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS Saphirus_Bans (" +
                    "UUID CHAR(36) PRIMARY KEY," +
                    "Name VARCHAR(255)," +
                    "BanEnd BIGINT,"+
                    "BanDate VARCHAR(255),"+
                    "Operator VARCHAR(255),"+
                    "Reason VARCHAR(255)"+
                    ")";

            PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error while creating Ban table: " + e.getMessage());
        }
    }

    /*"GenSlots BIGINT," +
                    "Multiplier DOUBLE," +
                    "Gens LONGTEXT," +
                    "Level INT," +
                    "XP BIGINT," +
                    "ControlStation LONGTEXT," +
                    "ControlStationPlaced BOOLEAN," +
                    "ControlStation_Collected_Gems BIGINT," +
                    "ControlStation_Collected_Money BIGINT," +
                    "ControlStation_Collected_Crates INT," +
                    "ControlStation_Collected_XP BIGINT" +
                    "Friends LONGTEXT"+*/
    public void createTableWarnTable() {
        try {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS Saphirus_Warns (" +
                    "WarnID BIGINT PRIMARY KEY AUTO_INCREMENT,"+
                    "UUID CHAR(36)," +
                    "Name VARCHAR(255)," +
                    "Amount INT,"+
                    "WarnDate VARCHAR(255),"+
                    "Operator VARCHAR(255),"+
                    "Reason VARCHAR(255)"+
                    ")";

            PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error while creating Warn table: " + e.getMessage());
        }
    }


    public void createTableMuteTable() {
        try {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS Saphirus_Mutes (" +
                    "UUID CHAR(36) PRIMARY KEY," +
                    "Name VARCHAR(255)," +
                    "MuteEnd BIGINT,"+
                    "MuteDate VARCHAR(255),"+
                    "Operator VARCHAR(255),"+
                    "Reason VARCHAR(255)"+
                    ")";

            PreparedStatement preparedStatement = connection.prepareStatement(createTableQuery);
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error while creating Mute table: " + e.getMessage());
        }
    }

    public HashMap<Integer, String> getPlacementByBalanceMoney() {
        HashMap<Integer, String> placementMap = new HashMap<>();

        try {
            String query = "SELECT UUID FROM PlayerData ORDER BY Balance_money DESC";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();


            int placement = 1;
            while (resultSet.next()) {
                String uuid = resultSet.getString("UUID");
                placementMap.put(placement, uuid);
                placement++;
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error while retrieving placement by Balance_money: " + e.getMessage());
        }

        return placementMap;
    }

    public HashMap<String, Object> getPlayerData(String uuid) {
        HashMap<String, Object> bal = new HashMap<>();
        try {
            String query = "SELECT * FROM PlayerData WHERE UUID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, uuid);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bal.put("UUID", resultSet.getString("UUID"));
                bal.put("Name", resultSet.getString("Name"));
                bal.put("Clan", resultSet.getString("Clan"));
                bal.put("Money", resultSet.getLong("Money"));
                bal.put("Credits", resultSet.getLong("Credits"));
                bal.put("Gems", resultSet.getLong("Gems"));
                bal.put("Linked", resultSet.getBoolean("Linked"));
                bal.put("WoodCutting", resultSet.getLong("WoodCutting"));
                bal.put("Blocks", resultSet.getLong("Blocks"));
                bal.put("PVE_Kills", resultSet.getLong("PVE_Kills"));
                bal.put("Fishing", resultSet.getLong("Deaths"));
                bal.put("Kills", resultSet.getInt("Kills"));
                bal.put("Deaths", resultSet.getInt("Deaths"));
                bal.put("Killstreak", resultSet.getInt("Killstreak"));
                bal.put("Bans", resultSet.getInt("Bans"));
                bal.put("Mutes", resultSet.getInt("Mutes"));
                bal.put("Warns_Total", resultSet.getInt("Warns_Total"));
                bal.put("Warns_Now", resultSet.getInt("Warns_Now"));
                bal.put("Playtime", resultSet.getLong("Playtime"));

            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            System.err.println("Error while retrieving UUIDs: " + e.getMessage());
        }

        return bal;
    }


    public Connection getConnection(){return connection;}
}
