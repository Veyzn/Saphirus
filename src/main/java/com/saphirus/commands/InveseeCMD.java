package com.saphirus.commands;

import com.saphirus.fastinv.FastInv;
import com.saphirus.main.Data;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InveseeCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;


        if(p.hasPermission("saphirus.invsee")) {
            if(args.length == 1) {
                if(Bukkit.getPlayer(args[0]) != null){
                   Player t = Bukkit.getPlayer(args[0]);
                   if(p.hasPermission("saphirus.invsee.admin")) {
                       p.openInventory(t.getInventory());
                   } else {
                       FastInv inv = new FastInv(4*9, "§cInvsee §f| §c" + t.getName());
                       inv.getInventory().setContents(t.getInventory().getContents());
                       inv.addClickHandler(click -> {
                           click.setCancelled(true);
                       });
                       inv.open(p);
                   }

                } else p.sendMessage(Data.Offline);
            } else p.sendMessage(Data.Usage + "/invsee <Player>");
        } else p.sendMessage(Data.noPerms);



        return false;
    }
}
