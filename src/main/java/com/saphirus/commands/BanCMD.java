package com.saphirus.commands;

import com.saphirus.main.Data;
import com.saphirus.manager.PunishManager;
import com.saphirus.utils.Maths;
import com.saphirus.utils.PlayerCache;
import com.saphirus.utils.TempPlayerCache;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)commandSender;

        if(p.hasPermission("saphirus.ban")) {
            if(args.length >= 4) {
                String uuid = getUUID(args[0]);
                Player t = Bukkit.getPlayer(uuid);
                PlayerCache pc = new PlayerCache(uuid);
                if(pc.exists()) {
                    TempPlayerCache tpc = new TempPlayerCache(uuid);
                    if(Maths.isInt(args[1])) {
                        String reason = "";
                        for(int i = 3; i < args.length; i++) {
                            reason = reason + args[i];
                            if(i + 1 < args.length) {
                                reason = reason + " ";
                            }
                        }
                        PunishManager pm = new PunishManager(uuid);
                        String unit = args[2];

                        if(!pm.isBanned()) {
                            LuckPerms luckPerms = LuckPermsProvider.get();
                            User user = luckPerms.getUserManager().loadUser(UUID.fromString(uuid)).join();
                            if(!user.getCachedData().getPermissionData().getPermissionMap().containsKey("saphirus.staff") || p.isOp()) {
                                long time = System.currentTimeMillis();

                                switch (unit) {
                                    case "days", "d":
                                        time += (long) 1000L *60*60*24*Integer.parseInt(args[1]);
                                        break;
                                    case "hours", "h":
                                        time += (long) 1000L *60*60*Integer.parseInt(args[1]);
                                        break;
                                    case "minutes", "m":
                                        time += (long) 1000L *60*Integer.parseInt(args[1]);
                                        break;
                                }

                                pm.addBan(uuid,p,time,reason);
                                pc.addBans(1);
                            } else p.sendMessage(Data.Prefix + "You can't ban this player!");
                        } else p.sendMessage(Data.Prefix + "This player is already banned!");
                    } else p.sendMessage(Data.invalidNumber);
                } else p.sendMessage(Data.Prefix + "This player never joined the server!");
            } else p.sendMessage(Data.Usage + "/ban <Player> <Duration> <Days | Hours | Minutes> <Reason>");

        } else p.sendMessage(Data.noPerms);

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        List<String> completer = new ArrayList<>();
        Player p = (Player) commandSender;
        if(p.hasPermission("saphirus.ban")) {
            if (strings.length == 2) {
                completer.add("How long should he be banned for? (For example: 1 , 2 , 3 ,4)");

                return completer;
            }

            if (strings.length == 3) {
                completer.add("days");
                completer.add("hours");
                completer.add("minutes");
                return completer;
            }

            if (strings.length == 4) {
                completer.add("For what reason does he get banned");
                return completer;
            }
        }

        return null;
    }

    private String getUUID(String playername) {
        return Bukkit.getOfflinePlayer(playername).getUniqueId().toString();
    }
}
