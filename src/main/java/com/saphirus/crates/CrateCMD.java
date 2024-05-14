package com.saphirus.crates;

import com.saphirus.main.Data;
import com.saphirus.utils.CenterText.TextCenter;
import com.saphirus.utils.Maths;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrateCMD implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player)sender;

        if(p.hasPermission("saphirus.crates")) {
            if(args.length == 0) {
                usage(p);
            } else if(args.length == 1) {
                switch (args[0].toLowerCase()) {
                    case "editor":
                        CrateManager.openAllCrates(p);
                        break;
                }
            } else if(args.length == 2) {
                CrateManager cm = new CrateManager(args[1]);
                switch (args[0].toLowerCase()) {
                    case "create" -> {

                        if (!cm.exists()) {
                            cm.create();
                            p.sendMessage(Data.Crates + "You successfully created §c" + args[1]);
                        } else p.sendMessage(Data.Crates + "This create already exists!");
                        break;
                    }
                    case "delete" -> {
                        if (cm.exists()) {
                            cm.delete();
                            p.sendMessage(Data.Crates + "You successfully deleted §c" + args[1]);
                        } else p.sendMessage(Data.Crates + "This crate doesn't exist!");
                        break;
                    }
                    case "setcrateitem" -> {
                        if(cm.exists()) {
                            if(p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                                cm.setItem(p.getInventory().getItemInMainHand());
                                p.sendMessage(Data.Crates + "You have changed the crate item of §c" + args[1]);
                            } else p.sendMessage(Data.Crates + "You have to hold an item in your hand!");
                        } else p.sendMessage(Data.Crates + "This crate doesn't exist!");
                        break;
                    }

                    case "additem" -> {
                        if(cm.exists()) {
                            if(p.getInventory().getItemInMainHand().getType() != Material.AIR) {
                                cm.addItem(new CrateItem(p.getInventory().getItemInMainHand()));
                                p.sendMessage(Data.Crates + "You have added an item to §c" + args[1]);
                            } else p.sendMessage(Data.Crates + "You have to hold an item in your hand!");
                        } else p.sendMessage(Data.Crates + "This crate doesn't exist!");
                    }

                    case "addblock" -> {
                        if(cm.exists()) {
                            CrateLocations cl = new CrateLocations();
                            if(p.getTargetBlockExact(10) != null) {
                                if (!cl.isLocationCrateBlock(p.getTargetBlockExact(10).getLocation())) {
                                    cl.addLocation(p.getTargetBlockExact(10).getLocation(), cm.getName().toLowerCase());
                                    p.sendMessage(Data.Crates + "You successfully binded this Block to §c" + cm.getName());
                                } else p.sendMessage(Data.Crates + "This block is already binded to a crate!");
                            } else p.sendMessage(Data.Crates + "You have to look at a block!");
                        } else p.sendMessage(Data.Crates + "This crate doesn't exist!");
                    }
                }
            } else if (args.length == 3) {
                switch (args[0].toLowerCase()) {
                    case "giveall" -> {
                        CrateManager cm = new CrateManager(args[1]);
                        if(cm.exists()) {
                            if(Maths.isInt(args[2])) {
                                int amount = Integer.parseInt(args[2]);
                                ItemStack item = cm.getKey();

                                for(Player all : Bukkit.getOnlinePlayers()) {
                                    for(int i = 0; i < amount;i++) {
                                        all.getInventory().addItem(item);
                                        all.playSound(all.getLocation(), Sound.BLOCK_NOTE_BLOCK_BIT, 5.0F, 5.0F);
                                    }
                                }
                                Bukkit.broadcastMessage("");
                                Bukkit.broadcastMessage(TextCenter.getCentredMessage("§5§lKEYALL"));
                                Bukkit.broadcastMessage(TextCenter.getCentredMessage("§fEveryone just got §c" + amount + "x §c" + cm.getColoredname()));
                                Bukkit.broadcastMessage("");
                            } else p.sendMessage(Data.invalidNumber);
                        }else p.sendMessage(Data.Crates + "This crate doesn't exist!");
                    }
                }

            } else if(args.length == 4) {
                CrateManager cm = new CrateManager(args[2]);
                switch (args[0].toLowerCase()) {
                    case "give" -> {
                        if(Bukkit.getPlayer(args[1]) != null) {
                            Player t = Bukkit.getPlayer(args[1]);
                            if(cm.exists()) {
                                if (Maths.isInt(args[3])) {
                                    int amount = Integer.parseInt(args[3]);
                                    ItemStack item = cm.getKey();
                                    item.setAmount(amount);
                                    t.getInventory().addItem(item);
                                    p.sendMessage(Data.Crates + "You gave §c" + t.getName() + " " + amount + "x " + cm.getColoredname());
                                    t.sendMessage(Data.Crates + "You received §c" + amount + "x " + cm.getColoredname());
                                }
                            }
                        } else p.sendMessage(Data.Offline);
                    }

                    case "givesilent" -> {
                        if(Bukkit.getPlayer(args[1]) != null) {
                            Player t = Bukkit.getPlayer(args[1]);
                            if(cm.exists()) {
                                if (Maths.isInt(args[3])) {
                                    int amount = Integer.parseInt(args[3]);
                                    ItemStack item = cm.getKey();
                                    item.setAmount(amount);
                                    t.getInventory().addItem(item);
                                    p.sendMessage(Data.Crates + "You gave §c" + t.getName() + " " + amount + "x " + cm.getColoredname() + " §fsilently");
                                }
                            }
                        } else p.sendMessage(Data.Offline);
                    }
                }
            }
        } else p.sendMessage(Data.noPerms);

        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> crates = CrateManager.getCratenames();
        Player p = (Player) commandSender;
        if(p.hasPermission("saphirus.crate")) {
            if (args.length == 1) {
                return List.of("create", "delete", "editor", "give", "giveall", "givesilent", "setirateitem", "additem", "addblock");
            }
            if (args.length > 1) {

                switch (args[0].toLowerCase()) {
                    case "create" -> {
                        return List.of("name");
                    }
                    case "delete", "additem", "addblock", "setcrateitem" -> {
                        return crates;
                    }
                    case "give", "givesilent" -> {
                        if (args.length == 3)
                            return crates;
                        if (args.length == 4)
                            return List.of("amount");
                    }
                    case "giveall" -> {
                        if (args.length == 2)
                            return crates;
                        if (args.length == 3)
                            return List.of("amount");
                    }
                }
            }
        }

        return null;
    }

    public static void usage(Player p) {
        //crate create <Name>
        //crate delete
        //crate editor
        //crate give <Player> <Crate> <Amount>
        //crate giveall <Crate> <Amount>
        //crate givesilent <Player> <Crate> <Amount>

        p.sendMessage(Data.Crates + "/hcrate §ccreate <Name> §8| §fCreate a crate");
        p.sendMessage(Data.Crates + "/hcrate §cdelete <Name> §8| §fDelete a crate");
        p.sendMessage(Data.Crates + "/hcrate §ceditor §8| §fOpen the crate editor");
        p.sendMessage(Data.Crates + "/hcrate §cgive <Player> <Crate> <Amount> §8| §fGive a player a Crate");
        p.sendMessage(Data.Crates + "/hcrate §cgiveall <Crate> <Amount> §8| §fGive crates to all players");
        p.sendMessage(Data.Crates + "/hcrate §cgivesilent <Player> <Crate> <Amount> §8| §fGive a crate to a player silently ");
        p.sendMessage(Data.Crates + "/hcrate §csetCrateItem §c<Crate> §8| §fSet crate key Item");
        p.sendMessage(Data.Crates + "/hcrate §caddItem <Crate> §8| §fAdd item to crate");
        p.sendMessage(Data.Crates + "/hcrate §caddBlock <Crate> §8| §fBind the block you looking at to a Crate");
    }
}
