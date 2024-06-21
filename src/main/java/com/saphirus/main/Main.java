package com.saphirus.main;

import com.ryan.RMain;
import com.saphirus.commands.*;
import com.saphirus.crates.CrateCMD;
import com.saphirus.crates.CrateEvent;
import com.saphirus.crates.CrateItem;
import com.saphirus.crates.CrateLocations;
import com.saphirus.daily.DailyCMD;
import com.saphirus.daily.DailyConfig;
import com.saphirus.fastinv.FastInvManager;
import com.saphirus.gang.GangCommand;
import com.saphirus.listener.ChatEvent;
import com.saphirus.listener.JoinEvent;
import com.saphirus.listener.TeleportEvent;
import com.saphirus.listener.UnknownCommand;
import com.saphirus.supportchat.SupportCMD;
import com.saphirus.utils.Board;
import com.saphirus.utils.TempPlayerCache;
import com.saphirus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public final class Main extends JavaPlugin {
    public static Plugin instance;
    public static Main main;
    public static MySQL sql = new MySQL();
    public static CommandMap commandMap;
    public RMain rmain;

    @Override
    public void onEnable() {
        main = this;
        instance = this;
        sql.connectToDatabase();
        rmain = new RMain( this ); // Please keep this immediately after the line that connects to the SQL database <3

        ConfigurationSerialization.registerClass(CrateItem.class);
        create();
        register();
        registerCommands();
        registerListener();
        StartTimers();
        Utils.createFolders();


        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable() {
        rmain.onDisable(); // Ryan's onDisable method
        TempPlayerCache.updateAllValuesInDatabase();
        sql.disconnectFromDatabase();
    }





    public void register() {
        FastInvManager.register(this);
    }

    public void create() {
        Config.createConfig();
        DailyConfig.createCFG();
        sql.createTablePlayerData();
        sql.createTableMuteTable();
        sql.createTableWarnTable();
        sql.createTableBanTable();
        CrateLocations cm = new CrateLocations();
        cm.createCFG();
    }

    public void StartTimers() {
        startConnectionCheckTask();
        for(Player all : Bukkit.getOnlinePlayers()) {
            TempPlayerCache tpc = new TempPlayerCache(all.getUniqueId().toString());
        }
        //LOAD DATA AND SCOREBOARD
        new BukkitRunnable() {
            @Override
            public void run() {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                            TempPlayerCache.updateAllValuesInDatabase();

                    }
                }.runTaskTimerAsynchronously(Main.instance,20*60*5, 20*60*5);


                for(Player all : Bukkit.getOnlinePlayers()) {
                    Board board = new Board();
                    board.set(all);
                }

                new BukkitRunnable() {
                    @Override
                    public void run() {

                        for(Player p : Bukkit.getOnlinePlayers()) {
                            Board board = new Board();
                            board.update(p);
                        }
                    }
                }.runTaskTimerAsynchronously(Main.instance, 120,120);
            }
        }.runTaskLater(instance, 20);


        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player all : Bukkit.getOnlinePlayers()) {
                    TempPlayerCache tpc = new TempPlayerCache(all.getUniqueId().toString());
                    tpc.setPlaytime(tpc.getPlaytime()+1);
                }
            }
        }.runTaskTimerAsynchronously(instance,20,20);




    }

    public void registerCommands() {
        getCommand("gamemode").setExecutor(new GamemodeCMD());
        getCommand("fly").setExecutor(new FlyCMD());
        getCommand("trash").setExecutor(new TrashCMD());
        getCommand("discord").setExecutor(new DiscordCMD());
        getCommand("feed").setExecutor(new FeedCMD());
        getCommand("globalmute").setExecutor(new GlobaldmuteCMD());
        getCommand("chatclear").setExecutor(new ChatclearCMD());
        getCommand("random").setExecutor(new RandomCMD());
        getCommand("broadcast").setExecutor(new BroadcastCMD());
        getCommand("countdown").setExecutor(new CountdownCMD());
        getCommand("teamchat").setExecutor(new StaffchatCMD());
        getCommand("workbench").setExecutor(new WorkbenchCMD());
        getCommand("bodysee").setExecutor(new BodyseeCMD());
        getCommand("invsee").setExecutor(new InveseeCMD());
        getCommand("enderchest").setExecutor(new EnderchestCMD());
        getCommand("maintenance").setExecutor(new MaintenanceCMD());
        getCommand("kickall").setExecutor(new KickAllCMD());
        getCommand("pvp").setExecutor(new PvPCMD());
        getCommand("spawn").setExecutor(new SpawnCMD());
        getCommand("setspawn").setExecutor(new SetSpawnCMD());

        getCommand("ban").setExecutor(new BanCMD());
        getCommand("ban").setTabCompleter(new BanCMD());

        getCommand("mute").setExecutor(new MuteCMD());
        getCommand("mute").setTabCompleter(new MuteCMD());

        getCommand("warn").setExecutor(new WarnCMD());
        getCommand("warn").setTabCompleter(new WarnCMD());

        getCommand("unban").setExecutor(new UnbanCMD());
        getCommand("unmute").setExecutor(new UnmuteCMD());

        getCommand("delwarn").setExecutor(new RemoveWarnCMD());
        getCommand("playerinfo").setExecutor(new PlayerInfoCMD());

        getCommand("hcrate").setExecutor(new CrateCMD());
        getCommand("hcrate").setTabCompleter(new CrateCMD());

        getCommand("support").setExecutor(new SupportCMD());
        getCommand("fixhand").setExecutor(new FixHandCMD());
        getCommand("fix").setExecutor(new FixCMD());
        getCommand("daily").setExecutor(new DailyCMD());

        getCommand("gang").setExecutor(new GangCommand());
        getCommand("gang").setTabCompleter(new GangCommand());
    }

    public void registerListener() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new TeleportEvent(), this);
        pm.registerEvents(new JoinEvent(), this);
        pm.registerEvents(new ChatEvent(), this);
        pm.registerEvents(new CrateEvent(), this);
        pm.registerEvents(new UnknownCommand(), this);
    }

    public void startConnectionCheckTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                sql.getColumn("fd2aa9dc-d499-431b-bfe7-db05e46e5070", "Name") ;
            }
        }.runTaskTimer(this, 200,200);
    }
}
