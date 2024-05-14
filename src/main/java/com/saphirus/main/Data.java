package com.saphirus.main;

import com.saphirus.utils.Utils;
import org.bukkit.Location;

import java.util.HashMap;

public class Data {


    public static HashMap<String, Location> teleporting = new HashMap<String, Location>();

    //{"version":2,"colors":["#1488CC","#FFFFFF"],"text":"Saphirus","format":"§x§$1§$2§$3§$4§$5§$6$f$c","formatchar":"&","customFormat":false,"prefix":"","bold":true,"italic":false,"underline":false,"strikethrough":false}
    public static String Prefix = "§8| §x§1§4§8§8§C§C§lG§x§3§B§9§C§D§5§lE§x§6§2§B§0§D§D§lN§x§8§A§C§4§E§6§lL§x§B§1§D§7§E§E§lA§x§D§8§E§B§F§7§lB§x§F§F§F§F§F§F§lZ §8» §f";
    public static String Trade = "§8| §x§0§0§F§F§2§6§lT§x§0§0§F§F§3§6§lR§x§0§0§F§F§4§5§lA§x§0§0§F§F§5§5§lD§x§0§0§F§F§6§4§lE §8» §f";
    public static String noPerms = "§8| §x§F§F§0§0§0§0§lE§x§D§D§0§4§0§4§lR§x§B§A§0§8§0§8§lR§x§9§8§0§B§0§B§lO§x§7§5§0§F§0§F§lR §8» §fYou are not allowed to do that!";
    public static String Usage = "§8| §x§0§0§C§1§F§F§lU§x§0§0§D§1§F§F§lS§x§0§0§E§0§F§F§lA§x§0§0§F§0§F§F§lG§x§0§0§F§F§F§F§lE §8» §fPlease use: §c";
    public static String Gamemode = "§8| §x§F§F§D§9§0§0§lG§x§F§F§C§3§0§0§lA§x§F§F§A§D§0§0§lM§x§F§F§9§7§0§0§lE§x§F§F§8§0§0§0§lM§x§F§F§6§A§0§0§lO§x§F§F§5§4§0§0§lD§x§F§F§3§E§0§0§lE §8» §f";
    public static String Fly = "§8| §x§1§8§F§F§D§A§lF§x§0§C§F§F§D§D§lL§x§0§0§F§F§E§0§lY §8» §f";
    public static String Offline = "§8| §x§F§F§0§0§0§0§lO§x§E§E§0§2§0§2§lF§x§D§E§0§4§0§4§lF§x§C§D§0§6§0§6§lL§x§B§C§0§8§0§8§lI§x§A§C§0§A§0§A§lN§x§9§B§0§C§0§C§lE §8» §fThis player is §coffline";
    public static String Feed = "§8| §x§0§0§F§F§3§0§lF§x§1§7§F§F§2§5§lE§x§2§D§F§F§1§B§lE§x§4§4§F§F§1§0§lD §8» §f";
    public static String Globalmute = "§8| §x§F§1§0§0§F§F§lG§x§E§A§0§2§F§F§lL§x§E§2§0§4§F§F§lO§x§D§B§0§5§F§F§lB§x§D§3§0§7§F§F§lA§x§C§C§0§9§F§F§lL§x§C§4§0§B§F§F§lM§x§B§D§0§C§F§F§lU§x§B§5§0§E§F§F§lT§x§A§E§1§0§F§F§lE §8» §f";
    public static String invalidNumber = Prefix  + Utils.fancy("This is an invalid Number");
    public static String Random = "§8| §x§F§F§F§8§0§0§lR§x§E§9§F§9§0§B§lA§x§D§3§F§B§1§5§lN§x§B§E§F§C§2§0§lD§x§A§8§F§E§2§A§lO§x§9§2§F§F§3§5§lM§8» §f";
    public static String Broadcast = "§8| §x§F§F§F§F§F§F§lB§x§F§F§D§F§D§F§lR§x§F§F§B§F§B§F§lO§x§F§F§9§F§9§F§lA§x§F§F§8§0§8§0§lD§x§F§F§6§0§6§0§lC§x§F§F§4§0§4§0§lA§x§F§F§2§0§2§0§lS§x§F§F§0§0§0§0§lT §8» §f";
    public static String Countdown = "§8| §x§D§D§0§0§F§F§lC§x§E§1§2§0§F§F§lO§x§E§6§4§0§F§F§lU§x§E§A§6§0§F§F§lN§x§E§E§8§0§F§F§lT§x§F§2§9§F§F§F§lD§x§F§7§B§F§F§F§lO§x§F§B§D§F§F§F§lW§x§F§F§F§F§F§F§lN §8» §f";
    public static String Teamchat = "§8| §x§F§F§0§0§0§0§lT§x§E§9§0§1§0§1§lE§x§D§3§0§3§0§3§lA§x§B§D§0§4§0§4§lM§x§A§8§0§5§0§5§lC§x§9§2§0§6§0§6§lH§x§7§C§0§8§0§8§lA§x§6§6§0§9§0§9§lT §8» §f";
    public static String Chatclear = "§8| §x§0§E§F§F§0§0§lC§x§2§C§F§F§2§0§lH§x§4§A§F§F§4§0§lA§x§6§8§F§F§6§0§lT§x§8§7§F§F§8§0§lC§x§A§5§F§F§9§F§lL§x§C§3§F§F§B§F§lE§x§E§1§F§F§D§F§lA§x§F§F§F§F§F§F§lR §8» §f";
    public static String PvP = "§8| §f§lPVP §8» §f";
    public static String Crates = "§8| §x§F§F§F§F§F§F§lC§x§F§0§C§C§F§F§lR§x§E§1§9§9§F§F§lA§x§D§1§6§6§F§F§lT§x§C§2§3§3§F§F§lE§x§B§3§0§0§F§F§lS §8» §f";
    public static String Supportchat = "§8| §x§8§A§F§F§0§0§lS§x§7§E§F§F§0§0§lU§x§7§1§F§F§0§0§lP§x§6§5§F§F§0§0§lP§x§5§8§F§F§0§0§lO§x§4§C§F§F§0§0§lR§x§4§0§F§F§0§0§lT§x§3§3§F§F§0§0§lC§x§2§7§F§F§0§0§lH§x§1§A§F§F§0§0§lA§x§0§E§F§F§0§0§lT §8» §f";
    public static String Daily = "§8| §x§F§F§0§0§C§4§lD§x§F§F§0§0§C§F§lA§x§F§F§0§0§D§9§lI§x§F§F§0§0§E§4§lL§x§F§F§0§0§E§E§lY §8» §f";

