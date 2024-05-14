package com.saphirus.commands;

import com.saphirus.main.Data;
import com.saphirus.manager.PunishManager;
import com.saphirus.utils.Maths;
import com.saphirus.utils.PlayerCache;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MuteCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)commandSender;

        if(p.hasPermission("saphirus.mute")) {
            if(args.length >= 4) {
                if(Bukkit.getPlayer(args[0]) != null) {
                    Player t = Bukkit.getPlayer(args[0]);
                    PlayerCache pc = new PlayerCache(t.getUniqueId().toString());
                    if (pc.exists()) {
                        if (Maths.isInt(args[1])) {
                            String reason = "";
                            for (int i = 3; i < args.length; i++) {
                                reason = reason + args[i];
                                if (i + 1 < args.length) {
                                    reason = reason + " ";
                                }
                            }
                            PunishManager pm = new PunishManager(t.getUniqueId().toString());
                            String unit = args[2];

                            if (!pm.isMuted()) {
                                LuckPerms luckPerms = LuckPermsProvider.get();
                                if(!t.hasPermission("saphirus.staff")) {
                                    long time = System.currentTimeMillis();

                                    switch (unit) {
                                        case "days", "d":
                                            time += (long) 1000L * 60 * 60 * 24 * Integer.parseInt(args[1]);
                                            break;
                                        case "hours", "h":
                                            time += (long) 1000L * 60 * 60 * Integer.parseInt(args[1]);
                                            break;
                                        case "minutes", "m":
                                            time += (long) 1000L * 60 * Integer.parseInt(args[1]);
                                            break;
                                    }

                                    pm.addMute(t, p, time, reason);
                                    pc.addMutes(1);
                                } else p.sendMessage(Data.Prefix + "You can't ban this player!");
                            } else p.sendMessage(Data.Prefix + "This player is already muted!");
                        } else p.sendMessage(Data.invalidNumber);
                    } else p.sendMessage(Data.Prefix + "This player never joined the server!");
                } else p.sendMessage(Data.Offline);
            } else p.sendMessage(Data.Usage + "/mute <Player> <Duration> <Days | Hours | Minutes> <Reason>");

        } else p.sendMessage(Data.noPerms);

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> completer = new ArrayList<>();
        Player p = (Player) commandSender;
        if(p.hasPermission("saphirus.mute")) {
            if (strings.length == 2) {
                completer.add("How long should he be muted for? (For example: 1 , 2 , 3 ,4)");

                return completer;
            }

            if (strings.length == 3) {
                completer.add("days");
                completer.add("hours");
                completer.add("minutes");
                return completer;
            }

            if (strings.length == 4) {
                completer.add("For what reason does he get muted");
                return completer;
            }
        }
        return null;
    }

    private String getUUID(String playername) {
        return Bukkit.getOfflinePlayer(playername).getUniqueId().toString();
    }
}
