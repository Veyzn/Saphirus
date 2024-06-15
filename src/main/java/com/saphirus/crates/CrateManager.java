package com.saphirus.crates;

import com.saphirus.fastinv.FastInv;
import com.saphirus.fastinv.ItemBuilder;
import com.saphirus.main.Data;
import com.saphirus.main.Main;
import com.saphirus.utils.CenterText.TextCenter;
import com.saphirus.utils.LocationUtil;
import com.saphirus.utils.Maths;
import com.saphirus.utils.RandomCollection;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CrateManager {


    private File file;
    private File ordner = new File("plugins/Saphirus/Crates/");
    private String name;
    private YamlConfiguration cfg;
    public CrateManager(String crate) {
        name = crate;
        file = new File("plugins/Saphirus/Crates/" + name.toLowerCase() + ".yml");
        cfg = YamlConfiguration.loadConfiguration(file);
    }

    public boolean exists(){return file.exists();}
    public void create() {
        if(!ordner.exists()) {
            ordner.mkdir();
        }

        try {
            file.createNewFile();

            cfg.set("Name", name);
            cfg.set("ColoredName", "§a" + name);
            cfg.set("Items", new ArrayList<CrateItem>());
            cfg.set("Active", false);
            cfg.set("CrateItem", new ItemStack(Material.TRIPWIRE_HOOK));
            cfg.set("Opened", 0L);
            cfg.set("Location", "null");
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        file.delete();
    }

    public void setItem(ItemStack item) {
        cfg.set("CrateItem", item);
        save();
    }

    public Location getBlockLocation() {return LocationUtil.stringToLocation(cfg.getString("Location"));}

    public void setBlockLocation(Location loc) {
        cfg.set("Location", LocationUtil.locationToString(loc));
        save();
    }


    public ItemStack getItem() {return cfg.getItemStack("CrateItem");}

    public String getName() {return cfg.getString("Name");}

    public String getColoredname() {
        return cfg.getString("ColoredName").replaceAll("&", "§");
    }

    public void setColoredName(String ColoredName) {
            cfg.set("Coloredname", ColoredName);
            save();
    }

    public static String getNiceChance(CrateManager crate, double chance) {
        int totalchance = 0;
        for (final CrateItem ci : crate.getCrateItems()) {
            totalchance += ci.getChance();
        }
        return getRandomChance(chance, totalchance) + "";
    }

    public boolean getActiv() {

        return cfg.getBoolean("Aktiv");
    }


    public  void setActiv(boolean status) {

        try {
            cfg.set("Aktiv", status);
            cfg.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double getRandomChance(double chance, final int total) {
        return round(chance * 100.0 / total, 2);
    }

    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(value);
        bigDecimal = bigDecimal.setScale(places, RoundingMode.UP);
        return bigDecimal.doubleValue();
    }

    public List<CrateItem> getCrateItems() {
        return (List<CrateItem>) cfg.getList("Items");
    }


    public static ArrayList<CrateManager> getCrates() {

        ArrayList<CrateManager> ams = new ArrayList<>();
        for (File file : new File("plugins/Saphirus/Crates/").listFiles()) {
            YamlConfiguration ycfg = YamlConfiguration.loadConfiguration(file);

            CrateManager cm = new CrateManager(ycfg.getString("Name"));
            ams.add(cm);

        }

        return ams;

    }


    public void addItem(CrateItem crateItem) {

        List<CrateItem> ci = getCrateItems();

        ci.add(crateItem);
        cfg.set("Items", ci);

        save();
    }

    public void removeItem(CrateItem crateItem) {

        List<CrateItem> ci = getCrateItems();

        ci.remove(crateItem);
        cfg.set("Items", ci);

        save();
    }

    public CrateItem getRandomItem() {
        final RandomCollection<CrateItem> crateItems = new RandomCollection<CrateItem>();
        for (final CrateItem crateItem : getCrateItems()) {
            crateItems.add(crateItem.getChance(), crateItem);
        }
        final List<CrateItem> items = new ArrayList<CrateItem>();
        for (int i = 0; i < crateItems.map.size() * 180; ++i) {
            items.add(crateItems.next().entry);
        }
        Collections.shuffle(items);
        return items.get(new Random().nextInt(items.size()));
    }

    public static void openAllCrates(Player p) {
        FastInv inv = new FastInv(6*9, "§cCrate Inventory");

        for(int i : inv.getBorders()) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§1").build());
        }

        for(CrateManager cm : getCrates()) {
            inv.addItem(new ItemBuilder(cm.getItem()).name(cm.getColoredname()).build(), e-> {
                openCrateEdit(p,cm);
            });
        }

        inv.open(p);
    }


    public static void openCrateEdit(Player p,CrateManager cm) {
        FastInv inv = new FastInv(3*9, "§cCrate §8| " + cm.getColoredname());
        for(int i : inv.getBorders()) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§1").build());
        }

        inv.setItem(11, new ItemBuilder(Material.REDSTONE_TORCH).name("§cStatus §8(" + (cm.getActiv()? "§a§lACTIV":"§c§lDEACTIVATED") + "§8)").build(), e -> {
            cm.setActiv(!cm.getActiv());
            openCrateEdit(p,cm);
        });

        inv.setItem(15, new ItemBuilder(Material.CHEST).name("§aItems").lore("§fCurrently containing: §c" + cm.getCrateItems().size()+ " Items").build(), e -> {
                cm.openCrateItems(p);
        });


        inv.open(p);
    }


    public void openCrateItems(Player p) {
        FastInv inv = new FastInv(5*9, "§cItems §8| " + getColoredname());
        for(int i : inv.getBorders()) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§1").build());
        }

        for(CrateItem ci : getCrateItems()) {

            inv.addItem(new ItemBuilder(ci.getDisplayItem()).name(ci.getDisplayName()).lore("§cChance: §f" + ci.getChance(), "§cRarity: " + ci.getRarity().getColoredName()).build(), e -> {
                openItemEdit(p, ci);
            });
        }

        inv.open(p);
    }

    public void openItemEdit(Player p, CrateItem ci) {
        FastInv inv = new FastInv(3*9, "§cItems §8| " + getColoredname());
        for(int i : inv.getBorders()) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§1").build());
        }

        inv.setItem(10, new ItemBuilder(Material.REDSTONE_BLOCK).name("§c§lBROACAST §8(" + (ci.isBroadcast()? "§a§lTRUE":"§c§lFALSE") + "§8)").build(), e-> {
            ArrayList<CrateItem> items = (ArrayList<CrateItem>) getCrateItems();
            items.remove(ci);
            ci.setBroadcast(!ci.isBroadcast());
            items.add(ci);
            setItems(items);
            openItemEdit(p,ci);
        });

        inv.setItem(11, new ItemBuilder(Material.CHEST).name("§9§lREWARDS ITEMS").lore("§7If it's empty, it will be ignored").build(), e -> {
            FastInv inv2 = new FastInv(3*9, "§aAdd Items §8(§fClose to save§8)");

            inv2.addClickHandler(pl -> {
                pl.setCancelled(false);
            });

            for(ItemStack i : ci.getRewardItems()) {
                inv2.addItem(i);
            }

            inv2.addCloseHandler(e2 -> {
                ArrayList<CrateItem> crateitems = (ArrayList<CrateItem>) getCrateItems();
                ArrayList<ItemStack> items = new ArrayList<>();

                for(ItemStack each : inv2.getInventory().getContents()) {
                    if(each != null) {
                        if(each.getType() != Material.AIR) {
                            items.add(each);
                        }
                    }
                }

                crateitems.remove(ci);
                ci.setRewardItems(items);
                crateitems.add(ci);
                setItems(crateitems);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        openItemEdit(p,ci);
                    }
                }.runTaskLater(Main.instance,5);

            });

            inv2.open(p);
        });

        inv.setItem(12, new ItemBuilder(Material.EMERALD_BLOCK).name("§b§lRARITY §8(" + ci.getRarity().getColoredName() + "§8)").build(), e -> {
            FastInv inv2 = new FastInv(3*9, "§aChange Rarity from Item");
            for(int i : inv2.getBorders()) {
                inv2.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§1").build());
            }
            inv2.setItem(10, new ItemBuilder(Material.GRAY_DYE).name(Rarity.COMMON.getColoredName()).build(), ev -> {
                ArrayList<CrateItem> crateitems = (ArrayList<CrateItem>) getCrateItems();
                crateitems.remove(ci);
                ci.setRarity(Rarity.COMMON);
                crateitems.add(ci);
                setItems(crateitems);
                openItemEdit(p,ci);
            });

            inv2.setItem(11, new ItemBuilder(Material.LIME_DYE).name(Rarity.UNCOMMON.getColoredName()).build(), ev -> {
                ArrayList<CrateItem> crateitems = (ArrayList<CrateItem>) getCrateItems();
                crateitems.remove(ci);
                ci.setRarity(Rarity.UNCOMMON);
                crateitems.add(ci);
                setItems(crateitems);
                openItemEdit(p,ci);
            });

            inv2.setItem(12, new ItemBuilder(Material.BLUE_DYE).name(Rarity.RARE.getColoredName()).build(), ev -> {
                ArrayList<CrateItem> crateitems = (ArrayList<CrateItem>) getCrateItems();
                crateitems.remove(ci);
                ci.setRarity(Rarity.RARE);
                crateitems.add(ci);
                setItems(crateitems);
                openItemEdit(p,ci);
            });

            inv2.setItem(14, new ItemBuilder(Material.PURPLE_DYE).name(Rarity.EPIC.getColoredName()).build(), ev -> {
                ArrayList<CrateItem> crateitems = (ArrayList<CrateItem>) getCrateItems();
                crateitems.remove(ci);
                ci.setRarity(Rarity.EPIC);
                crateitems.add(ci);
                setItems(crateitems);
                openItemEdit(p,ci);
            });

            inv2.setItem(15, new ItemBuilder(Material.YELLOW_DYE).name(Rarity.LEGENDARY.getColoredName()).build(), ev -> {
                ArrayList<CrateItem> crateitems = (ArrayList<CrateItem>) getCrateItems();
                crateitems.remove(ci);
                ci.setRarity(Rarity.LEGENDARY);
                crateitems.add(ci);
                setItems(crateitems);
                openItemEdit(p,ci);
            });

            inv2.setItem(16, new ItemBuilder(Material.RED_DYE).name(Rarity.Mythical.getColoredName()).build(), ev -> {
                ArrayList<CrateItem> crateitems = (ArrayList<CrateItem>) getCrateItems();
                crateitems.remove(ci);
                ci.setRarity(Rarity.Mythical);
                crateitems.add(ci);
                setItems(crateitems);
                openItemEdit(p,ci);
            });

            inv2.open(p);

        } );

        inv.setItem(14, new ItemBuilder(Material.GOLD_INGOT).name("§e§lCHANCE §8(§f" + ci.getChance() + "§8)").build(), e -> {
            new AnvilGUI.Builder()
                    .onComplete((plr, text) -> {
                        // Called when the player completes the input in the AnvilGUI
                        if(Maths.isInt(text)) {
                            ArrayList<CrateItem> crateitems = (ArrayList<CrateItem>) getCrateItems();
                            crateitems.remove(ci);
                            ci.setChance(Integer.parseInt(text));
                            crateitems.add(ci);
                            setItems(crateitems);
                            openItemEdit(p, ci);
                        } else {
                            p.sendMessage(Data.invalidNumber);
                        }
                        return AnvilGUI.Response.close();
                    })
                    .text("Enter a chance") // Set the default text in the AnvilGUI
                    .title("§eEnter the Chance (Numbers only)") // Set the title of the AnvilGUI
                    .itemLeft(new ItemStack(Material.PAPER))
                    .plugin(Main.instance)
                    .open(p);
                });


        inv.setItem(15, new ItemBuilder(Material.COMMAND_BLOCK).name("§7§lCOMMANDS §8(§f" + (ci.getCommands() == null? "Empty":ci.getCommands()) + "§8)").build(), e -> {
            new AnvilGUI.Builder()
                    .onComplete((plr, text) -> {
                        // Called when the player completes the input in the AnvilGUI
                            ArrayList<CrateItem> crateitems = (ArrayList<CrateItem>) getCrateItems();
                            crateitems.remove(ci);
                            ci.setCommands(text);
                            crateitems.add(ci);
                            setItems(crateitems);
                            openItemEdit(p, ci);

                        return AnvilGUI.Response.close();
                    })
                    .text("Enter the command without '/'") // Set the default text in the AnvilGUI
                    .itemLeft(new ItemStack(Material.PAPER))
                    .title("Enter the command without '/'") // Set the title of the AnvilGUI
                    .plugin(Main.instance)
                    .open(p);
        });

        inv.setItem(16, new ItemBuilder(Material.BARRIER).name("§c§lREMOVE").build(), e -> {
            ArrayList<CrateItem> crateitems = (ArrayList<CrateItem>) getCrateItems();
            crateitems.remove(ci);
            setItems(crateitems);
            openCrateItems(p);
        });







        inv.open(p);

    }

    public void setItems(ArrayList<CrateItem> list) {
        cfg.set("Items",list);
        save();
    }

    public enum Rarity {
        COMMON("§7", "Common"), UNCOMMON("§a", "Uncommon"), RARE("§9", "Rare"), EPIC("§5",
                "Epic"), LEGENDARY("§e", "Legendary"),Mythical("§c", "Mythical");

        private String color;

        private String name;

        Rarity(String color, String name) {
            this.color = color;
            this.name = name;
        }

        public String getColor() {
            return this.color;
        }

        public String getName() {
            return this.name;
        }

        public String getColoredName() {
            return this.color + this.name;
        }
    }

    public ItemStack getKey() {
        ItemStack item = new ItemStack(getItem());
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(getColoredname());

        ArrayList<String> crate_lore = new ArrayList<>();
        crate_lore.add("");
        crate_lore.add("§fThis crate contains §c" + getCrateItems().size() + "x items §fin total");
        crate_lore.add("§fYou will receive §c1x random §fitem when opening it!");
        crate_lore.add("");
        crate_lore.add("§fGo to §c/crates §fto open it!");
        meta.setLore(crate_lore);
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey(Main.instance, "crate"), PersistentDataType.STRING, getName());

        item.setItemMeta(meta);
        return item;
    }


    public void win(Player p) {
        CrateItem ci = getRandomItem();

        if(!ci.getRewardItems().isEmpty()) {
            for(ItemStack each : ci.getRewardItems()) {
                p.getInventory().addItem(each);
            }
        }

        if(ci.getCommands() != null) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ci.getCommands());
        }


        if(ci.isBroadcast()) {
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(TextCenter.getCentredMessage("§x§F§F§E§3§0§0§lA§x§D§A§E§8§0§0§lM§x§B§5§E§C§0§0§lA§x§9§1§F§1§0§0§lZ§x§6§C§F§6§0§0§lI§x§4§7§F§A§0§0§lN§x§2§2§F§F§0§0§lG"));
            Bukkit.broadcastMessage(TextCenter.getCentredMessage("The player §c" + p.getName() + "§f just got §c" + ci.getDisplayName() + " §8(" + ci.getRarity().getColoredName() + "§8)"));
            Bukkit.broadcastMessage(TextCenter.getCentredMessage("§ffrom the §c" + getColoredname()));
            Bukkit.broadcastMessage("");
        } else {
            p.sendMessage(Data.Crates + "You got §c" + ci.getDisplayName() + "§f from the §c" + getColoredname());
        }

        List<CrateItem> items = getCrateItems();

        items.remove(ci);
        ci.setAmount(ci.getAmount()+1);
        items.add(ci);
        setItems((ArrayList<CrateItem>) items);



    }

    public int comparePreviewItems(CrateItem item1, CrateItem item2) {
        // First, compare by chance in descending order (higher chance comes first)
        int chanceComparison = Integer.compare(item2.getChance(), item1.getChance());

        // If chance is equal, compare by rarity
        if (chanceComparison == 0) {
            return item1.getRarity().compareTo(item2.getRarity());
        }

        return chanceComparison;
    }

    public void openPreview(Player p) {
        FastInv inv = new FastInv(4*9, "§fPreview §8| " + getColoredname());
        for(int i : inv.getBorders()) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).name("§1").build());
        }

        List<CrateItem> items = getCrateItems();
        Collections.sort(items, new PreviewComparator());

        for(CrateItem item : items) {
            inv.addItem(new ItemBuilder(item.getDisplayItem()).name(item.displayName.replaceAll("&","§"))
                    .lore("",
                            "§fRarity: §c" + item.getRarity().getColoredName(),
                            "§fChance: §c" + getNiceChance(this,item.getChance()) + "%",
                            "",
                            "§fThis item has been drawn §c" + item.getAmount() + "x"
                    ).build());
        }

        inv.open(p);
    }

    public static List<String> getCratenames() {
        List<String> names = new ArrayList<>();
        for(CrateManager cm : getCrates()) {
            names.add(cm.getName());
        }
        return names;
    }


}
