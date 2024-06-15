package com.saphirus.gang;

import com.saphirus.main.Data;
import com.saphirus.main.Main;
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
                            //RANKING HAS TO BE DONE
                            p.sendMessage("                   §a§l" + gm.getName() + " GANG");
                            p.sendMessage("");
                            p.sendMessage(Data.Gang + "Name: §a" + gm.getName() + " §8(§e#1§8) §8| §fTrophies: §e" + gm.getTrophies());
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

                    case "trophy" -> {
                        //HAS TO BE DONE
                    }

                    case "level" -> {
                        //HAS TO BE DONE
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
                                    if(gm.getMembers().size() > 4) {
                                        if(!ttc.inTeam()) {
                                            if(!invited.containsValue(t.getUniqueId().toString()) || !invited.get(t.getUniqueId().toString()).contains(gm.getName())) {

                                                TextComponent message = new TextComponent(Data.Gang + "The gang §c " + gm.getName() + " §finvited you to! Click");

                                                // Create the clickable part
                                                TextComponent clickablePart = new TextComponent("§a§lHERE");
                                                clickablePart.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gang join " + gm.getName()));

                                                // Combine the components
                                                message.addExtra(clickablePart);
                                                message.addExtra(" §fto join the gang");

                                                t.spigot().sendMessage(message);

                                            } else p.sendMessage(Data.Gang + "You already invited this player!");
                                        } else p.sendMessage(Data.Gang + "This player is already in a gang!");
                                    } else p.sendMessage(Data.Gang + "Your gang already reached the maximum of 4 members!");
                                } else p.sendMessage(Data.Gang + "This player does not exist");
                            } else sendNotOwner(p);
                        } else sendNotInGang(p);
                    }

                    case "join" -> {

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
        default_completer.add("invite");
        default_completer.add("kick");
        default_completer.add("join");
        default_completer.add("leave"); //DONE
        default_completer.add("stats"); //DONE
        default_completer.add("deposit");
        default_completer.add("withdraw");
        default_completer.add("trophies");
        default_completer.add("level");

        List<String> completer = new ArrayList<>();
        switch (args.length) {
            case 1 -> {
                return default_completer;
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
