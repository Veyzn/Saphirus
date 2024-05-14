package com.saphirus.commands;

import com.saphirus.main.Data;
import com.saphirus.manager.PunishManager;
import com.saphirus.utils.PlayerCache;
import com.saphirus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PlayerInfoCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {


        Player p = (Player)sender;

        if(p.hasPermission("saphirus.playerinfo")) {
            if(args.length == 1) {
                PlayerCache pc = new PlayerCache(getUUID(args[0]));
                if(pc.exists()) {
                    PunishManager pm = new PunishManager(getUUID(args[0]));
                    HashMap<String, Object> data = pc.getPlayerData();
                    p.sendMessage(Data.Prefix + "Name: §c" + data.get("Name"));
                    p.sendMessage(Data.Prefix + "First Joined: §c" + pc.getJoinDate());
                    p.sendMessage(Data.Prefix + "Playtime: §c" + Utils.convertSecondsToTime((Long) data.get("Playtime")));
                    p.sendMessage(Data.Prefix + "Clan: §c" + data.get("Clan"));
                    p.sendMessage(Data.Prefix + "Linked: §c" + pc.isLinkedString());
                    p.sendMessage("");
                    p.sendMessage(Data.Prefix + "Money: §a$" + Utils.getZahl((Long) data.get("Money")));
                    p.sendMessage(Data.Prefix + "Gems: §2" + Utils.getZahl((Long)data.get("Gems")));
                    p.sendMessage("");
                    p.sendMessage(Data.Prefix + "Blocks: §c" + data.get("Blocks"));
                    p.sendMessage(Data.Prefix + "PVE Kills: §c" + data.get("PVE_Kills"));
                    p.sendMessage(Data.Prefix + "Fishing: §c" + data.get("Fishing"));
                    p.sendMessage("");
                    p.sendMessage(Data.Prefix + "Kills: §c" + data.get("Kills"));
                    p.sendMessage(Data.Prefix + "Deaths: §c" + data.get("Deaths"));
                    p.sendMessage("");
                    p.sendMessage(Data.Prefix + "Bans: " + data.get("Bans"));
                    p.sendMessage(Data.Prefix + "Mutes: " + data.get("Mutes"));
                    p.sendMessage(Data.Prefix + "Warns: " + data.get("Warns_Total"));
                    if(pm.isBanned()) {
                        p.sendMessage("");
                        HashMap<String,Object> ban_info = (HashMap<String, Object>) pm.getBanInformation();
                        p.sendMessage(Data.Prefix + "§4§lBANNED §8(§fEnd: §c" + Utils.calculateRemainingTime((Long) ban_info.get("BanEnd")) + " §8| §fReason: §c" + ban_info.get("Reason") + " §8| §fDate: " + ban_info.get("BanDate") + " §8| §fOperator: §c" + ban_info.get("Operator") + "§8)");

                    }

                    if(pm.isMuted()) {
                        p.sendMessage("");
                        HashMap<String,Object> ban_info = (HashMap<String, Object>) pm.getMuteInfo();
                        p.sendMessage(Data.Prefix + "§5§lMUTED §8(§fEnd: §c" + Utils.calculateRemainingTime((Long) ban_info.get("MuteEnd")) + " §8| §fReason: §c" + ban_info.get("Reason") + " §8| §fDate: " + ban_info.get("MuteDate") + " §8| §fOperator: §c" + ban_info.get("Operator") + "§8)");
                    }

                    if((int)data.get("Warns_Total") > 0) {
                        p.sendMessage("");
                        Map<Long, Map<String,Object>> warns = pm.getWarnsInformation();
                        for (Map.Entry<Long, Map<String, Object>> entry : warns.entrySet()) {
                            long warnID = entry.getKey();
                            Map<String, Object> warnData = entry.getValue();
                            p.sendMessage(Data.Prefix + "§6§lWARN §fID: " + warnID + " §8(§fReason: §c" + warnData.get("Reason") + " §8| §fAmount: §c" + warnData.get("Amount") + " §8| §fDate: §c" + warnData.get("WarnDate") + " §8| §fOperator: §c" + warnData.get("Operator") + "§8)");
                        }

                    }

                } else p.sendMessage(Data.Prefix + "This player never joined the server!");
            } else p.sendMessage(Data.Usage + "/playerinfo <Player>");
        } else p.sendMessage(Data.noPerms);


        return false;
    }

    private String getUUID(String playername) {
        return Bukkit.getOfflinePlayer(playername).getUniqueId().toString();
    }
}
