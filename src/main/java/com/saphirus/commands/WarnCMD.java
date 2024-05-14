package com.saphirus.commands;

import com.saphirus.main.Data;
import com.saphirus.manager.PunishManager;
import com.saphirus.utils.Maths;
import com.saphirus.utils.PlayerCache;
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

public class WarnCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)commandSender;

        if(p.hasPermission("saphirus.warn")) {
            if(args.length >= 3) {
                if(Bukkit.getPlayer(args[0]) != null) {
                    Player t = Bukkit.getPlayer(args[0]);
                    PlayerCache pc = new PlayerCache(t.getUniqueId().toString());
                    if(!t.hasPermission("saphirus.staff") || p.isOp()) {
                        if (Maths.isInt(args[1])) {
                            String reason = "";
                            for (int i = 2; i < args.length; i++) {
                                reason = reason + args[i];
                                if (i + 1 < args.length) {
                                    reason = reason + " ";
                                }
                            }

                            PunishManager pm = new PunishManager(t.getUniqueId().toString());
                            pm.addWarn(t,p,Integer.parseInt(args[1]),reason);
                            pc.addWarnsTotal(1);
                            pc.addWarnsNow(1);

                            switch (pc.getWarnsNow()) {
                                case 3:
                                    pm.addBan(t.getUniqueId().toString(), p, System.currentTimeMillis() + (1000*60*60*24*3), "3x Warns");
                                    pc.addBans(1);
                                    break;


                                case 6:
                                    pm.addBan(t.getUniqueId().toString(), p, System.currentTimeMillis() + (1000*60*60*24*7), "6x Warns");
                                    pc.addBans(1);
                                    break;


                                case 9:
                                    pm.addBan(t.getUniqueId().toString(), p, System.currentTimeMillis() + (1000*60*60*24*14), "9x Warns");
                                    pc.addBans(1);
                                    pc.setWarnsNow(0);
                                    break;
                            }
                        } else p.sendMessage(Data.invalidNumber);
                    } else p.sendMessage(Data.Prefix + "You can't warn this player!");
                } else p.sendMessage(Data.Offline);
            } else p.sendMessage(Data.Usage + "/warn <Player> <Amount> <Reason>");
        } else p.sendMessage(Data.noPerms);

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Player p = (Player) commandSender;
        if(p.hasPermission("saphirus.warn")) {
            List<String> completer = new ArrayList<>();
            if (strings.length == 2) {
                completer.add("How many warns?");

                return completer;
            }

            if (strings.length == 3) {
                completer.add("Reason");
                return completer;
            }
        }


        return null;
    }
}
