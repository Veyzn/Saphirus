package com.saphirus.stats.genplayer;

import com.saphirus.main.Main;
import com.saphirus.main.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TempGenPlayer {
    private static final HashMap<String, HashMap<String, Object>> genPlayerDataCache = new HashMap<>();
    static MySQL sql = Main.sql;
    Connection connection = sql.getConnection();

    private String uuid;

    public TempGenPlayer(String UUID) {
        this.uuid = UUID;
        if (!genPlayerDataCache.containsKey(UUID)) {
            loadGenPlayerDataFromDatabase();
        }
    }

    public void unloadGenPlayer() {
        if (genPlayerDataCache.containsKey(uuid)) {
            GenPlayer gpc = new GenPlayer(uuid);
            gpc.updateAllValues(genPlayerDataCache.get(uuid));
            genPlayerDataCache.remove(uuid);
        }
    }

    public static boolean isGenPlayerLoaded(String uuid) {
        return genPlayerDataCache.containsKey(uuid);
    }

    private void loadGenPlayerDataFromDatabase() {
        String selectQuery = "SELECT * FROM GenPlayer WHERE UUID = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, uuid);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("UUID", resultSet.getString("UUID"));
                    data.put("GenSlots", resultSet.getInt("GenSlots"));
                    data.put("Multiplier", resultSet.getDouble("Multiplier"));
                    data.put("Turrets", resultSet.getInt("Turrets"));
                    data.put("PlacedGens", resultSet.getInt("PlacedGens"));
                    genPlayerDataCache.put(uuid, data);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Object> getGenPlayerData(String uuid) {
        return genPlayerDataCache.get(uuid);
    }

    private Map<String, Object> getGenPlayerData() {
        return genPlayerDataCache.get(uuid);
    }

    private int getIntValue(String key) {
        return (int) getGenPlayerData().get(key);
    }

    private double getDoubleValue(String key) {
        return (double) getGenPlayerData().get(key);
    }

    private void updateIntValue(String key, int value) {
        getGenPlayerData().put(key, value);
    }

    private void updateDoubleValue(String key, double value) {
        getGenPlayerData().put(key, value);
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

    public static void updateAllValuesInDatabase() {
        String updateQuery = "UPDATE GenPlayer SET " +
                "GenSlots = ?, Multiplier = ?, Turrets = ?, PlacedGens = ? " +
                "WHERE UUID = ?";

        try (PreparedStatement preparedStatement = Main.sql.getConnection().prepareStatement(updateQuery)) {

            int batchSize = 1000; // Adjust the batch size as needed
            int count = 0;

            for (HashMap.Entry<String, HashMap<String, Object>> entry : genPlayerDataCache.entrySet()) {
                String uuid = entry.getKey();
                Map<String, Object> data = entry.getValue();

                preparedStatement.setInt(1, (Integer) data.get("GenSlots"));
                preparedStatement.setDouble(2, (Double) data.get("Multiplier"));
                preparedStatement.setInt(3, (Integer) data.get("Turrets"));
                preparedStatement.setInt(4, (Integer) data.get("PlacedGens"));
                preparedStatement.setString(5, uuid);

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
