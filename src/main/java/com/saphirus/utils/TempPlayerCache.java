package com.saphirus.utils;

import com.saphirus.main.Main;
import com.saphirus.main.MySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TempPlayerCache {

    public static HashMap<String, TempPlayerCache> data = new HashMap<>();
    static MySQL sql = Main.sql;
    static Connection connection = sql.getConnection();
    // Fields
    private String UUID;
    private String name;
    private String clan;
    private long money;
    private long credits;
    private long gems;
    private boolean linked;
    private long woodCutting;
    private long blocks;
    private long fishing;
    private long pveKills;
    private int kills;
    private int deaths;
    private int killstreak;
    private int bans;
    private int mutes;
    private int warnsTotal;
    private int warnsNow;
    private long playtime;
    private double multiplier;
    private String gens;
    private int level;
    private long xp;
    private String controlstation;
    private boolean controlstation_placed;
    private long collected_gems;
    private long collected_money;
    private int collected_crates;
    private long collected_xp;
    private String friends;

    // Constructors

    public TempPlayerCache(String uuid) {
        UUID = uuid;
        if(!isDataLoaded()) {
            PlayerCache pc = new PlayerCache(UUID);
            HashMap<String, Object> data = pc.getPlayerData();
            HashMap<String, Object> genPlayerData = pc.getGenPlayerData();
            name = (String) data.get("Name");
            clan = (String) data.get("Clan");
            money = (long) data.get("Money");
            credits = (long) data.get("Credits");
            gems = (long) data.get("Gems");
            linked = (boolean) data.get("Linked");
            woodCutting = (long) data.get("WoodCutting");
            blocks = (long) data.get("Blocks");
            pveKills = (long) data.get("PVE_Kills");
            fishing = (long) data.get("Fishing");
            kills = (int) data.get("Kills");
            deaths = (int) data.get("Deaths");
            killstreak = (int) data.get("Killstreak");
            bans = (int) data.get("Bans");
            mutes = (int) data.get("Mutes");
            warnsTotal = (int) data.get("Warns_Total");
            warnsNow = (int) data.get("Warns_Now");
            playtime = (long) data.get("Playtime");

            multiplier = (double) genPlayerData.get("Multiplier");
            gens = (String) genPlayerData.get("Gens");
            level = (int) genPlayerData.get("Level");
            xp = (long) genPlayerData.get("XP");
            controlstation = (String) genPlayerData.get("ControlStation");
            controlstation_placed = (boolean) genPlayerData.get("ControlStationPlaced");
            collected_gems = (long) genPlayerData.get("ControlStation_Collected_Gems");
            collected_money = (long) genPlayerData.get("ControlStation_Collected_Money");
            collected_crates = (int) genPlayerData.get("ControlStation_Collected_Crates");
            collected_xp = (long) genPlayerData.get("ControlStation_Collected_XP");
        }
    }


    public void loadData() {
        data.put(getUUID(), this);
    }

    public boolean isDataLoaded() {return data.containsKey(getUUID());}

    public void unloadData() {
        updateAllValuesPlayer();
        data.remove(getUUID());
    }

    // Getters and Setters

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClan() {
        return clan;
    }
    public boolean inClan() {return !clan.equalsIgnoreCase("none");}

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    // Getter and Setter for gens
    public String getGens() {
        return gens;
    }

    public void setGens(String gens) {
        this.gens = gens;
    }

    // Getter and Setter for level
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    // Getter and Setter for xp
    public long getXp() {
        return xp;
    }

    public void setXp(long xp) {
        this.xp = xp;
    }

    // Getter and Setter for controlstation
    public String getControlstation() {
        return controlstation;
    }

    public void setControlstation(String controlstation) {
        this.controlstation = controlstation;
    }

    // Getter and Setter for controlstation_placed
    public boolean isControlstation_placed() {
        return controlstation_placed;
    }

    public void setControlstation_placed(boolean controlstation_placed) {
        this.controlstation_placed = controlstation_placed;
    }

    // Getter and Setter for collected_gems
    public long getCollected_gems() {
        return collected_gems;
    }

    public void setCollected_gems(long collected_gems) {
        this.collected_gems = collected_gems;
    }

    // Add method for collected_gems
    public void addCollected_gems(long amount) {
        this.collected_gems += amount;
    }

    // Remove method for collected_gems
    public void removeCollected_gems(long amount) {
        this.collected_gems = Math.max(0, this.collected_gems - amount);
    }

    // Getter and Setter for collected_money
    public long getCollected_money() {
        return collected_money;
    }

    public void setCollected_money(long collected_money) {
        this.collected_money = collected_money;
    }

    // Add method for collected_money
    public void addCollected_money(long amount) {
        this.collected_money += amount;
    }

    // Remove method for collected_money
    public void removeCollected_money(long amount) {
        this.collected_money = Math.max(0, this.collected_money - amount);
    }

    // Getter and Setter for collected_crates
    public int getCollected_crates() {
        return collected_crates;
    }

    public void setCollected_crates(int collected_crates) {
        this.collected_crates = collected_crates;
    }

    // Add method for collected_crates
    public void addCollected_crates(int amount) {
        this.collected_crates += amount;
    }

    // Remove method for collected_crates
    public void removeCollected_crates(int amount) {
        this.collected_crates = Math.max(0, this.collected_crates - amount);
    }

    // Getter and Setter for collected_xp
    public long getCollected_xp() {
        return collected_xp;
    }

    public void setCollected_xp(long collected_xp) {
        this.collected_xp = collected_xp;
    }

    // Add method for collected_xp
    public void addCollected_xp(long amount) {
        this.collected_xp += amount;
    }

    // Remove method for collected_xp
    public void removeCollected_xp(long amount) {
        this.collected_xp = Math.max(0, this.collected_xp - amount);
    }

    public void setClan(String clan) {
        this.clan = clan;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public long getCredits() {
        return credits;
    }

    public void setCredits(long credits) {
        this.credits = credits;
    }

    public long getGems() {
        return gems;
    }

    public void setGems(long gems) {
        this.gems = gems;
    }

    public boolean isLinked() {
        return linked;
    }


    public String isLinkedString() {
        if(isLinked()) {
            return "§a§a✔";

        } else return "§c§l✖";
    }

    public void setLinked(boolean linked) {
        this.linked = linked;
    }

    public long getWoodCutting() {
        return woodCutting;
    }

    public void setWoodCutting(long woodCutting) {
        this.woodCutting = woodCutting;
    }

    public long getBlocks() {
        return blocks;
    }

    public void setBlocks(long blocks) {
        this.blocks = blocks;
    }

    public long getFishing() {
        return fishing;
    }

    public void setFishing(long fishing) {
        this.fishing = fishing;
    }

    public long getPveKills() {
        return pveKills;
    }

    public void setPveKills(long pveKills) {
        this.pveKills = pveKills;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getKillstreak() {
        return killstreak;
    }

    public void setKillstreak(int killstreak) {
        this.killstreak = killstreak;
    }

    public int getBans() {
        return bans;
    }

    public void setBans(int bans) {
        this.bans = bans;
    }

    public int getMutes() {
        return mutes;
    }

    public void setMutes(int mutes) {
        this.mutes = mutes;
    }

    public int getWarnsTotal() {
        return warnsTotal;
    }

    public void setWarnsTotal(int warnsTotal) {
        this.warnsTotal = warnsTotal;
    }

    public int getWarnsNow() {
        return warnsNow;
    }

    public void setWarnsNow(int warnsNow) {
        this.warnsNow = warnsNow;
    }

    public long getPlaytime() {
        return playtime;
    }

    public void setPlaytime(long playtime) {
        this.playtime = playtime;
    }

    // Add and remove methods

    public void addMoney(long amount) {
        setMoney(getMoney() + amount);
    }

    public void removeMoney(long amount) {
        setMoney(Math.max(0, getMoney() - amount));
    }

    public void addCredits(long amount) {
        setCredits(getCredits() + amount);
    }

    public void removeCredits(long amount) {
        setCredits(Math.max(0, getCredits() - amount));
    }

    public void addGems(long amount) {
        setGems(getGems() + amount);
    }

    public void removeGems(long amount) {
        setGems(Math.max(0, getGems() - amount));
    }

    public void addWoodCutting(long amount) {
        setWoodCutting(getWoodCutting() + amount);
    }

    public void removeWoodCutting(long amount) {
        setWoodCutting(Math.max(0, getWoodCutting() - amount));
    }

    public void addBlocks(long amount) {
        setBlocks(getBlocks() + amount);
    }

    public void removeBlocks(long amount) {
        setBlocks(Math.max(0, getBlocks() - amount));
    }

    public void addFishing(long amount) {
        setFishing(getFishing() + amount);
    }

    public void removeFishing(long amount) {
        setFishing(Math.max(0, getFishing() - amount));
    }

    public void addPVE_KILLS(long amount) {
        setPveKills(getPveKills() + amount);
    }

    public void removePVE_KILLS(long amount) {
        setPveKills(Math.max(0, getPveKills() - amount));
    }

    public void addKills(int amount) {
        setKills(getKills() + amount);
    }

    public void removeKills(int amount) {
        setKills(Math.max(0, getKills() - amount));
    }

    public void addDeaths(int amount) {
        setDeaths(getDeaths() + amount);
    }

    public void removeDeaths(int amount) {
        setDeaths(Math.max(0, getDeaths() - amount));
    }

    public void addKillstreak(int amount) {
        setKillstreak(getKillstreak() + amount);
    }

    public void removeKillstreak(int amount) {
        setKillstreak(Math.max(0, getKillstreak() - amount));
    }

    public void addBans(int amount) {
        setBans(getBans() + amount);
    }

    public void removeBans(int amount) {
        setBans(Math.max(0, getBans() - amount));
    }

    public void addMutes(int amount) {
        setMutes(getMutes() + amount);
    }

    public void removeMutes(int amount) {
        setMutes(Math.max(0, getMutes() - amount));
    }

    public void addWarns_Total(int amount) {
        setWarnsTotal(getWarnsTotal() + amount);
    }

    public void removeWarns_Total(int amount) {
        setWarnsTotal(Math.max(0, getWarnsTotal() - amount));
    }

    public void addWarns_Now(int amount) {
        setWarnsNow(getWarnsNow() + amount);
    }

    public void removeWarns_Now(int amount) {
        setWarnsNow(Math.max(0, getWarnsNow() - amount));
    }


    public void updateAllValuesPlayer() {
        updateAllValuesGenPlayer();
        String query = "UPDATE PlayerData SET " +
                "Name=?, Clan=?, Money=?, Credits=?, Gems=?, Linked=?, WoodCutting=?, Blocks=?, " +
                "Fishing=?, PVE_KILLS=?, Kills=?, Deaths=?, Killstreak=?, Playtime=? WHERE UUID=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            TempPlayerCache playerData = this;

            statement.setString(1, playerData.getName());
            statement.setString(2, playerData.getClan());
            statement.setLong(3, playerData.getMoney());
            statement.setLong(4, playerData.getCredits());
            statement.setLong(5, playerData.getGems());
            statement.setBoolean(6, playerData.isLinked());
            statement.setLong(7, playerData.getWoodCutting());
            statement.setLong(8, playerData.getBlocks());
            statement.setLong(9, playerData.getFishing());
            statement.setLong(10, playerData.getPveKills());
            statement.setInt(11, playerData.getKills());
            statement.setInt(12, playerData.getDeaths());
            statement.setInt(13, playerData.getKillstreak());
            statement.setLong(14, playerData.getPlaytime());
            statement.setString(15, playerData.getUUID());

            statement.addBatch();


            int[] updateCounts = statement.executeBatch();
            // Handle the updateCounts array if needed

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateAllValuesGenPlayer() {
        String query = "UPDATE Saphirus_GenPlayer SET " +
                "Multiplier=?, Gens=?, Level=?, XP=?, ControlStation=?, " +
                "ControlStationPlaced=?, ControlStation_Collected_Gems=?, ControlStation_Collected_Money=?, " +
                "ControlStation_Collected_Crates=?, ControlStation_Collected_XP=? WHERE UUID=?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            TempPlayerCache playerData = this;

            statement.setDouble(1, playerData.getMultiplier());
            statement.setString(2, playerData.getGens());
            statement.setInt(3, playerData.getLevel());
            statement.setLong(4, playerData.getXp());
            statement.setString(5, playerData.getControlstation());
            statement.setBoolean(6, playerData.isControlstation_placed());
            statement.setLong(7, playerData.getCollected_gems());
            statement.setLong(8, playerData.getCollected_money());
            statement.setInt(9, playerData.getCollected_crates());
            statement.setLong(10, playerData.getCollected_xp());
            statement.setString(11, playerData.getUUID());

            statement.addBatch();

            int[] updateCounts = statement.executeBatch();
            // Handle the updateCounts array if needed

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAllValuesInDatabase() {

        String query = "UPDATE PlayerData SET " +
                "Name=?, Clan=?, Money=?, Credits=?, Gems=?, Linked=?, WoodCutting=?, Blocks=?, " +
                "Fishing=?, PVE_KILLS=?, Kills=?, Deaths=?, Killstreak=?, Playtime=? WHERE UUID=?";

        String genQuery = "UPDATE Saphirus_GenPlayer SET " +
                "Multiplier=?, Gens=?, Level=?, XP=?, ControlStation=?, " +
                "ControlStationPlaced=?, ControlStation_Collected_Gems=?, ControlStation_Collected_Money=?, " +
                "ControlStation_Collected_Crates=?, ControlStation_Collected_XP=? WHERE UUID=?";

        try (PreparedStatement statementPlayer = connection.prepareStatement(query);
             PreparedStatement statementGenPlayer = connection.prepareStatement(genQuery)) {
            for (Map.Entry<String, TempPlayerCache> entry : data.entrySet()) {
                TempPlayerCache playerData = entry.getValue();

                // Update PlayerData
                statementPlayer.setString(1, playerData.getName());
                statementPlayer.setString(2, playerData.getClan());
                statementPlayer.setLong(3, playerData.getMoney());
                statementPlayer.setLong(4, playerData.getCredits());
                statementPlayer.setLong(5, playerData.getGems());
                statementPlayer.setBoolean(6, playerData.isLinked());
                statementPlayer.setLong(7, playerData.getWoodCutting());
                statementPlayer.setLong(8, playerData.getBlocks());
                statementPlayer.setLong(9, playerData.getFishing());
                statementPlayer.setLong(10, playerData.getPveKills());
                statementPlayer.setInt(11, playerData.getKills());
                statementPlayer.setInt(12, playerData.getDeaths());
                statementPlayer.setInt(13, playerData.getKillstreak());
                statementPlayer.setLong(14, playerData.getPlaytime());
                statementPlayer.setString(15, playerData.getUUID());

                statementPlayer.addBatch();

                statementGenPlayer.setDouble(1, playerData.getMultiplier());
                statementGenPlayer.setString(2, playerData.getGens());
                statementGenPlayer.setInt(3, playerData.getLevel());
                statementGenPlayer.setLong(4, playerData.getXp());
                statementGenPlayer.setString(5, playerData.getControlstation());
                statementGenPlayer.setBoolean(6, playerData.isControlstation_placed());
                statementGenPlayer.setLong(7, playerData.getCollected_gems());
                statementGenPlayer.setLong(8, playerData.getCollected_money());
                statementGenPlayer.setInt(9, playerData.getCollected_crates());
                statementGenPlayer.setLong(10, playerData.getCollected_xp());
                statementGenPlayer.setString(11, playerData.getUUID());

                statementGenPlayer.addBatch();
            }

            int[] updateCountsPlayer = statementPlayer.executeBatch();
            int[] updateCountsGenPlayer = statementGenPlayer.executeBatch();
            // Handle the updateCounts array if needed

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
