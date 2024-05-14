package com.saphirus.commands;

import com.saphirus.main.Data;
import com.saphirus.main.Main;
import com.saphirus.utils.Maths;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RandomCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;

        if(p.hasPermission("saphirus.random")) {
            if(args.length == 0) {
                int r = new Random().nextInt(Bukkit.getOnlinePlayers().size());
                final Player random = (Player)Bukkit.getOnlinePlayers().toArray()[r];
                Bukkit.broadcastMessage(Data.Random + "The one and only chosen player is §c" + random.getName());
            } else if(args.length == 1) {
                if(Maths.isInt(args[0])) {
                    int amount = Integer.parseInt(args[0]);
                    Bukkit.broadcastMessage(Data.Random + "§c" + p.getName() + "§f will randomly choose §c" + amount + "x Players");
                    new BukkitRunnable() {
                        int i = 0;
                        public void run() {
                            if( i != amount) {
                                i++;
                                int r = new Random().nextInt(Bukkit.getOnlinePlayers().size());
                                final Player random = (Player)Bukkit.getOnlinePlayers().toArray()[r];
                                Bukkit.broadcastMessage(Data.Random + " §c" + i +  ". §f" + random.getName());
                                for(Player all : Bukkit.getOnlinePlayers()) {
                                    all.playSound(all.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5.0F, 5.0F);
                                }
                            }

                            if(i == amount) {
                                Bukkit.broadcastMessage(Data.Random);
                                Bukkit.broadcastMessage(Data.Random + "Congratulations to the chosen ones!");
                                this.cancel();
                            }
                        }
                    }.runTaskTimerAsynchronously(Main.instance, 20,5);



                }else p.sendMessage(Data.invalidNumber);

            } else p.sendMessage(Data.Usage + "/random <amount> (optional)");


        } else p.sendMessage(Data.noPerms);

        return false;
    }
}
