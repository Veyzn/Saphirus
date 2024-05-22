package com.saphirus.listener;

import com.saphirus.commands.GlobaldmuteCMD;
import com.saphirus.main.Data;
import com.saphirus.manager.PunishManager;
import com.saphirus.supportchat.SupportCMD;
import com.saphirus.utils.TempPlayerCache;
import com.saphirus.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        String message = e.getMessage().replaceAll("%","%%");
        String clan ="";
        TempPlayerCache tpc = new TempPlayerCache(p.getUniqueId().toString());

        if(tpc.inTeam()) {
            clan = "§8x§a" + tpc.getTeam();
        }

        String chatcolor = "§7";
        if(p.hasPermission("saphirus.staff")) {
            chatcolor = "§c";
        }

        PunishManager pm = new PunishManager(p.getUniqueId().toString());

        if(SupportCMD.rooms.containsKey(p)) {
            e.setCancelled(true);
            Player staff = SupportCMD.rooms.get(p);
            staff.sendMessage(Data.Supportchat + "§c" + p.getName() + ": §f" + e.getMessage());
            p.sendMessage(Data.Supportchat + "§c" + p.getName() + ": §f" + e.getMessage());
        }

        if(pm.isMuted()) {
            e.setCancelled(true);
            HashMap<String,Object> ban_info = (HashMap<String, Object>) pm.getMuteInfo();
            if(!SupportCMD.rooms.containsKey(p) || !GlobaldmuteCMD.status) {
                p.sendMessage(Data.Prefix + "You are still muted for §c" + Utils.calculateRemainingTime((Long) ban_info.get("MuteEnd")));
            }
        }

        if(GlobaldmuteCMD.status) {
            if(!p.hasPermission("saphirus.staff")) {
                e.setCancelled(true);
                if(!SupportCMD.rooms.containsKey(p)) {
                    p.sendMessage(Data.Globalmute + "The chat is currently §cdisabled");
                }
            }
        }

        e.setFormat("§8(" + Utils.getGroupPrefix(Utils.getUserRank(p.getName())) + "§8)" + clan + " §f" + p.getName() + " §8» " + chatcolor + message) ;

    }
}
