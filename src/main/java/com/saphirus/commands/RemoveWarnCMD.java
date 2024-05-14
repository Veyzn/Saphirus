package com.saphirus.commands;

import com.saphirus.main.Data;
import com.saphirus.manager.PunishManager;
import com.saphirus.utils.PlayerCache;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveWarnCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)commandSender;

        if(p.hasPermission("saphirus.removewarn")) {
            if(args.length == 2) {
                if(Bukkit.getPlayer(args[0]) != null) {
                    Player t = Bukkit.getPlayer(args[0]);
                    PunishManager pm = new PunishManager(t.getUniqueId().toString());
                    PlayerCache pc = new PlayerCache(t.getUniqueId().toString());
                    if(!args[1].equalsIgnoreCase("all")) {
                        if (pm.doesWarnIDExist(Integer.parseInt(args[1]))) {
                            p.sendMessage(Data.Prefix + "Removed WarnID §c" + args[1] + " §ffrom player §c" + t.getName());
                            pm.deleteWarnByID(Integer.parseInt(args[1]));
                            pc.removeWarnsNow(1);
                            pc.removeWarnsTotal(1);
                        } else p.sendMessage(Data.Prefix + "This WarnID doesn't exist!");
                    } else {
                        p.sendMessage(Data.Prefix + "Removed all Warns from player §c" + t.getName());
                        pm.deleteAllPlayerWarns();
                        pc.setWarnsTotal(0);
                        pc.setWarnsNow(0);
                    }
                } else p.sendMessage(Data.Offline);
            } else p.sendMessage(Data.Usage + "/delwarn <Player> <WarnID>");
        } else p.sendMessage(Data.noPerms);

        return false;
    }
}
