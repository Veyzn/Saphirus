package com.saphirus.commands;

import com.saphirus.main.Data;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatclearCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;


        if(p.hasPermission("saphirus.staff")) {
            for(Player all : Bukkit.getOnlinePlayers()) {
                if(!all.hasPermission("saphirus.staff")) {
                    for (int i = 0; i < 100; i++) {
                        all.sendMessage("");
                    }
                }

                all.sendMessage(Data.Chatclear + "§c" + p.getName() + "§f has cleared the chat!");
            }
            for(Player all : Bukkit.getOnlinePlayers()) {
                all.playSound(all.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5.0F, 5.0F);
            }

        }else p.sendMessage(Data.noPerms);

        return false;
    }
}
