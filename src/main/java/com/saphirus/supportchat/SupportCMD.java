package com.saphirus.supportchat;

import com.saphirus.main.Data;
import com.saphirus.utils.CenterText.TextCenter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SupportCMD implements CommandExecutor, TabCompleter {

    public static ArrayList<Player> waiting = new ArrayList<>();
    public static HashMap<Player, Player> rooms = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;

        if(args.length == 0) {
            if(p.hasPermission("saphirus.staff")) {
                p.sendMessage(Data.Usage + "/support <accept | list | end>");
            } else p.sendMessage(Data.Usage + "/support <open | cancel>");
        } else if(args.length == 1) {
            switch(args[0].toLowerCase()) {
                case "open" -> {
                    if (!p.hasPermission("saphirus.staff")) {
                        if (!waiting.contains(p) && !rooms.containsKey(p)) {
                            p.sendMessage(Data.Supportchat + "You have opened a ticket. A staff will help you!");
                            waiting.add(p);
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                if (all.hasPermission("saphirus.staff")) {
                                    all.sendMessage("");
                                    all.sendMessage(TextCenter.getCentredMessage("The player §c" + p.getName() + " §f is waiting for support"));
                                    TextComponent text = new TextComponent(TextCenter.getCentredMessage("§8(§a§lACCEPT§8)"));
                                    text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/support accept " + p.getName()));
                                    ((Player) all).spigot().sendMessage(text);
                                    all.sendMessage("");
                                    all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5.0F, 5.0F);
                                }
                            }
                        } else p.sendMessage(Data.Supportchat + "You already in a support room or waiting for it");
                    } else p.sendMessage(Data.Supportchat + "You are a staff. You can't send a support request");
                }
                case "cancel","close" -> {
                    if(waiting.contains(p)) {
                        p.sendMessage(Data.Supportchat + "You have canceled your support request");
                        waiting.remove(p);
                    }

                    if(rooms.containsKey(p)) {
                        Player staff = rooms.get(p);
                        p.sendMessage(Data.Supportchat + "You have closed the support chat. Hopefully we were able to help you");
                        staff.sendMessage(Data.Supportchat + "The player §c" + p.getName() + " §fclosed the support chat");
                        rooms.remove(p);
                        rooms.remove(staff);
                    }
                }

                case "list" -> {
                    if(p.hasPermission("saphirus.staff")) {
                        if(!waiting.isEmpty()) {
                            p.sendMessage(Data.Supportchat + "Here are the open support requests:");
                            for(Player waiter : waiting) {
                                p.sendMessage(Data.Supportchat + "- §c" + waiter.getName() + "§8(§a/support accept §c" + waiter.getName() + "§8)");
                            }
                        } else p.sendMessage(Data.Supportchat + "Currently no one is waiting for support!");
                    } else p.sendMessage(Data.noPerms);
                }

                case "end" -> {
                    if(p.hasPermission("saphirus.staff")) {
                        if(rooms.containsKey(p)) {
                            Player t = rooms.get(p);
                            t.sendMessage(Data.Supportchat + "§c" + p.getName() + " §fhas closed the support chat. Hopefully we were able to help you");
                            p.sendMessage(Data.Supportchat + "You have closed the support chat with §c" + t.getName());
                            rooms.remove(p);
                            rooms.remove(t);
                        }
                    } else p.sendMessage(Data.noPerms);
                }
            }
        } else if(args.length == 2) {
            switch(args[0].toLowerCase()) {
                case "join","accept" -> {
                    if(p.hasPermission("saphirus.staff")) {
                        if(Bukkit.getPlayer(args[1]) != null) {
                            Player t = Bukkit.getPlayer(args[1]);
                            if(waiting.contains(t)) {
                                if(!rooms.containsKey(p)) {
                                    p.sendMessage(Data.Supportchat + "You are now in a talk with §c" + t.getName());
                                    t.sendMessage(Data.Supportchat + "You are now in a talk with §c" + p.getName());
                                    waiting.remove(t);
                                    rooms.put(p,t);
                                    rooms.put(t,p);
                                    p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5.0F, 5.0F);
                                    t.playSound(t.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5.0F, 5.0F);
                                } else p.sendMessage(Data.Supportchat + "You are already in a support chat");
                            } else p.sendMessage(Data.Supportchat + "This isn't waiting for support!");
                        } else p.sendMessage(Data.Offline);
                    } else p.sendMessage(Data.noPerms);
                }
            }
        } else {
            if(p.hasPermission("saphirus.staff")) {
                p.sendMessage(Data.Usage + "/support <accept | list | end>");
            } else p.sendMessage(Data.Usage + "/support <open | cancel>");
        }

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }
}
