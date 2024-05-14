package com.saphirus.commands;

import com.saphirus.main.Data;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadcastCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;
        if(p.hasPermission("saphirus.broadcast")) {

            if(args.length == 0) {
                p.sendMessage(Data.Usage + "/broadcast <message>");
                return true;
            }

            String msg = "§f";
            for(int i = 0; i < args.length; i++) {
                msg = msg + args[i];
                if(i + 1 < args.length) {
                    msg = msg + " ";
                }
            }
            String bcmsg = msg.replaceAll("&", "§");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(Data.Broadcast + bcmsg);
            Bukkit.broadcastMessage("");
            for(Player all : Bukkit.getOnlinePlayers()) {
                all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5.0F, 5.0F);
            }
        }

        return false;
    }
}
