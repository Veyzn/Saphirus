package com.saphirus.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;


public class Board {



    public Board() {}

    public void set(Player p) {

        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("stats", "dummy");
        TempPlayerCache tpc = new TempPlayerCache(p.getUniqueId().toString());

//ɪ
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        obj.setDisplayName("§7» §b§lSAPHIRUS §8[§f§lɪ§8] §7«");

        //PLAYER
        obj.getScore("").setScore(12);
        obj.getScore(" §b§l" + Utils.fancy(p.getName())).setScore(11);

        obj.getScore("§2").setScore(10);
        Team money = board.registerNewTeam("money");
        money.setPrefix("  §f➥ §aᴍᴏɴᴇʏ: §a$");
        money.setSuffix("§a" + Maths.shortMoney(tpc.getMoney()));
        money.addEntry("§2");


        obj.getScore("§3").setScore(9);
        Team gems = board.registerNewTeam("gems");
        gems.setPrefix("  §f➥ §2ɢᴇᴍꜱ: §2");
        gems.setSuffix("§2" + Maths.shortMoney(tpc.getGems()));
        gems.addEntry("§3");


        obj.getScore("§4").setScore(8);
        Team stats = board.registerNewTeam("stats");
        stats.setPrefix("  §f➥ §cꜱᴛᴀᴛꜱ: §a");
        stats.setSuffix("§a" + Utils.getZahl(tpc.getKills()) + " §8| §c" + Utils.getZahl(tpc.getDeaths()));
        stats.addEntry("§4");

        obj.getScore("§5").setScore(7);
        Team clan = board.registerNewTeam("clan");
        clan.setPrefix("  §f➥ §6ᴛᴇᴀᴍ: §a");
        clan.setSuffix("§a" + tpc.getTeam());
        clan.addEntry("§5");

        obj.getScore("§1§2").setScore(6);
        obj.getScore(" §e§lꜱᴇʀᴠᴇʀ ꜱᴛᴀᴛꜱ").setScore(5);

        obj.getScore("§6").setScore(4);
        Team players = board.registerNewTeam("players");
        players.setPrefix("  §f➥ §aᴏɴʟɪɴᴇ: ");
        players.setSuffix("§8(§a" + Bukkit.getOnlinePlayers().size() + " §8| §c250§8)");
        players.addEntry("§6");

        obj.getScore("§7").setScore(3);
        Team linked = board.registerNewTeam("linked");
        linked.setPrefix("  §f➥ §bʟɪɴᴋᴇᴅ: §b");
        linked.setSuffix("§c " + Utils.getCheck(tpc.isLinked()));
        linked.addEntry("§7");

        obj.getScore("§7§2").setScore(2);
        obj.getScore("  §7» §f" + Utils.fancy("Saphirus.com") +  " §7«").setScore(1);

        //ɢᴇᴍꜱ
        //ᴇᴛʜᴇʀᴇᴜᴍ
        //ʙɪᴛᴄᴏɪɴ
        //ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘQʀꜱᴛᴜᴠᴡxʏᴢ
        //https://lingojam.com/FancyTextGenerator

        //➥

        p.setScoreboard(board);



    }

    public void update(Player p) {

        Scoreboard board = p.getScoreboard();
        TempPlayerCache tpc = new TempPlayerCache(p.getUniqueId().toString());

        p.getScoreboard().getTeam("money").setSuffix("§a" + Maths.shortMoney(tpc.getMoney()));
        p.getScoreboard().getTeam("gems").setSuffix("§2" + Maths.shortMoney(tpc.getGems()));
        p.getScoreboard().getTeam("stats").setSuffix("§a" + Utils.getZahl(tpc.getKills()) + " §8| §c" + Utils.getZahl(tpc.getDeaths()));
        p.getScoreboard().getTeam("clan").setSuffix("§a" + tpc.getTeam());
        p.getScoreboard().getTeam("players").setSuffix("§8(§a" + Bukkit.getOnlinePlayers().size() + " §8| §c250§8)");
        p.getScoreboard().getTeam("linked").setSuffix("§c " + tpc.isLinked());



    }


    public static void updateTab(Player p) {
	/*	Scoreboard board = p.getScoreboard();
	Team Owner = board.getTeam("01Owner");
	Team Manager  = board.getTeam("02Manager");
	 Team Admin  = board.getTeam("03Admin");
	 Team Developer = board.getTeam("04Developer");
	 Team SrModerator = board.getTeam("05SrMod");
	 Team Moderator = board.getTeam("06Mod");
	 Team Builder = board.getTeam("07Builder");
	 Team Supporter = board.getTeam("08Supporter");
	 Team Guard = board.getTeam("09Guard");
	 Team Youtuber = board.getTeam("10Youtuber");
	 Team Predator = board.getTeam("11Predator");
    Team Legend = board.getTeam("12Legend");
	Team Ultimate = board.getTeam("13Ultimate");
	Team Hustler = board.getTeam("14Hustler");
	Team Cosmos = board.getTeam("15Cosmos");
	Team Viper = board.getTeam("16Viper");
	Team Spieler = board.getTeam("17Spieler");







	for(Player all : Bukkit.getOnlinePlayers()) {


		if(Utils.inGroup(all,"Owner")) {
			Owner.addPlayer(all);

		} else if(Utils.inGroup(all,"Manager")) {
			Manager.addPlayer(all);

		}else if(Utils.inGroup(all,"Admin")) {
			Admin.addPlayer(all);

		} else if(Utils.inGroup(all,"Developer")) {
			Developer.addPlayer(all);

		} else if(Utils.inGroup(all,"SrModerator")) {
			SrModerator.addPlayer(all);

		} else if(Utils.inGroup(all,"Moderator")) {
			Moderator.addPlayer(all);

		} else if(Utils.inGroup(all,"Builder")) {
			Builder.addPlayer(all);

		} else if(Utils.inGroup(all,"Supporter")) {
			Supporter.addPlayer(all);

		} else if(Utils.inGroup(all,"Guard")) {
			Guard.addPlayer(all);

		} else if(Utils.inGroup(all,"Youtuber")) {
			Youtuber.addPlayer(all);

		} else if(Utils.inGroup(all,"Predator")) {
			Predator.addPlayer(all);

		} else if(Utils.inGroup(all,"Legend")) {
			Legend.addPlayer(all);

		} else if(Utils.inGroup(all,"Ultimate")) {
			Ultimate.addPlayer(all);

		} else if(Utils.inGroup(all,"Hustler")) {
			Hustler.addPlayer(all);

		} else if(Utils.inGroup(all,"Cosmos")) {
			Cosmos.addPlayer(all);

		} else if(Utils.inGroup(all,"Viper")) {
			Viper.addPlayer(all);

		} else {
			Spieler.addPlayer(all);

		}*/

    }












}
