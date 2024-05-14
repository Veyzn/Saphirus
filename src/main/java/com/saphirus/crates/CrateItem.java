package com.saphirus.crates;


import com.saphirus.fastinv.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.*;


public class CrateItem implements ConfigurationSerializable {
    private int chance;

    public String displayName;

    private ItemStack rewardItem;
    private List<ItemStack> rewardItems;

    private String commands;

    private boolean broadcast;

    private CrateManager.Rarity rarity;

    private int amount;

    public CrateItem(Map<String, Object> map) {
        this.chance = ((Integer)map.get("tickets")).intValue();
        this.displayName = (String)map.get("displayname");
        this.rewardItem = (ItemStack)map.get("reward");
        this.commands = (String)map.get("commands");
        this.broadcast = ((Boolean)map.get("broadcast")).booleanValue();
        this.rarity = CrateManager.Rarity.valueOf((String)map.get("rarity"));
        this.amount = ((Integer)map.get("drawn")).intValue();
        this.rewardItems = ((List<ItemStack>)map.get("rewardItems")).stream().toList();
    }

    public CrateItem(int chance, String displayName, ItemStack rewardItem, String commands, boolean broadcast, CrateManager.Rarity rarity) {
        this.rarity = rarity;
        this.chance = chance;
        this.displayName = displayName;
        this.rewardItem = rewardItem;
        this.commands = commands;
        this.broadcast = broadcast;
        this.amount = 0;
        this.rewardItems = new ArrayList<ItemStack>();
    }

    public CrateItem(ItemStack rewardItem) {
        this.rarity = CrateManager.Rarity.COMMON;
        this.chance = 0;
        this.displayName = rewardItem.getItemMeta().getDisplayName();
        this.rewardItem = rewardItem;
        this.commands = null;
        this.broadcast = false;
        this.amount = 0;
        this.rewardItems = new ArrayList<ItemStack>();
    }

    public boolean isBroadcast() {
        return this.broadcast;
    }

    public int getChance() {
        return this.chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDisplayName() {
        return this.rarity.getColor() + ChatColor.translateAlternateColorCodes('&', this.displayName);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ItemStack getRewardItem() {
        return this.rewardItem.clone();
    }
    public List<ItemStack> getRewardItems() {
        return this.rewardItems;
    }

    public void setRewardItem(ItemStack rewardItem) {
        this.rewardItem = rewardItem;
    }

    public void setRewardItems(ArrayList<ItemStack> list) {
        this.rewardItems = list;
    }

    public String getCommands() {
        return this.commands;
    }

    public ItemStack getDisplayItem() {
        return (new ItemBuilder(this.rewardItem.clone())).lore(Arrays.asList(new String[0])).name(getDisplayName()).build();
    }

    public void setCommands(String commands) {
        this.commands = commands;
    }

    public void setBroadcast(Boolean br) {
        this.broadcast = br;
    }

    public CrateManager.Rarity getRarity() {
        return this.rarity;
    }

    public void setRarity(CrateManager.Rarity rarity) {
        this.rarity = rarity;
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("tickets", Integer.valueOf(this.chance));
        map.put("displayname", this.displayName);
        map.put("reward", this.rewardItem);
        map.put("broadcast", Boolean.valueOf(this.broadcast));
        map.put("rarity", this.rarity.name());
        map.put("drawn", Integer.valueOf(this.amount));
        map.put("commands", this.commands);
        map.put("rewardItems", this.rewardItems);
        return map;
    }
}

