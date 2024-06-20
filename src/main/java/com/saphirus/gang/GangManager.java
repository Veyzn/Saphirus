package com.saphirus.gang;

import com.saphirus.fastinv.FastInv;
import com.saphirus.fastinv.ItemBuilder;
import com.saphirus.main.Data;
import com.saphirus.utils.TempPlayerCache;
import com.saphirus.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class GangManager {

    private String name;
    private File file;
    private YamlConfiguration cfg;
    private static final File GANGS_FOLDER = new File("plugins/Saphirus/Gangs");

    public GangManager(String names) {
        name = names;
        file = new File(GANGS_FOLDER, name.toLowerCase() + ".yml");
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public boolean exists(){return file.exists();}

    public void createGang(String uuid_owner) {
        try {
            file.createNewFile();

            cfg.set("Name", name);
            cfg.set("Owner", uuid_owner);
            cfg.set("Trophies", 0L);
            cfg.set("Members", new ArrayList<>());
            cfg.set("Bank.money", 0L);
            cfg.set("Bank.gems", 0L);
            cfg.set("Team-Level", 0);

            saveConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void disband() {
        if(!getEveryone().isEmpty()) {
            for(String uuid : getEveryone()) {
                TempPlayerCache tpc = new TempPlayerCache(uuid);
                tpc.setTeam("none");
            }
        }

        file.delete();
    }

    public String getName() {
        return cfg.getString("Name");
    }

    public void setName(String name) {
        cfg.set("Name", name);
        saveConfig();
    }

    public String getOwner() {
        return cfg.getString("Owner");
    }

    public void setOwner(String uuid_owner) {
        cfg.set("Owner", uuid_owner);
        saveConfig();
    }

    public long getTrophies() {
        return cfg.getLong("Trophies");
    }

    public void setTrophies(long trophies) {
        cfg.set("Trophies", trophies);
        saveConfig();
    }

    public List<String> getMembers() {
        return cfg.getStringList("Members");
    }

    public void addMember(String member) {
        List<String> members = getMembers();
        members.add(member);
        cfg.set("Members", members);
        saveConfig();
    }

    public void removeMember(String member) {
        List<String> members = getMembers();
        members.remove(member);
        cfg.set("Members", members);
        saveConfig();
    }

    public long getBankMoney() {
        return cfg.getLong("Bank.money");
    }

    public void setBankMoney(long money) {
        cfg.set("Bank.money", money);
        saveConfig();
    }

    public long getBankGems() {
        return cfg.getLong("Bank.gems");
    }

    public void setBankGems(long gems) {
        cfg.set("Bank.gems", gems);
        saveConfig();
    }

    public int getTeamLevel() {
        return cfg.getInt("Team-Level");
    }

    public void setTeamLevel(int level) {
        cfg.set("Team-Level", level);
        saveConfig();
    }

    public void saveConfig() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getEveryone() {
        List<String> everyone = getMembers();
        everyone.add(getOwner());
        return everyone;
    }

    public void sendPlayerGangMessage(String uuid, String message) {
        Player p = Bukkit.getPlayer(UUID.fromString(uuid));
        for(String everyone : getEveryone()) {
            Player every = Bukkit.getPlayer(UUID.fromString(everyone));
            if(every != null && p != null) {
                every.sendMessage(Data.Gang + "§c" + p.getName() + ": §a" + message.replace("#", ""));
            }
        }
    }

    public void sendGangMessage(String message) {
        for(String everyone : getEveryone()) {
            Player every = Bukkit.getPlayer(UUID.fromString(everyone));
            if(every != null) {
                every.sendMessage(Data.Gang + "§a" + message);
            }
        }
    }

    public static List<GangManager> getAllGangs() {
        List<GangManager> gangs = new ArrayList<>();
        File[] files = GANGS_FOLDER.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files != null) {
            for (File file : files) {
                String gangName = file.getName().replace(".yml", "");
                GangManager gm = new GangManager(gangName);
                if (gm.exists()) {
                    gangs.add(gm);
                }
            }
        }
        return gangs;
    }

    public static List<GangManager> getTop10Gangs() {
        return getAllGangs().stream()
                .sorted(Comparator.comparingLong(GangManager::getTrophies).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    public int getGangPlacement() {
        List<GangManager> sortedGangs = getAllGangs().stream()
                .sorted(Comparator.comparingLong(GangManager::getTrophies).reversed())
                .collect(Collectors.toList());

        for (int i = 0; i < sortedGangs.size(); i++) {
            if (sortedGangs.get(i).getName().equalsIgnoreCase(getName())) {
                return i + 1; // 1-based index
            }
        }
        return -1; // Gang not found
    }

    public void openTrophyInventory(Player p) {
        FastInv inv = new FastInv(6*9, "§eBuy trophies");

        for(int i : inv.getBorders()) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§1").build());
        }
        AtomicInteger amount = new AtomicInteger(1);
        long price_per_trophy = 5000000L;

        inv.setItem(13, new ItemBuilder(Material.TOTEM_OF_UNDYING)
                .name("§e§lTROPHIES")
                .lore("§7This is used to place in the " , "§eTrophy Leaderboard", "","§a$5.000.000 §7per §eTrophy")
                .build());

        inv.setItem(29, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .name("§a+1")
                .build(), e -> {
            amount.getAndAdd(1);
            updateTrophie(p, amount,inv,price_per_trophy);
        });

        inv.setItem(30, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .name("§a+10")
                .build(), e -> {
            amount.getAndAdd(10);
            updateTrophie(p, amount,inv,price_per_trophy);
        });

        inv.setItem(31, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .name("§a+50")
                .build(), e -> {
            amount.getAndAdd(50);
            updateTrophie(p, amount,inv,price_per_trophy);
        });

        inv.setItem(32, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .name("§a+100")
                .build(), e -> {
            amount.getAndAdd(100);
            updateTrophie(p, amount,inv,price_per_trophy);
        });

        inv.setItem(33, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE)
                .name("§a§lMAX §8(§e" + Utils.getZahl(getBankMoney()/price_per_trophy) + " Trophies§8)")
                .build(), e -> {
            amount.getAndSet((int) (getBankMoney()/price_per_trophy));
            updateTrophie(p, amount,inv,price_per_trophy);
        });

        inv.setItem(40, new ItemBuilder(getBankMoney() >= price_per_trophy*amount.get()  && amount.get() >0? Material.LIME_DYE:Material.RED_DYE)
                .name(getBankMoney() >= price_per_trophy*amount.get() ? "§aConfirm purchase":"§cNot enough money")
                .lore("§7Amount: §e" + amount.get() + " Trophies", "§7Cost: §a$" + Utils.getZahl(price_per_trophy * amount.get()))
                .build(), e -> {
                    if(getBankMoney() >= price_per_trophy * amount.get()) {
                        setTrophies(getTrophies() + amount.get());
                        setBankMoney(getBankMoney() - (price_per_trophy*amount.get()));
                        sendGangMessage("§c" + p.getName() + " §apurchased §e" + amount.get() + " Trophies §afor $" + Utils.getZahl(price_per_trophy*amount.get()));
                        p.closeInventory();
                    }

        });

        inv.open(p);


    }

    public void updateTrophie(Player p, AtomicInteger amount, FastInv inv,long price_per_trophy) {
        inv.setItem(13, new ItemBuilder(Material.TOTEM_OF_UNDYING)
                .name("§e§lTROPHIES")
                .amount(amount.get() == 0 ? 1:amount.get())
                .lore("§7This is used to place in the " , "§eTrophy Leaderboard", "","§a$5.000.000 §7per §eTrophy")
                .build());

        inv.setItem(40, new ItemBuilder(getBankMoney() >= price_per_trophy*amount.get() && amount.get() >0 ? Material.LIME_DYE:Material.RED_DYE)
                .name(getBankMoney() >= price_per_trophy*amount.get() ? "§aConfirm purchase":"§cNot enough money")
                .lore("§7Amount: §e" + amount.get() + " Trophies", "§7Cost: §a$" + Utils.getZahl(price_per_trophy * amount.get()))
                .build(), e -> {
            if(getBankMoney() >= price_per_trophy * amount.get()  && amount.get() >0) {
                setTrophies(getTrophies() + amount.get());
                setBankMoney(getBankMoney() - (price_per_trophy*amount.get()));
                sendGangMessage("§c" + p.getName() + " §apurchased §e" + amount.get() + " Trophies §afor $" + Utils.getZahl(price_per_trophy*amount.get()));
                p.closeInventory();
            }

        });

        p.updateInventory();
    }
}
