package com.saphirus.gens;

import com.saphirus.main.Main;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class GenEvent implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItemInHand();
        Block block = event.getBlockPlaced();

        if (item.getItemMeta() != null && item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.instance, "tier"), PersistentDataType.INTEGER)) {
            int tier = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.instance, "tier"), PersistentDataType.INTEGER);

            // Registriere den neuen Generator
            Location loc = block.getLocation();
            Main.getGenItemSpawner().addGenerator(loc, tier, player.getUniqueId().toString());

            player.sendMessage("Du hast einen Generator der Stufe " + tier + " platziert!");
        }

    }


    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Location loc = block.getLocation();

        if (Main.getGenItemSpawner().isGenerator(loc)) {
            // Entferne den Generator
            Main.getGenItemSpawner().removeGenerator(loc);

            event.getPlayer().sendMessage("Du hast einen Generator entfernt!");
        }
    }
}
