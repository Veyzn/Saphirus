package com.saphirus.manager;

import com.saphirus.main.Data;
import com.saphirus.main.Main;
import com.saphirus.main.MySQL;
import com.saphirus.utils.PlayerCache;
import com.saphirus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PunishManager {


    private String uuid;
    static MySQL sql = Main.sql;
    Connection connection = sql.getConnection();

    public PunishManager(String UUID) {
        uuid = UUID;
    }

    public boolean isBanned() {
        String selectQuery = "SELECT * FROM Saphirus_Bans WHERE UUID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            String playerUUID = uuid;
            preparedStatement.setString(1, playerUUID);

            // Execute the select statement
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    long banEnd = resultSet.getLong("BanEnd");
                    long currentTimeMillis = System.currentTimeMillis();

                    if (banEnd > currentTimeMillis) {
                        return true;  // Player is still banned
                    } else {
                        // Ban has expired, remove the record from the table
                        String deleteQuery = "DELETE FROM Saphirus_Bans WHERE UUID = ?";
                        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                            deleteStatement.setString(1, playerUUID);
                            deleteStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            // Handle any errors during deletion
                        }

                        return false;  // Player is not banned anymore
                    }
                } else {
                    return false;  // Player is not in the bans table
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean isMuted() {
        String selectQuery = "SELECT * FROM Saphirus_Mutes WHERE UUID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            String playerUUID = uuid;
            preparedStatement.setString(1, playerUUID);

            // Execute the select statement
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    long banEnd = resultSet.getLong("MuteEnd");
                    long currentTimeMillis = System.currentTimeMillis();

                    if (banEnd > currentTimeMillis) {
                        return true;  // Player is still banned
                    } else {
                        // Ban has expired, remove the record from the table
                        String deleteQuery = "DELETE FROM Saphirus_Mutes WHERE UUID = ?";
                        try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                            deleteStatement.setString(1, playerUUID);
                            deleteStatement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                            // Handle any errors during deletion
                        }

                        return false;  // Player is not banned anymore
                    }
                } else {
                    return false;  // Player is not in the bans table
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void addBan(String p, Player opeartor, Long banend, String reason) {
        try {
            // Load the JDBC driver
            // Create the SQL INSERT statement
            String insertQuery = "INSERT INTO Saphirus_Bans (UUID,Name,BanEnd,BanDate,Operator,Reason) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            // Prepare the insert statement
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            LocalDateTime currentDateTime = LocalDateTime.now();
            PlayerCache pc = new PlayerCache(p);

            // Define the desired date-time format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
            // Set the values for the insert statement
            preparedStatement.setString(1, p);
            preparedStatement.setString(2, pc.getName());
            preparedStatement.setLong(3, banend);
            preparedStatement.setString(4, currentDateTime.format(formatter));
            preparedStatement.setString(5, opeartor.getName());
            preparedStatement.setString(6, reason);
            // Execute the INSERT statement
            int rowsInserted = preparedStatement.executeUpdate();

            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage("  §4§l" + Utils.fancy("BAN HAMMER"));
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage("    §f§l» §c" + Utils.fancy("Punished: ") + "§f" + Utils.fancy(pc.getName()));
            Bukkit.broadcastMessage("    §f§l» §c" + Utils.fancy("Staff: ") + "§f" + Utils.fancy(opeartor.getName()));
            Bukkit.broadcastMessage("    §f§l» §c" + Utils.fancy("Reason: ") + "§f" + Utils.fancy(reason));
            Bukkit.broadcastMessage(" ");

            Bukkit.broadcastMessage("    §f§l» §c" + Utils.fancy("Duration: ") + "§f" + Utils.fancy(Utils.calculateRemainingTime(banend)));
            Bukkit.broadcastMessage(" ");


            if (rowsInserted > 0) {
                System.out.println("Player banned successfully!");

                if(Bukkit.getPlayer(UUID.fromString(p)) != null) {
                    Player pa = Bukkit.getPlayer(UUID.fromString(p));
                    pa.kickPlayer(Data.getHeader() + "\n\n" +
                            "§fYou have been banned!\n" +
                            "§cReason: §f" + reason + "\n" +
                            "§cBanned by: §f" + opeartor.getName() + "\n" +
                            "§cDuration: §f" + Utils.calculateRemainingTime(banend) + "\n" +
                            "\n" +
                            "§cTry to appeal on our discord: §fdiscord.saphirus.com");
                }
            }

            // Close resources
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public void addMute(Player p, Player opeartor, Long muteend, String reason) {
        try {
            // Load the JDBC driver
            // Create the SQL INSERT statement
            String insertQuery = "INSERT INTO Saphirus_Mutes (UUID,Name,MuteEnd,MuteDate,Operator,Reason) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            // Prepare the insert statement
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Define the desired date-time format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
            // Set the values for the insert statement
            preparedStatement.setString(1, p.getUniqueId().toString());
            preparedStatement.setString(2, p.getName());
            preparedStatement.setLong(3, muteend);
            preparedStatement.setString(4, currentDateTime.format(formatter));
            preparedStatement.setString(5, opeartor.getName());
            preparedStatement.setString(6, reason);
            // Execute the INSERT statement
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Player mute successfully!");
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage("  §5§l" + Utils.fancy("MUTE"));
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage("    §f§l» §c" + Utils.fancy("Punished: ") + "§f" + Utils.fancy(p.getName()));
                Bukkit.broadcastMessage("    §f§l» §c" + Utils.fancy("Staff: ") + "§f" + Utils.fancy(opeartor.getName()));
                Bukkit.broadcastMessage("    §f§l» §c" + Utils.fancy("Reason: ") + "§f" + Utils.fancy(reason));
                Bukkit.broadcastMessage(" ");

                Bukkit.broadcastMessage("    §f§l» §c" + Utils.fancy("Duration: ") + "§f" + Utils.fancy(Utils.calculateRemainingTime(muteend)));
                Bukkit.broadcastMessage(" ");

            }

            // Close resources
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addWarn(Player p, Player opeartor, int amount, String reason) {
        try {
            // Load the JDBC driver
            // Create the SQL INSERT statement
            String insertQuery = "INSERT INTO Saphirus_Warns (UUID,Name,Amount,WarnDate,Operator,Reason) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            // Prepare the insert statement
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Define the desired date-time format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
            // Set the values for the insert statement
            preparedStatement.setString(1, p.getUniqueId().toString());
            preparedStatement.setString(2, p.getName());
            preparedStatement.setInt(3, amount);
            preparedStatement.setString(4, currentDateTime.format(formatter));
            preparedStatement.setString(5, p.getName());
            preparedStatement.setString(6, reason);
            // Execute the INSERT statement
            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Player warned successfully!");

                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage("  §6§l" + Utils.fancy("WARN"));
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage("    §f§l» §c" + Utils.fancy("Punished: ") + "§f" + Utils.fancy(p.getName()));
                Bukkit.broadcastMessage("    §f§l» §c" + Utils.fancy("Staff: ") + "§f" + Utils.fancy(opeartor.getName()));
                Bukkit.broadcastMessage("    §f§l» §c" + Utils.fancy("Reason: ") + "§f" + Utils.fancy(reason));
                Bukkit.broadcastMessage("    §f§l» §c" + Utils.fancy("Warns: ") + "§f" + amount);
                Bukkit.broadcastMessage(" ");

                Bukkit.broadcastMessage(" ");


            }

            // Close resources
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public int getWarns() {

            // Load the JDBC driver
            // Create the SQL INSERT statement
        String query = "SELECT EXISTS (SELECT 1 FROM your_table_name WHERE UUID = ?) AS UUIDExists, " +
                "COUNT(*) AS TotalAmount FROM your_table_name WHERE UUID = ?";

            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {


                // Set the UUID value for counting occurrences
                String playerUUID = uuid;
                preparedStatement.setString(1, playerUUID);

                // Execute the count query
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        // Retrieve the existence result
                        boolean uuidExists = resultSet.getBoolean("UUIDExists");
                        if (uuidExists) {
                            // Retrieve the total count
                            return resultSet.getInt("TotalAmount");
                        } else {
                            return 0;
                        }
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        return 0;
    }

    public Map<String, Object> getBanInformation() {
        Map<String, Object> uuidInfo = new HashMap<>();

        // SQL query to retrieve information about a UUID
        String query = "SELECT * FROM Saphirus_Bans WHERE UUID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the UUID value for the query
            preparedStatement.setString(1, uuid);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve information from the result set and store it in the map
                    uuidInfo.put("UUID", resultSet.getString("UUID"));
                    uuidInfo.put("Name", resultSet.getString("Name"));
                    uuidInfo.put("BanEnd", resultSet.getLong("BanEnd"));
                    uuidInfo.put("BanDate", resultSet.getString("BanDate"));
                    uuidInfo.put("Operator", resultSet.getString("Operator"));
                    uuidInfo.put("Reason", resultSet.getString("Reason"));
                } else {
                    System.out.println("Player with UUID " + uuid + " not found in the table.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return uuidInfo;
    }

    public Map<String, Object> getMuteInfo() {
        Map<String, Object> uuidInfo = new HashMap<>();

        // SQL query to retrieve information about a UUID from the Mute table
        String query = "SELECT * FROM Saphirus_Mutes WHERE UUID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the UUID value for the query
            preparedStatement.setString(1, uuid);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve information from the result set and store it in the map
                    uuidInfo.put("UUID", resultSet.getString("UUID"));
                    uuidInfo.put("Name", resultSet.getString("Name"));
                    uuidInfo.put("MuteEnd", resultSet.getLong("MuteEnd"));
                    uuidInfo.put("MuteDate", resultSet.getString("MuteDate"));
                    uuidInfo.put("Operator", resultSet.getString("Operator"));
                    uuidInfo.put("Reason", resultSet.getString("Reason"));
                } else {
                    System.out.println("Player with UUID " + uuid + " not found in the Mute table.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return uuidInfo;
    }

    public Map<Long, Map<String, Object>> getWarnsInformation() {
        Map<Long, Map<String, Object>> warnsInfo = new HashMap<>();

        // SQL query to count and retrieve information about warns for a UUID
        String query = "SELECT * FROM Saphirus_Warns WHERE UUID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the UUID value for the query
            preparedStatement.setString(1, uuid);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    // Retrieve information from the result set and store it in the map
                    long warnID = resultSet.getLong("WarnID");
                    Map<String, Object> warnInfo = new HashMap<>();
                    warnInfo.put("UUID", resultSet.getString("UUID"));
                    warnInfo.put("Name", resultSet.getString("Name"));
                    warnInfo.put("Amount", resultSet.getInt("Amount"));
                    warnInfo.put("WarnDate", resultSet.getString("WarnDate"));
                    warnInfo.put("Operator", resultSet.getString("Operator"));
                    warnInfo.put("Reason", resultSet.getString("Reason"));

                    warnsInfo.put(warnID, warnInfo);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return warnsInfo;
    }

    public void removePlayerBan() {
        // SQL query to remove the player from Saphirus_Bans table
        String deleteQuery = "DELETE FROM Saphirus_Bans WHERE UUID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            // Set the UUID value for the delete query
            preparedStatement.setString(1, uuid);

            // Execute the delete statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Player with UUID " + uuid + " removed from Saphirus_Bans table.");
            } else {
                System.out.println("Player with UUID " + uuid + " not found in Saphirus_Bans table.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePlayerMute() {
        // SQL query to remove the player from Saphirus_Bans table
        String deleteQuery = "DELETE FROM Saphirus_Mutes WHERE UUID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            // Set the UUID value for the delete query
            preparedStatement.setString(1, uuid);

            // Execute the delete statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Player with UUID " + uuid + " removed from Saphirus_Mutes table.");
            } else {
                System.out.println("Player with UUID " + uuid + " not found in Saphirus_Mutes table.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteAllPlayerWarns() {
        // SQL query to delete all warns for a specific UUID from Saphirus_Warns table
        String deleteAllQuery = "DELETE FROM Saphirus_Warns WHERE UUID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteAllQuery)) {
            // Set the UUID value for the delete query
            preparedStatement.setString(1, uuid);

            // Execute the delete statement
            int rowsAffected = preparedStatement.executeUpdate();

            System.out.println(rowsAffected + " warns for UUID " + uuid + " deleted from Saphirus_Warns table.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWarnByID(long warnID) {
        // SQL query to delete a specific WarnID from Saphirus_Warns table
        String deleteByIDQuery = "DELETE FROM Saphirus_Warns WHERE WarnID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteByIDQuery)) {
            // Set the WarnID value for the delete query
            preparedStatement.setLong(1, warnID);

            // Execute the delete statement
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Warn with ID " + warnID + " deleted from Saphirus_Warns table.");
            } else {
                System.out.println("Warn with ID " + warnID + " not found in Saphirus_Warns table.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean doesWarnIDExist(long warnID) {
        String selectQuery = "SELECT * FROM Saphirus_Warns WHERE WarnID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, warnID);

            // Execute the select statement
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();  // Return true if the WarnID exists, false otherwise
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle any SQL exceptions
        }

        return false;
    }
    //removeBan
    //removeWarns
    //removeMute
    

}
