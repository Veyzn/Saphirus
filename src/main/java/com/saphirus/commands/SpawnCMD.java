package com.saphirus.commands;

import com.saphirus.main.Config;
import com.saphirus.main.Data;
import com.saphirus.main.Main;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class SpawnCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Player p = (Player)commandSender;

        if(p.hasPermission("saphirus.instant.tp")) {
            p.teleport(Config.getSpawn());
            p.sendTitle("§a§lTELEPORTED", "§fYou have been teleport to the §cSpawn", 10,20,10);
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5,1);
        } else {
            if(!Data.teleporting.containsKey(p.getUniqueId().toString())) {
                Data.teleporting.put(p.getUniqueId().toString(), p.getLocation());
                p.sendTitle("§a§lTELEPORTING", "§fYou will be teleported in §c3 seconds", 5, 20, 5);

                new BukkitRunnable() {
                    int sec = 3;
                    @Override
                    public void run() {
                        if(!Data.teleporting.containsKey(p.getUniqueId().toString())) {
                            this.cancel();
                            return;
                        }
                        switch (sec) {
                            case 2,1:
                                p.sendTitle("§a§lTELEPORTING", "§fYou will be teleported in §c" + sec + " seconds",1,23,1);
                                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5,1);
                                break;
                            case 0:
                                p.sendTitle("§a§lTELEPORTED", "§fYou have been teleport to the §cSpawn", 1,20,10);
                                p.teleport(Config.getSpawn());
                                Data.teleporting.remove(p.getUniqueId().toString());
                                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5,1);
                                this.cancel();
                                break;
                        }

                        sec--;

                    }
                }.runTaskTimer(Main.instance,0,20);
            }
        }

        return false;
    }
}
