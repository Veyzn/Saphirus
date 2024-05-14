package com.saphirus.commands;

import com.saphirus.main.Data;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlyCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;

        if(p.hasPermission("saphirus.fly")) {
            if(args.length == 0) {
                if(!p.getAllowFlight()) {
                    p.sendMessage(Data.Fly + "You are now §cflying");
                    p.setAllowFlight(true);
                } else {
                    p.sendMessage(Data.Fly + "You are not §cflying §fanymore");
                    p.setAllowFlight(false);
                }
            } else if(args.length == 1) {
                if(p.hasPermission("saphirus.fly.other")) {
                    if(Bukkit.getPlayer(args[0]) != null) {
                        Player t = Bukkit.getPlayer(args[0]);
                        if(!t.getAllowFlight()) {
                            t.sendMessage(Data.Fly + "You are now §cflying");
                            t.setAllowFlight(true);
                            p.sendMessage(Data.Fly + "The player §c" + t.getName() + "§f is now §cflying");
                        } else {
                            t.sendMessage(Data.Fly + "You are not §cflying §fanymore");
                            t.setAllowFlight(false);
                            p.sendMessage(Data.Fly + "The player §c" + t.getName() + "§f is not §cflying §fanymore");
                        }
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5.0F, 5.0F);
                    } else p.sendMessage(Data.Offline);
                } else p.sendMessage(Data.noPerms);
            } else p.sendMessage(Data.Usage + "/fly");
        } else p.sendMessage(Data.noPerms);

        return false;
    }
}
