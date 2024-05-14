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

public class CountdownCMD implements CommandExecutor {

    public static boolean running = false;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;

        if(p.hasPermission("saphirus.countdown")) {
            if(args.length == 1) {
                if(!running) {
                    if(Maths.isInt(args[0])) {


                        Bukkit.broadcastMessage(Data.Countdown + "The player §c" + p.getName() + " §fhas started a §c" + args[0] + " seconds §fcountdown");

                        new BukkitRunnable() {
                            int seconds = Integer.parseInt(args[0]);
                            @Override
                            public void run() {
                                switch (seconds) {
                                    case 120,90,60,50,40,30,20,10,5,4,3,2,1 :
                                        Bukkit.broadcastMessage(Data.Countdown + "The countdown will end in §c" + seconds + " seconds");
                                        for(Player all : Bukkit.getOnlinePlayers()) {
                                            all.playSound(all.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5.0F, 5.0F);
                                        }
                                        break;
                                    case 0:
                                        Bukkit.broadcastMessage(Data.Countdown + "The countdown has ended now!");
                                        for(Player all : Bukkit.getOnlinePlayers()) {
                                            all.playSound(all.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 5.0F, 5.0F);
                                        }
                                        this.cancel();
                                        break;
                                }

                                seconds--;
                            }
                        }.runTaskTimerAsynchronously(Main.instance, 20 ,20);


                    } else p.sendMessage(Data.invalidNumber);
                } else p.sendMessage(Data.Countdown + "A countdown is already running!");
            } else p.sendMessage(Data.Usage + "/countdown <Seconds>");
        } else p.sendMessage(Data.noPerms);

        return false;
    }
}
