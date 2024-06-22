package com.saphirus.gens;

import com.saphirus.main.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GenTierManager {
    private static final String FILE_PATH = "plugins/YourPlugin/gen_tiers.yml";
    private File file;
    private FileConfiguration config;
    private Map<Integer, GenTier> genTiers;

    public GenTierManager() {
        this.file = new File(FILE_PATH);
        this.config = YamlConfiguration.loadConfiguration(file);
        this.genTiers = new HashMap<>();
        loadGenTiers();
        saveConfig();
    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addTier(int tier, long upgradePrice, long sellPrice, Material block, String displayName) {
        String tierPath = "tiers." + tier;
        config.set(tierPath + ".upgrade_price", upgradePrice);
        config.set(tierPath + ".sell_price", sellPrice);
        config.set(tierPath + ".block", block.name());
        config.set(tierPath + ".display_name", displayName);
        saveConfig();
        loadGenTiers();  // Reload tiers after adding
    }

    public void removeTier(int tier) {
        String tierPath = "tiers." + tier;
        config.set(tierPath, null);
        saveConfig();
        loadGenTiers();  // Reload tiers after removing
    }

    private void loadGenTiers() {
        genTiers.clear();
        for (String key : config.getConfigurationSection("tiers").getKeys(false)) {
            int tier = Integer.parseInt(key);
            long upgradePrice = config.getLong("tiers." + tier + ".upgrade_price");
            long sellPrice = config.getLong("tiers." + tier + ".sell_price");
            Material block = Material.valueOf(config.getString("tiers." + tier + ".block"));
            String displayName = config.getString("tiers." + tier + ".display_name");
            genTiers.put(tier, new GenTier(tier, upgradePrice, sellPrice, block, displayName));
        }
    }

    public Map<Integer, GenTier> getGenTiers() {
        return genTiers;
    }

    // Getter and Setter for upgrade_price
    public long getUpgradePrice(int tier) {
        return config.getLong("tiers." + tier + ".upgrade_price");
    }

    public void setUpgradePrice(int tier, long upgradePrice) {
        config.set("tiers." + tier + ".upgrade_price", upgradePrice);
        saveConfig();
    }

    // Getter and Setter for sell_price
    public long getSellPrice(int tier) {
        return config.getLong("tiers." + tier + ".sell_price");
    }

    public void setSellPrice(int tier, long sellPrice) {
        config.set("tiers." + tier + ".sell_price", sellPrice);
        saveConfig();
    }

    // Getter and Setter for block
    public Material getBlock(int tier) {
        return Material.valueOf(config.getString("tiers." + tier + ".block"));
    }

    public void setBlock(int tier, Material block) {
        config.set("tiers." + tier + ".block", block.name());
        saveConfig();
    }

    // Getter and Setter for display_name
    public String getDisplayName(int tier) {
        return config.getString("tiers." + tier + ".display_name");
    }

    public void setDisplayName(int tier, String displayName) {
        config.set("tiers." + tier + ".display_name", displayName);
        saveConfig();
    }

    public static class GenTier {
        private int tier;
        private long upgradePrice;
        private long sellPrice;
        private Material block;
        private String displayName;

        public GenTier(int tier, long upgradePrice, long sellPrice, Material block, String displayName) {
            this.tier = tier;
            this.upgradePrice = upgradePrice;
            this.sellPrice = sellPrice;
            this.block = block;
            this.displayName = displayName;
        }

        public int getTier() {
            return tier;
        }

        public long getUpgradePrice() {
            return upgradePrice;
        }

        public long getSellPrice() {
            return sellPrice;
        }

        public Material getBlock() {
            return block;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public ItemStack getGenTier(Player p, int tier) {
        GenTier genTier = genTiers.get(tier);
        if (genTier == null) {
            return null;
        }

        ItemStack itemStack = new ItemStack(genTier.getBlock());
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta != null) {
            itemMeta.setDisplayName(genTier.getDisplayName());
            itemMeta.getPersistentDataContainer().set(new NamespacedKey(Main.instance, "tier") , PersistentDataType.INTEGER, tier);
            itemStack.setItemMeta(itemMeta);
        }

        return itemStack;
    }
}
