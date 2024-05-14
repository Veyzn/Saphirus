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

public class UnbanCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player) commandSender;
        if(p.hasPermission("saphirus.ban")) {
            if(args.length == 1) {
                String uuid = getUUID(args[0]);
                PlayerCache pc = new PlayerCache(uuid);
                if(pc.exists()) {
                    PunishManager pm = new PunishManager(uuid);
                    if(pm.isBanned()) {
                        pm.removePlayerBan();
                        p.sendMessage(Data.Prefix + "You just unbanned §c" + args[0]);
                        pc.removeBans(1);
                    } else p.sendMessage(Data.Prefix + "This player isn't banned!");
                } else p.sendMessage(Data.Prefix + "This player never joined the server!");
            } else p.sendMessage(Data.Usage + "/unban <Player>");
        } else p.sendMessage(Data.noPerms);

        return false;
    }


    private String getUUID(String playername) {
        return Bukkit.getOfflinePlayer(playername).getUniqueId().toString();
    }
}
