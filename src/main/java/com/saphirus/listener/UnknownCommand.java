package com.saphirus.listener;

import com.saphirus.main.Data;
import com.saphirus.main.Main;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class UnknownCommand implements Listener {

    @EventHandler
    public void onUnknownCommand(PlayerCommandPreprocessEvent e) {
        String command = e.getMessage().substring(1).split(" ")[0];

        // Überprüfen, ob der Befehl in der CommandMap registriert ist
        Command registeredCommand = Main.commandMap.getCommand(command);
        if (registeredCommand == null) {
            // Wenn der Befehl nicht existiert, Nachricht senden und Event abbrechen
            e.getPlayer().sendMessage(Data.Prefix + "This command is unknown!");
            e.setCancelled(true);
        }
    }
}
