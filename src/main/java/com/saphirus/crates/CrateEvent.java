package com.saphirus.crates;

import com.saphirus.main.Data;
import com.saphirus.main.Main;
import com.saphirus.utils.Utils;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class CrateEvent implements Listener {

    @EventHandler
    public void breakCrateBlock(BlockBreakEvent e) {
        CrateLocations cm = new CrateLocations();
        Player p = e.getPlayer();
        if(cm.isLocationCrateBlock(e.getBlock().getLocation())) {
            if(p.isSneaking() && p.isOp()) {
                p.sendMessage(Data.Crates + "You have removed a crate location from §c" + cm.getBlockCrate(e.getBlock().getLocation()));
                cm.removeLocation(e.getBlock().getLocation());
            } else {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteractWithBlock(PlayerInteractEvent e) {
        Player p = e.getPlayer();

        if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            CrateLocations cl = new CrateLocations();
            if(cl.isLocationCrateBlock(e.getClickedBlock().getLocation())) {
                 e.setCancelled(true);

                 if(p.getInventory().getItemInMainHand().hasItemMeta()) {
                     ItemStack item = p.getInventory().getItemInMainHand();

                     if(item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.instance, "crate"), PersistentDataType.STRING)) {

                        String crate = item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.instance, "crate"), PersistentDataType.STRING);

                        if(cl.getBlockCrate(e.getClickedBlock().getLocation()).equalsIgnoreCase(crate)) {
                            CrateManager cm = new CrateManager(crate);
                            if(cm.getActiv()) {
                                if(Utils.hasFreeSpaceInInventory(p)) {

                                    Utils.removeItemFromHand(p, 1);
                                    cm.win(p);


                                } else {
                                    p.sendTitle("§c§lERROR", "§fYour inventory is full!", 10,30,10);
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 5.0F, 5.0F);
                                }
                            } else p.sendTitle("§c§lERROR", "§fThis crate is currently disabled!", 10,30,10);
                        } else {
                            p.sendTitle("§c§lERROR", "§fYou have don't have the key in your hand!", 10,30,10);
                            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 5.0F, 5.0F);
                        }
                     } else {
                         p.sendTitle("§c§lERROR", "§fYou have don't have the key in your hand!", 10,30,10);
                         p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 5.0F, 5.0F);
                     }
                 } else {
                     p.sendTitle("§c§lERROR", "§fYou have don't have the key in your hand!", 10,30,10);
                     p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_DIDGERIDOO, 5.0F, 5.0F);
                 }

            }
        } else if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
            CrateLocations cl = new CrateLocations();
            if(cl.isLocationCrateBlock(e.getClickedBlock().getLocation())) {
                if(p.isSneaking() && p.isOp()) {
                    e.setCancelled(false);
                } else {
                    e.setCancelled(true);
                    CrateManager cm = new CrateManager(cl.getBlockCrate(e.getClickedBlock().getLocation()));
                    cm.openPreview(p);
                }
            }
        }

    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {

        Player p = e.getPlayer();

        if(p.getInventory().getItemInMainHand().hasItemMeta()) {
            if(p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.instance, "crate"), PersistentDataType.STRING)) {
                e.setCancelled(true);
            }
        }
    }
}
