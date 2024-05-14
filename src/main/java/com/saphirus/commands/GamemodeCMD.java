package com.saphirus.commands;

import com.saphirus.main.Data;
import com.saphirus.utils.Utils;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GamemodeCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;

        if(p.hasPermission("saphirus.gamemode")) {
            if(args.length == 1) {
                if (args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival")) {
                    p.sendMessage(Data.Gamemode + "You are now in §cSurvival-Mode");
                    p.setGameMode(GameMode.SURVIVAL);
                } else if(args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative")) {
                    p.sendMessage(Data.Gamemode + "You are now in §cCreative-Mode");
                    p.setGameMode(GameMode.CREATIVE);
                } else if(args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("spectator")) {
                    p.sendMessage(Data.Gamemode + "You are now in §cSpectator-Mode");
                    p.setGameMode(GameMode.SPECTATOR);
                } else p.sendMessage(Data.Usage +  Utils.fancy("/gamemode [0 | 1 | 2]"));
            } else p.sendMessage(Data.Usage +  Utils.fancy("/gamemode [0 | 1 | 2]"));
        } else p.sendMessage(Data.noPerms);


        return false;
    }
}
