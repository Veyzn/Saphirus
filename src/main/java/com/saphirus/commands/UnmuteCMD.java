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

public class UnmuteCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player) commandSender;
        if(p.hasPermission("saphirus.mute")) {
            if(args.length == 1) {
                if(Bukkit.getPlayer(args[0]) != null) {
                    Player t = Bukkit.getPlayer(args[0]);
                    PunishManager pm = new PunishManager(t.getUniqueId().toString());
                    PlayerCache pc = new PlayerCache(t.getUniqueId().toString());
                    if (pm.isMuted()) {
                        pm.removePlayerMute();
                        p.sendMessage(Data.Prefix + "You just unmuted §c" + args[0]);
                        pc.removeMutes(1);
                    } else p.sendMessage(Data.Prefix + "This player isn't banned!");
                } else p.sendMessage(Data.Offline);
            } else p.sendMessage(Data.Usage + "/unban <Player>");
        } else p.sendMessage(Data.noPerms);

        return false;
    }


    private String getUUID(String playername) {
        return Bukkit.getOfflinePlayer(playername).getUniqueId().toString();
    }
}
