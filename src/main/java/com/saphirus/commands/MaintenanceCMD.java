package com.saphirus.commands;

import com.saphirus.main.Config;
import com.saphirus.main.Data;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MaintenanceCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player)sender;


        if(p.hasPermission("saphirus.maintenance")) {
            if(!Config.isMaintenanceEnabled()) {
                Config.setMaintenance(true);
                p.sendMessage(Data.Prefix + "You just §aactivated §fthe §cMaintenance-Mode");
                for(Player all : Bukkit.getOnlinePlayers()) {
                    if (!all.hasPermission("saphirus.admin")) {
                        all.kickPlayer(Data.getHeader() + "\n\n" +
                                "§fThe §cMaintenance-Mode §fjust got activated\n" +
                                "§bFeel free to join our Discord\n" +
                                "§fdiscord.saphirus.com\n\n" +
                                "§c- §fWe will be back soon! §c-");
                    }
                }
            } else {
                Config.setMaintenance(false);
                p.sendMessage(Data.Prefix + "You just §cdeactivated §fthe §cMaintenance-Mode");
            }
        } else p.sendMessage(Data.noPerms);



        return false;
    }
}
