package com.saphirus.commands;

import com.saphirus.fastinv.FastInv;
import com.saphirus.fastinv.ItemBuilder;
import com.saphirus.main.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BodyseeCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;

        if(p.hasPermission("saphirus.bodysee")) {
            if(args.length == 1) {
                FastInv inv = new FastInv(3 * 9, "§f§lBODYSEE");
                if(Bukkit.getPlayer(args[0]) != null) {
                    Player t = Bukkit.getPlayer(args[0]);
                    ItemStack empty = new ItemBuilder(Material.BARRIER).name("§cEmpty").build();

                    if(t.getInventory().getHelmet() != null) {
                        inv.setItem(10, t.getInventory().getHelmet());
                    } else inv.setItem(10, empty);

                    if(t.getInventory().getChestplate() != null) {
                        inv.setItem(12, t.getInventory().getChestplate());
                    } else inv.setItem(12, empty);

                    if(t.getInventory().getLeggings() != null) {
                        inv.setItem(14, t.getInventory().getLeggings());
                    } else inv.setItem(14, empty);

                    if(t.getInventory().getBoots() != null) {
                        inv.setItem(16, t.getInventory().getBoots());
                    } else inv.setItem(16, empty);

                    inv.open(p);
                } else p.sendMessage(Data.Offline);
            } else p.sendMessage(Data.Usage + "/bodysee <Player>");
        } else p.sendMessage(Data.noPerms);

        return false;
    }
}
