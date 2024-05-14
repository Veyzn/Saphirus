package com.saphirus.commands;

import com.saphirus.main.Config;
import com.saphirus.main.Data;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PvPCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p= (Player)sender;

        if(p.hasPermission("saphirus.pvp")) {
            if(Config.isPvPEnabled()) {
                Config.setPvP(false);
                Bukkit.broadcastMessage(Data.PvP + "PvP just got §cdisabled §fon the Server");
            } else {
                Config.setPvP(true);
                Bukkit.broadcastMessage(Data.PvP + "PvP just got §aenabled §fon the Server");
            }
        } else p.sendMessage(Data.noPerms);

        return false;
    }
}
