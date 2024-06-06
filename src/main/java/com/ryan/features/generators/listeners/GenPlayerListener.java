package com.ryan.features.generators.listeners;

import com.ryan.features.generators.managers.GenPlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GenPlayerListener implements Listener {

    @EventHandler( priority = EventPriority.MONITOR )
    public void onPlayerPreLoginEvent( AsyncPlayerPreLoginEvent event ) {
        // If the player will be logging in, loads them into the cache
        if ( event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED ) {
            GenPlayerManager.loadIntoCache( event.getUniqueId() );
        }
    }

    @EventHandler
    public void onPlayerQuit( PlayerQuitEvent event ) {
        GenPlayerManager.updateCacheLocation( event.getPlayer() );
    }
}