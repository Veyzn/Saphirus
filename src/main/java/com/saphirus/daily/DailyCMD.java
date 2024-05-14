package com.saphirus.daily;

import com.saphirus.main.Data;
import com.saphirus.utils.TempPlayerCache;
import com.saphirus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DailyCMD implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        Player p = (Player) commandSender;

        if(DailyConfig.canClaim(p.getUniqueId().toString())) {
            Bukkit.broadcastMessage(Data.Daily + "The player §c" + p.getName() + "§f has claimed their §c/daily");
            TempPlayerCache tpc = TempPlayerCache.data.get(p.getUniqueId().toString());
            DailyConfig.addPlayer(p.getUniqueId().toString(), System.currentTimeMillis() + (1000*60*60*24));
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5.0F, 5.0F);
            switch (Utils.getUserRank(p.getName()).toLowerCase()) {

                case "default" -> {
                    p.sendMessage(Data.Daily + "You received");
                    p.sendMessage(Data.Daily + " §c- §a$50.000");
                    p.sendMessage(Data.Daily + " §c- §2500 Gems");
                    tpc.addGems(500L);
                    tpc.addMoney(50000L);
                }

                case "grinder" -> {
                    p.sendMessage(Data.Daily + "You received");
                    p.sendMessage(Data.Daily + " §c- §a$100.000");
                    p.sendMessage(Data.Daily + " §c- §21.000 Gems");
                    tpc.addGems(1000L);
                    tpc.addMoney(100000L);
                }

                case "hunter", "media" -> {
                    p.sendMessage(Data.Daily + "You received");
                    p.sendMessage(Data.Daily + " §c- §a$200.000");
                    p.sendMessage(Data.Daily + " §c- §22.000 Gems");
                    tpc.addGems(2000L);
                    tpc.addMoney(200000L);
                }

                case "warlord", "trial", "supporter", "mod", "srmod", "builder", "developer", "admin", "manager" -> {
                    p.sendMessage(Data.Daily + "You received");
                    p.sendMessage(Data.Daily + " §c- §a$300.000");
                    p.sendMessage(Data.Daily + " §c- §24.000 Gems");
                    tpc.addGems(4000L);
                    tpc.addMoney(300000L);
                }

                case "legend", "creator" -> {
                    p.sendMessage(Data.Daily + "You received");
                    p.sendMessage(Data.Daily + " §c- §a$500.000");
                    p.sendMessage(Data.Daily + " §c- §25.000 Gems");
                    tpc.addGems(5000L);
                    tpc.addMoney(500000L);
                }

                case "ascended" -> {
                    p.sendMessage(Data.Daily + "You received");
                    p.sendMessage(Data.Daily + " §c- §a$750.000");
                    p.sendMessage(Data.Daily + " §c- §28.000 Gems");
                    tpc.addGems(8000L);
                    tpc.addMoney(750000L);
                }

                case "god","director" -> {
                    p.sendMessage(Data.Daily + "You received");
                    p.sendMessage(Data.Daily + " §c- §a$1.000.000");
                    p.sendMessage(Data.Daily + " §c- §210.000 Gems");
                    tpc.addGems(10000L);
                    tpc.addMoney(1000000L);
                }


            }

        } else p.sendMessage(Data.Daily + "You can claim your §c/daily §fin §c" + Utils.calculateRemainingTime(DailyConfig.cfg.getLong(p.getUniqueId().toString())));

        return false;
    }
}
