package com.saphirus.commands;

import com.saphirus.main.Data;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WorkbenchCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player)sender;

        if(p.hasPermission("saphirus.workbench")) {
            p.openWorkbench(p.getLocation(), true);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5.0F, 5.0F);
        } else p.sendMessage(Data.noPerms);

        return false;
    }
}
