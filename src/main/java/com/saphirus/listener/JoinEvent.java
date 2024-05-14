package com.saphirus.listener;

import com.saphirus.main.Config;
import com.saphirus.main.Data;
import com.saphirus.main.Main;
import com.saphirus.manager.PunishManager;
import com.saphirus.supportchat.SupportCMD;
import com.saphirus.utils.Board;
import com.saphirus.utils.PlayerCache;
import com.saphirus.utils.TempPlayerCache;
import com.saphirus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class JoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PlayerCache pc = new PlayerCache(p.getUniqueId().toString());
        e.setJoinMessage(null);
        if(!pc.exists()) {
            pc.create(p);
            Config.setJoins(Config.getJoins() +1);
            Bukkit.broadcastMessage(Data.Prefix + "§8§l!!§a§lNEW§8§l!! §f" + p.getName() + " §8[§b§l#§f" + Config.getJoins() + "§8]");
        } else {
            if(p.hasPermission("saphirus.staff")) {
                Bukkit.broadcastMessage("§8[§a§l+§8] " + Utils.getGroupPrefix(Utils.getUserRank(p.getName())) + "§f " + p.getName());
            }
        }



        TempPlayerCache tpc = new TempPlayerCache(p.getUniqueId().toString());
        if(!tpc.isDataLoaded()) {
            tpc.loadData();
        }


                Board board = new Board();
                board.set(p);


    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage("§8[§c§l-§8] " + Utils.getGroupPrefix(Utils.getUserRank(p.getName())) + "§f " + p.getName());


        SupportCMD.waiting.remove(p);

        if(SupportCMD.rooms.containsKey(p)) {
            Player staff = SupportCMD.rooms.get(p);
            staff.sendMessage(Data.Supportchat + "§c" + p.getName() + " §fleft the server. Support chat is closed");
            SupportCMD.rooms.remove(p);
            SupportCMD.rooms.remove(staff);
        }

        new BukkitRunnable() {
            int timer = 0;
            @Override
            public void run() {
                if(!p.isOnline()) {
                    timer++;
                    if(timer == 20) {

                        TempPlayerCache tpc = TempPlayerCache.data.get(p.getUniqueId().toString());
                        tpc.unloadData();
                        this.cancel();
                    }
                } else {
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.instance,20*10,20*10);

    }



    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent e) {
        if(Config.isMaintenanceEnabled()) {
            if(!Bukkit.getServer().getWhitelistedPlayers().contains(Bukkit.getOfflinePlayer(e.getUniqueId()))) {
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Data.getHeader() + "\n\n"+
                        "§fWe are currently under §cMaintenance\n" +
                        "§fWe will be back soon!\n"+
                        "\n"+
                        "§fFeel free to join our Discord: §bdiscord.saphirus.com");
            }
        }

        PunishManager pm = new PunishManager(e.getUniqueId().toString());
        if(pm.isBanned()) {
            HashMap<String,Object> ban_info = (HashMap<String, Object>) pm.getBanInformation();
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, Data.getHeader() + "\n\n" +
                    "§fYou have been banned!\n" +
                    "§cReason: §f" + ban_info.get("Reason") + "\n" +
                    "§cBanned by: §f" + ban_info.get("Operator") + "\n" +
                    "§cDuration: §f" + Utils.calculateRemainingTime((Long) ban_info.get("BanEnd")) + "\n" +
                    "\n" +
                    "§cTry to appeal on our discord: §fdiscord.saphirus.com");
        }
    }
}
