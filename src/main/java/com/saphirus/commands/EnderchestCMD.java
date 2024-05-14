package com.saphirus.commands;

import com.saphirus.main.Data;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class EnderchestCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Player p = (Player)commandSender;


        if(p.hasPermission("saphirus.enderchest")) {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5.0F, 5.0F);
            p.openInventory(p.getEnderChest());
        } else p.sendMessage(Data.noPerms);

        return false;
    }
}
