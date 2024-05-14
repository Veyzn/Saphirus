package com.saphirus.commands;

import com.saphirus.main.Data;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StaffchatCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;

        if(p.hasPermission("saphirus.staff")) {

            if(args.length == 0) {
                p.sendMessage(Data.Usage + "/teamchat <message>");
                return true;
            }

            String msg = "§c";
            for(int i = 0; i < args.length; i++) {
                msg = msg + args[i];
                if(i + 1 < args.length) {
                    msg = msg + " ";
                }
            }
            String staffmsg = msg;

            for(Player all : Bukkit.getOnlinePlayers()) {
                if(all.hasPermission("saphirus.staff")) {
                    all.sendMessage(Data.Teamchat + "§f" + p.getName() + ": " + staffmsg);
                    all.playSound(all.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5.0F, 5.0F);
                }
            }

        } else p.sendMessage(Data.noPerms);

        return false;
    }
}
