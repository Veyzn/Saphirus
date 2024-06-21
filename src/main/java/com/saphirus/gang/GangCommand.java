package com.saphirus.gang;

import com.saphirus.main.Data;
import com.saphirus.main.Main;
import com.saphirus.utils.Maths;
import com.saphirus.utils.TempPlayerCache;
import com.saphirus.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GangCommand implements CommandExecutor, TabCompleter {


    public static HashMap<String, ArrayList<String>> invited = new HashMap<>();
    ArrayList<String> disbandPlayer = new ArrayList<>();
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player)sender;
        TempPlayerCache tpc = new TempPlayerCache(p.getUniqueId().toString());
        String uuid = p.getUniqueId().toString();
        GangManager gm = null;


        if(tpc.inTeam()) {
            gm = new GangManager(tpc.getTeam());
        }
        GangManager finalGm = gm;
        switch (args.length) {
            default -> sendHelp(p);

            case 1 -> {
                switch (args[0].toLowerCase()) {
                    default -> sendHelp(p);

                    case "disband" -> {
                            if(tpc.inTeam()) {
                                assert gm != null;
                                if(isOwner(gm,uuid)) {
                                    if(!disbandPlayer.contains(uuid)) {
                                        p.sendMessage(Data.Gang + "Are you sure you want to disband the gang?");
                                        p.sendMessage(Data.Gang + "Confirm by re-entering the command §cThis will expire in 10 seconds");
                                        disbandPlayer.add(uuid);
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                disbandPlayer.remove(uuid);
                                            }
                                        }.runTaskLaterAsynchronously(Main.instance, 20*5);
                                    } else {
                                        gm.sendGangMessage("The gang was disbanded!");
                                        gm.disband();
                                        disbandPlayer.remove(uuid);
                                    }
                                }
                            } else sendNotInGang(p);
                    }

                    case "leave" -> {
                        if(tpc.inTeam()) {
                            assert gm != null;
                            if(!isOwner(gm,uuid)) {
                                gm.sendGangMessage("The player §c" + p.getName() + " left §athe gang");
                                tpc.setTeam("none");
                                gm.removeMember(uuid);
                            } else p.sendMessage(Data.Gang + "You can not leave the gang because you are the owner. Use §a/gang disband");

                        } else sendNotInGang(p);

                    }

                    case "stats" -> {
                        if(tpc.inTeam()) {
                            assert gm != null;
                            p.sendMessage("                   §a§l" + gm.getName() + " GANG");
                            p.sendMessage("");
                            p.sendMessage(Data.Gang + "Name: §a" + gm.getName() + " §8(§e#" + gm.getGangPlacement() + "§8) §8| §fTrophies: §e" + gm.getTrophies());
                            p.sendMessage(Data.Gang + "Owner: " + Utils.OnOffTest(UUID.fromString(gm.getOwner())) + " §8| §fLevel: §a" + gm.getTeamLevel());
                            p.sendMessage(Data.Gang + "Balance: §a$" + Utils.getZahl(gm.getBankMoney()) + " §8| §fGems: §2" + Utils.getZahl(gm.getBankGems()));
                            p.sendMessage(Data.Gang);
                            p.sendMessage(Data.Gang + "Members");
                            String members = "";
                            for(int i = 0; i < gm.getMembers().size(); i++) {
                                if(i != gm.getMembers().size()) {
                                    members += Utils.OnOffTest(UUID.fromString(gm.getMembers().get(i))) + " §8• ";
                                } else members += Utils.OnOffTest(UUID.fromString(gm.getMembers().get(i)));
                            }
                            if(!gm.getMembers().isEmpty()) {
                                p.sendMessage(Data.Gang + members);
                            } else p.sendMessage(Data.Gang + "§aThis gang has no members");

                            p.sendMessage("");
                        } else sendNotInGang(p);

                    }

                    case "trophy", "trophies" -> {
                        if(tpc.inTeam()) {
                            if(isOwner(gm,p.getUniqueId().toString())) {
                                gm.openTrophyInventory(p);
                            } else sendNotOwner(p);
                        } else sendNotInGang(p);
                    }

                    case "level" -> {
                        if(tpc.inTeam()) {
                            if(isOwner(gm,p.getUniqueId().toString())) {
                                gm.openLevelInventory(p);
                            } else sendNotOwner(p);
                        } else sendNotInGang(p);
                    }


                }
            } //LENGTH 1

            case 2 -> {
                switch (args[0].toLowerCase()) {
                    default -> sendHelp(p);

                    case "create" -> {
                        if(!tpc.inTeam()) {
                            if(isValidString(args[1])) {
                                String name = args[1];
                                GangManager new_gm = new GangManager(name);
                                if(!new_gm.exists()) {
                                    new_gm.createGang(uuid);
                                    tpc.setTeam(name);
                                    Bukkit.broadcastMessage(Data.Gang + "§a" + name + " Gang §fhas been founded by §a" + p.getName() + "§f. Be careful!");
                                } else p.sendMessage(Data.Gang + "This gang already exists!");
                            } else p.sendMessage(Data.Gang + "This name is invalid. Maximum of 5 letters and numbers.");
                        }
                    }

                    case "kick" -> {
                        if(tpc.inTeam()) {
                            if(isOwner(gm, p.getUniqueId().toString())) {
                                if(Utils.getUUID(args[1]) != null) {
                                    String target_uuid = Utils.getUUID(args[1]);
                                    TempPlayerCache ttc = new TempPlayerCache(target_uuid);
                                    if(gm.getMembers().contains(target_uuid)) {
                                        gm.removeMember(target_uuid);
                                        ttc.setTeam("none");
                                        gm.sendGangMessage("The player §c" + ttc.getName() + " §ahas been kicked!");
                                    } else p.sendMessage(Data.Gang + "This player is not in your gang!");
                                } else p.sendMessage(Data.Gang + "This player does not exist");
                            } else sendNotOwner(p);
                        } else sendNotInGang(p);
                    }
                    case "invite" -> {
                        if(tpc.inTeam()) {
                            if(isOwner(gm, p.getUniqueId().toString())) {
                                if(Bukkit.getPlayer(args[1]) != null) {
                                    Player t = Bukkit.getPlayer(args[1]);
                                    TempPlayerCache ttc = new TempPlayerCache(t.getUniqueId().toString());
                                    if(gm.getMembers().size() < 4) {
                                        if(!ttc.inTeam()) {
                                            if(!invited.containsKey(t.getUniqueId().toString()) || !invited.get(t.getUniqueId().toString()).contains(gm.getName())) {
                                                t.sendMessage(Data.Gang + "The gang §c" + gm.getName() + " §finvited you!");
                                                TextComponent message = new TextComponent("             Click ");

                                                // Create the clickable part
                                                TextComponent clickablePart = new TextComponent("§a§lHERE");
                                                clickablePart.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gang join " + gm.getName()));

                                                // Combine the components
                                                message.addExtra(clickablePart);
                                                message.addExtra(" §fto join the gang");

                                                t.spigot().sendMessage(message);
                                                p.sendMessage(Data.Gang + "You have invited §c" + t.getName() + " §fto the gang!");

                                                if(!invited.containsValue(t.getUniqueId().toString())) {
                                                    ArrayList<String> new_invited = new ArrayList<>();
                                                    new_invited.add(gm.getName());
                                                    invited.put(t.getUniqueId().toString(), new_invited);
                                                } else {
                                                    ArrayList<String> invites = invited.get(t.getUniqueId());
                                                    invites.add(gm.getName());
                                                    invited.put(t.getUniqueId().toString(), invites);
                                                }


                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        ArrayList<String> invites = invited.get(t.getUniqueId().toString());
                                                        invites.remove(finalGm.getName());
                                                        invited.put(t.getUniqueId().toString(), invites);
                                                    }
                                                }.runTaskLaterAsynchronously(Main.instance,20*30);

                                            } else p.sendMessage(Data.Gang + "You already invited this player!");
                                        } else p.sendMessage(Data.Gang + "This player is already in a gang!");
                                    } else p.sendMessage(Data.Gang + "Your gang already reached the maximum of 4 members!");
                                } else p.sendMessage(Data.Gang + "This player does not exist");
                            } else sendNotOwner(p);
                        } else sendNotInGang(p);
                    }

                    case "join" -> {
                        if(tpc.inTeam()) {
                            p.sendMessage(Data.Gang + "You are already in a gang");
                            return true;
                        }

                        GangManager join_gm = new GangManager(args[1]);

                        if(!invited.containsKey(p.getUniqueId().toString()) || !invited.get(p.getUniqueId().toString()).contains(join_gm.getName())) {
                            p.sendMessage(Data.Gang + "The gang did not invite you!");
                            return true;
                        }

                        if(!join_gm.exists()) {
                            p.sendMessage(Data.Gang + "That gang does not exist!");
                            return true;
                        }

                        if(join_gm.getMembers().size() == 4) {
                            p.sendMessage(Data.Gang + "The gang has already reached the maximum of 4 members");
                            return true;
                        }

                        tpc.setTeam(join_gm.getName());
                        join_gm.addMember(p.getUniqueId().toString());
                        join_gm.sendGangMessage("The player §c" + p.getName() + "§a joined the gang!");

                        ArrayList<String> invites = invited.get(p.getUniqueId().toString());
                        invites.remove(join_gm.getName());
                        invited.put(p.getUniqueId().toString(), invites);

                    }

                    case "stats" -> {
                        GangManager gm_stats = new GangManager(args[1]);
                        if(gm_stats.exists()) {
                            p.sendMessage("                   §a§l" + gm_stats.getName() + " GANG");
                            p.sendMessage("");
                            p.sendMessage(Data.Gang + "Name: §a" + gm_stats.getName() + " §8(§e#" + gm_stats.getGangPlacement() + "§8) §8| §fTrophies: §e" + gm_stats.getTrophies());
                            p.sendMessage(Data.Gang + "Owner: " + Utils.OnOffTest(UUID.fromString(gm_stats.getOwner())) + " §8| §fLevel: §a" + gm_stats.getTeamLevel());
                            p.sendMessage(Data.Gang);
                            p.sendMessage(Data.Gang + "Members");
                            String members = "";
                            for (int i = 0; i < gm_stats.getMembers().size(); i++) {
                                if (i != gm_stats.getMembers().size()) {
                                    members += Utils.OnOffTest(UUID.fromString(gm_stats.getMembers().get(i))) + " §8• ";
                                } else members += Utils.OnOffTest(UUID.fromString(gm_stats.getMembers().get(i)));
                            }
                            if (!gm_stats.getMembers().isEmpty()) {
                                p.sendMessage(Data.Gang + members);
                            } else p.sendMessage(Data.Gang + "§aThis gang has no members");

                            p.sendMessage("");
                        } else p.sendMessage(Data.Gang + "That gang does not exist!");
                    }
                }
            }

            case 3 -> {
                switch (args[0].toLowerCase()) {
                    default -> sendHelp(p);

                    case "deposit" -> {
                        if(!tpc.inTeam()) {
                            sendNotInGang(p);
                            return true;
                        }

                        if(!Maths.isLong(args[2])) {
                            p.sendMessage(Data.Gang + "This is an invalid number!");
                            return true;
                        }

                        Long amount = Long.parseLong(args[2]);

                        if(args[1].equalsIgnoreCase("money")) {
                            if(tpc.getMoney() < amount) {
                                p.sendMessage(Data.Gang + "You don't have enough money!");
                                return true;
                            }

                            gm.setBankMoney(gm.getBankMoney() + amount);
                            tpc.removeMoney(amount);
                            gm.sendGangMessage("§c" + p.getName() + " §adeposited §c$" + Utils.getZahl(amount) + " §ato the bank!");

                        } else if(args[1].equalsIgnoreCase("gems")) {
                            if(tpc.getGems() < amount) {
                                p.sendMessage(Data.Gang + "You don't have enough gems!");
                                return true;
                            }

                            gm.setBankGems(gm.getBankGems() + amount);
                            tpc.removeGems(amount);
                            gm.sendGangMessage("§c" + p.getName() + " §adeposited §c" + Utils.getZahl(amount) + " Gems §ato the bank!");
                        } else sendHelp(p);

                    }

                    case "withdraw" -> {
                        if(!tpc.inTeam()) {
                            sendNotInGang(p);
                            return true;
                        }

                        if(!Maths.isLong(args[2])) {
                            p.sendMessage(Data.Gang + "This is an invalid number!");
                            return true;
                        }

                        Long amount = Long.parseLong(args[2]);

                        if(args[1].equalsIgnoreCase("money")) {
                            if(gm.getBankMoney() < amount) {
                                p.sendMessage(Data.Gang + "Your gang does not have enough money in the bank!");
                                return true;
                            }
                            gm.setBankMoney(gm.getBankMoney() - amount);
                            tpc.addMoney(amount);
                            gm.sendGangMessage("§c" + p.getName() + " §awithdrew §c$" + Utils.getZahl(amount) + " §afrom the bank!");
                        } else if(args[1].equalsIgnoreCase("gems")) {
                            if(gm.getBankGems() < amount) {
                                p.sendMessage(Data.Gang + "Your gang does not have enough gems in the bank!");
                                return true;
                            }
                            gm.setBankGems(gm.getBankGems() - amount);
                            tpc.addGems(amount);
                            gm.sendGangMessage("§c" + p.getName() + " §awithdrew §c" + Utils.getZahl(amount) + " Gems §afrom the bank!");
                        } else sendHelp(p);
                    }
                }
            }

        }


        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> default_completer = new ArrayList<>();
        default_completer.add("create"); //DONE
        default_completer.add("disband"); //DONE
        default_completer.add("kick"); //DONE
        default_completer.add("invite");//DONE
        default_completer.add("kick"); //DONE
        default_completer.add("join"); //DONE
        default_completer.add("leave"); //DONE
        default_completer.add("stats"); //DONE
        default_completer.add("deposit");
        default_completer.add("withdraw");
        default_completer.add("trophies");
        default_completer.add("level");

        List gangs = new ArrayList<String>();
        for(GangManager gm : GangManager.getAllGangs()) {
            gangs.add(gm.getName());
        }
        List<String> completer = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                return default_completer;
            }

            case 2 -> {
                if(args[0].equalsIgnoreCase("stats") || args[0].equalsIgnoreCase("join")) {

                    return gangs;
                }

                if(args[0].equalsIgnoreCase("withdraw") || args[0].equalsIgnoreCase("deposit") ) {
                    return List.of("gems", "money");
                }

                if(args[0].equalsIgnoreCase("create")) {
                    return List.of("Your gang name");
                }

            }

            case 3 -> {
                if(args[0].equalsIgnoreCase("withdraw") || args[0].equalsIgnoreCase("deposit")) {
                    return List.of("amount");
                }
            }
        }

        return null;
    }

    //gang create <Name>
    //gang disband
    //gang invite
    //gang leave
    //gang kick
    //gang stats / gang stats <gang>
    //gang deposit <money | gems> <amount>
    //gang withdraw <money | gems> <amount>
    //gang trophies /LEADER ONLY
    //gang level
    public void sendHelp(Player p) {
        p.sendMessage(Data.Gang);
        p.sendMessage(Data.Gang + "/gang create <§aName§f> §8| §aCreate your own gang");
        p.sendMessage(Data.Gang + "/gang invite <§aPlayer§f> §8| §aInvite a player to your gang");
        p.sendMessage(Data.Gang + "/gang join <§aGang§f> §8| §aJoin a gang you have been invited to");
        p.sendMessage(Data.Gang + "/gang disband §8| §aDisband your gang");
        p.sendMessage(Data.Gang + "/gang leave §8| §aLeave your gang");
        p.sendMessage(Data.Gang + "/gang stats §8| §aShows the stats of your gang");
        p.sendMessage(Data.Gang + "/gang stats <§aGang§f> §8| §aShow stats of another gang");
        p.sendMessage(Data.Gang + "/gang deposit <§aCurrency§f> <§aAmount> §8| §aDeposit a currency in to the bank");
        p.sendMessage(Data.Gang + "/gang withdraw <§aCurrency§f> <§aAmount> §8| §aWithdraw a currency from the bank");
        p.sendMessage(Data.Gang + "/gang trophies §8| §aBuy trophies for the leaderboard");
        p.sendMessage(Data.Gang + "/gang level §8| §aLevel up your gang");
        p.sendMessage(Data.Gang);
        p.sendMessage(Data.Gang + "Type §a# §fto send a gang message");
        p.sendMessage(Data.Gang);
    }

    public void sendNotInGang(Player p) {p.sendMessage(Data.Gang + "§cYou are not in a gang! §c/gang create <Name>");}
    public void sendNotOwner(Player p) {p.sendMessage(Data.Gang + "§cYou are not the owner of the gang");}

    public boolean isOwner(GangManager gm, String uuid) {return gm.getOwner().equalsIgnoreCase(uuid);}

    public boolean isValidString(String input) {
        if (input.length() > 5) {
            return false;
        }

        if (!input.matches("[a-zA-Z0-9]+")) {
            return false;
        }

        return true;
    }
}
