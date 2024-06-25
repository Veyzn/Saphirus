package com.saphirus.gens;

import com.saphirus.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GenManager implements Listener {
    private final GenJsonManager GenJsonManager;
    private final GenTierManager genTierManager;
    private final Map<UUID, Map<Location, GenJsonManager.Generator>> playerGenerators;

    public GenManager(GenJsonManager GenJsonManager, GenTierManager genTierManager) {
        this.GenJsonManager = Main.getGenJsonManager();
        this.genTierManager = Main.getGenTierManager();
        this.playerGenerators = new HashMap<>();
        loadAllPlayerGenerators();
        startSpawningTask();
    }

    public void addGenerator(Location loc, int tier, String uuid) {
        GenJsonManager.addGenerator(loc, tier, uuid);
        UUID ownerUUID = UUID.fromString(uuid);
        playerGenerators.computeIfAbsent(ownerUUID, k -> new HashMap<>()).put(loc, new GenJsonManager.Generator(loc, tier, uuid));
    }

    public void removeGenerator(Location loc) {
        GenJsonManager.removeGenerator(loc);
        playerGenerators.values().forEach(generators -> generators.remove(loc));
    }

    public boolean isGenerator(Location loc) {
        return playerGenerators.values().stream().anyMatch(generators -> generators.containsKey(loc));
    }

    public void updateTier(Location loc, int tier) {
        GenJsonManager.updateTier(loc, tier);
        for (Map<Location, GenJsonManager.Generator> generators : playerGenerators.values()) {
            if (generators.containsKey(loc)) {
                generators.get(loc).setTier(tier);
                break;
            }
        }
    }

    public void loadPlayerGenerators(UUID playerUUID) {
        List<GenJsonManager.Generator> generators = GenJsonManager.getGeneratorsByOwner(playerUUID.toString());
        Map<Location, GenJsonManager.Generator> playerGenMap = new HashMap<>();
        for (GenJsonManager.Generator generator : generators) {
            playerGenMap.put(generator.getLocation(), generator);
        }
        playerGenerators.put(playerUUID, playerGenMap);
    }

    public void unloadPlayerGenerators(UUID playerUUID) {
        playerGenerators.remove(playerUUID);
    }

    public void loadAllPlayerGenerators() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            loadPlayerGenerators(player.getUniqueId());
        }
    }

    public void unloadAllPlayerGenerators() {
        playerGenerators.clear();
    }


    private void startSpawningTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, Map<Location, GenJsonManager.Generator>> entry : playerGenerators.entrySet()) {
                    UUID ownerUUID = entry.getKey();
                    Player player = Bukkit.getPlayer(ownerUUID);
                    if (player != null && player.isOnline()) {
                        for (GenJsonManager.Generator generator : entry.getValue().values()) {
                            GenTierManager.GenTier genTier = genTierManager.getGenTiers().get(generator.getTier());
                            if (genTier != null) {
                                spawnItem(generator.getLocation().add(0.5,0.2,0.5), genTier.getSellPrice());
                            }
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(Main.instance, 0L, 200L); // 200 ticks = 10 seconds
    }

    private void spawnItem(Location location, long sellPrice) {
        new BukkitRunnable() {
            @Override
            public void run() {
                ItemStack itemStack = new ItemStack(Material.PAPER);
                ItemMeta itemMeta = itemStack.getItemMeta();
                if (itemMeta != null) {
                    itemMeta.setDisplayName("$" + sellPrice);
                    itemMeta.getPersistentDataContainer().set(new NamespacedKey(Main.instance, "sellprice"), PersistentDataType.LONG, sellPrice);
                    itemStack.setItemMeta(itemMeta);
                }
                Location spawnLocation = location.clone().add(0, 1, 0); // Spawn item 1 block above the generator
                Item item = location.getWorld().dropItem(spawnLocation, itemStack);
                item.setPickupDelay(0);
            }
        }.runTask(Main.instance);
    }
}
