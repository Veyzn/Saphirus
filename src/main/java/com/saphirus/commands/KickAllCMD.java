package com.saphirus.commands;

import com.saphirus.main.Data;
import com.saphirus.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class KickAllCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player)commandSender;


        if(p.hasPermission("saphirus.kickall")) {
            ArrayList<Player> players = new ArrayList<>();
            for(Player all : Bukkit.getOnlinePlayers()) {
                if(!all.hasPermission("saphirus.kickall")) {
                    players.add(all);
                }
            }

            p.sendMessage(Data.Prefix + "All players will be kicked now!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(players.size()==0) {
                        this.cancel();
                        return;
                    }
                    Player r = players.get(0);
                    if(r.isOnline()) {
                        r.kickPlayer(Data.getHeader() + "\n" +
                                "§fThe Server is restarting\n" +
                                "\n" +
                                "§c- §fWe will be back soon! §c-");
                    }

                    players.remove(r);
                }
            }.runTaskTimer(Main.instance, 10,10);



        } else p.sendMessage(Data.noPerms);

        return false;
    }
}
