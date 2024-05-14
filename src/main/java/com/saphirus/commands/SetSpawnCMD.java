package com.saphirus.commands;

import com.saphirus.main.Config;
import com.saphirus.main.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetSpawnCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Player p = (Player)commandSender;


        if(p.hasPermission("saphirus.admin")) {
            p.sendTitle("§a§lSUCCESSFUL", "§fYou have set the §cSpawn", 10,40,10);
            Config.setSpawn(p);
        } else p.sendMessage(Data.noPerms);

        return false;
    }
}
