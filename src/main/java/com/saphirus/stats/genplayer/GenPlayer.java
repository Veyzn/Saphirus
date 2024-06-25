package com.saphirus.stats.genplayer;

import com.saphirus.main.Main;
import com.saphirus.main.MySQL;
import com.saphirus.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class GenPlayer {
    private String uuid;
    static MySQL sql = Main.sql;
    Connection connection = sql.getConnection();

    public GenPlayer(String UUID) {
        this.uuid = UUID;
    }

    public void create(Player p) {
        String insertQuery = "INSERT INTO GenPlayer (UUID, GenSlots, Multiplier, Turrets, PlacedGens,Placed_Core,Core_Location) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, p.getUniqueId().toString());
            preparedStatement.setInt(2, 20); // GenSlots
            preparedStatement.setDouble(3, 1.0); // Multiplier
            preparedStatement.setInt(4, 0); // Turrets
            preparedStatement.setInt(5, 0);
            preparedStatement.setBoolean(6, false);
            preparedStatement.setString(7, "none");// PlacedGens
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists() {
        String selectQuery = "SELECT UUID FROM GenPlayer WHERE UUID = ?";
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

    private int getIntValue(String column) {
        String selectQuery = "SELECT " + column + " FROM GenPlayer WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(column);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String getStringValue(String column) {
        String selectQuery = "SELECT " + column + " FROM GenPlayer WHERE UUID = ?";
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
        return "";
    }

    private boolean getBooleanValue(String column) {
        String selectQuery = "SELECT " + column + " FROM GenPlayer WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getBoolean(column);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private double getDoubleValue(String column) {
        String selectQuery = "SELECT " + column + " FROM GenPlayer WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble(column);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private void updateIntValue(String column, int value) {
        String updateQuery = "UPDATE GenPlayer SET " + column + " = ? WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateStringValue(String column, String value) {
        String updateQuery = "UPDATE GenPlayer SET " + column + " = ? WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateBooleanValue(String column, boolean value) {
        String updateQuery = "UPDATE GenPlayer SET " + column + " = ? WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setBoolean(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateDoubleValue(String column, double value) {
        String updateQuery = "UPDATE GenPlayer SET " + column + " = ? WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setDouble(1, value);
            preparedStatement.setString(2, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Methods for GenSlots
    public int getGenSlots() {
        return getIntValue("GenSlots");
    }

    public void addGenSlots(int amount) {
        updateIntValue("GenSlots", getGenSlots() + amount);
    }

    public void removeGenSlots(int amount) {
        updateIntValue("GenSlots", getGenSlots() - amount);
    }

    public void setGenSlots(int amount) {
        updateIntValue("GenSlots", amount);
    }

    // Methods for Multiplier
    public double getMultiplier() {
        return getDoubleValue("Multiplier");
    }

    public void addMultiplier(double amount) {
        updateDoubleValue("Multiplier", getMultiplier() + amount);
    }

    public void removeMultiplier(double amount) {
        updateDoubleValue("Multiplier", getMultiplier() - amount);
    }

    public void setMultiplier(double amount) {
        updateDoubleValue("Multiplier", amount);
    }

    // Methods for Turrets
    public int getTurrets() {
        return getIntValue("Turrets");
    }

    public void addTurrets(int amount) {
        updateIntValue("Turrets", getTurrets() + amount);
    }

    public void removeTurrets(int amount) {
        updateIntValue("Turrets", getTurrets() - amount);
    }

    public void setTurrets(int amount) {
        updateIntValue("Turrets", amount);
    }

    //METHOD FOR Placed_Core
    public boolean isPlaced() {return getBooleanValue("Placed_Core");}
    public void updatePlacedCore(boolean value) {updateBooleanValue("Placed_Core", value);}

    //METHOD FOR CORE_LOCATION
    public String getLocationString() {return getStringValue("Core_Location");}
    public void setLocation(Location loc) {updateStringValue("Core_Location", LocationUtil.locationToString(loc));}

    // Methods for PlacedGens
    public int getPlacedGens() {
        return getIntValue("PlacedGens");
    }

    public void addPlacedGens(int amount) {
        updateIntValue("PlacedGens", getPlacedGens() + amount);
    }

    public void removePlacedGens(int amount) {
        updateIntValue("PlacedGens", getPlacedGens() - amount);
    }

    public void setPlacedGens(int amount) {
        updateIntValue("PlacedGens", amount);
    }

    // Method to get all GenPlayer data as a HashMap
    public HashMap<String, Object> getAllData() {
        HashMap<String, Object> data = new HashMap<>();
        String selectQuery = "SELECT * FROM GenPlayer WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    data.put("UUID", resultSet.getString("UUID"));
                    data.put("GenSlots", resultSet.getInt("GenSlots"));
                    data.put("Multiplier", resultSet.getDouble("Multiplier"));
                    data.put("Turrets", resultSet.getInt("Turrets"));
                    data.put("PlacedGens", resultSet.getInt("PlacedGens"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Method to update all values in one query
    public void updateAllValues(HashMap<String, Object> values) {
        String updateQuery = "UPDATE GenPlayer SET GenSlots = ?, Multiplier = ?, Turrets = ?, PlacedGens = ?, Placed_Core = ?, Core_Location = ? WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, (Integer) values.get("GenSlots"));
            preparedStatement.setDouble(2, (Double) values.get("Multiplier"));
            preparedStatement.setInt(3, (Integer) values.get("Turrets"));
            preparedStatement.setInt(4, (Integer) values.get("PlacedGens"));
            preparedStatement.setBoolean(4, (Boolean) values.get("PlacedCore"));
            preparedStatement.setString(4, (String) values.get("Core_Location"));

            preparedStatement.setString(5, uuid);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getGenPlayerData() {
        HashMap<String, Object> genPlayerData = new HashMap<>();

        String selectQuery = "SELECT * FROM GenPlayer WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    genPlayerData.put("UUID", resultSet.getString("UUID"));
                    genPlayerData.put("GenSlots", resultSet.getInt("GenSlots"));
                    genPlayerData.put("Multiplier", resultSet.getDouble("Multiplier"));
                    genPlayerData.put("Turrets", resultSet.getInt("Turrets"));
                    genPlayerData.put("PlacedGens", resultSet.getInt("PlacedGens"));
                    genPlayerData.put("PlacedCore", resultSet.getBoolean("Placed_Core"));
                    genPlayerData.put("Core_Location", resultSet.getString("PlacedGens"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genPlayerData;
    }
}
