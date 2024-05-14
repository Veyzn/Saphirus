package com.saphirus.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DiscordCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;

        p.sendMessage("");
        p.sendMessage("                §b§lJoin our Discord");
        p.sendMessage("               §fdiscord.saphirus.com");
        p.sendMessage("          §fUse §b/link §ffor incredible §brewards");
        p.sendMessage("");

        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5.0F, 5.0F);
        return false;
    }
}
