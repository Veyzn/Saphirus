package com.saphirus.commands;

import com.saphirus.main.Data;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GlobaldmuteCMD implements CommandExecutor {

    public static boolean status = false;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;

        if(p.hasPermission("saphirus.staff")) {
            if(status) {
                status = false;
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(Data.Globalmute + "The chat got §aactivated §fby §c" + p.getName());
                Bukkit.broadcastMessage("");
            } else {
                status = true;
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(Data.Globalmute + "The chat got §cdeactivated §fby §c" + p.getName());
                Bukkit.broadcastMessage("");
            }
            for(Player all : Bukkit.getOnlinePlayers()) {
                all.playSound(all.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5.0F, 5.0F);
            }
        } else p.sendMessage(Data.noPerms);



        return false;
    }
}
