package com.saphirus.listener;

import com.saphirus.main.Data;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class TeleportEvent implements Listener {


    @EventHandler
    public void onTeleportMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        if(Data.teleporting.containsKey(p.getUniqueId().toString())) {
            if(p.getLocation().distance(Data.teleporting.get(p.getUniqueId().toString())) >= 0.5D) {
                Data.teleporting.remove(p.getUniqueId().toString());
                p.sendTitle("§c§lCANCELED", "§fYou moved! §cTeleportation §fcanceled", 10,20,10);
                p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_DESTROY,3,1);
            }
        }
    }
}