    public static String getHeader() {
        return "§x§5§c§5§c§5§c§m §x§6§7§6§7§6§7§m §x§7§1§7§1§7§1§m §x§7§c§7§c§7§c§m §x§8§7§8§7§8§7§m §x§9§2§9§2§9§2§m §x§9§c§9§c§9§c§m §x§a§7§a§7§a§7§m §x§b§2§b§2§b§2§m §x§b§d§b§d§b§d§m §x§c§7§c§7§c§7§m §x§d§2§d§2§d§2§m §x§d§d§d§d§d§d§m §x§e§8§e§8§e§8§m §x§f§2§f§2§f§2§m §x§f§d§f§d§f§d§m§f» §x§F§F§F§F§F§F§lH§x§F§F§C§C§C§C§lY§x§F§F§9§9§9§9§lB§x§F§F§6§6§6§6§lR§x§F§F§3§3§3§3§lI§x§F§F§0§0§0§0§lD§x§E§8§0§4§0§4§lC§x§D§0§0§7§0§7§lL§x§B§9§0§B§0§B§lA§x§A§1§0§E§0§E§lS§x§8§A§1§2§1§2§lH §f«§8§l§m§x§f§d§f§d§f§d§m §x§f§2§f§2§f§2§m §x§e§8§e§8§e§8§m §x§d§d§d§d§d§d§m §x§d§2§d§2§d§2§m §x§c§7§c§7§c§7§m §x§b§d§b§d§b§d§m §x§b§2§b§2§b§2§m §x§a§7§a§7§a§7§m §x§9§c§9§c§9§c§m §x§9§2§9§2§9§2§m §x§8§7§8§7§8§7§m §x§7§c§7§c§7§c§m §x§7§1§7§1§7§1§m §x§6§7§6§7§6§7§m §x§5§c§5§c§5§c§m";
    }
}
