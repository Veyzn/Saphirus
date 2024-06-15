package com.saphirus.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Utils {


	public static void removeItemFromHand(Player p, int amount) {
		ItemStack itemInHand = p.getInventory().getItemInMainHand();
		if (itemInHand.getAmount() > 1) {
			itemInHand.setAmount(itemInHand.getAmount() - 1);
			p.getInventory().setItemInMainHand(itemInHand);
		} else p.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
	}


	public static String getZahl(int Zahl) {
		DecimalFormat df = new DecimalFormat();

		String zahl = df.format(Zahl);

		return zahl.replace(",", ".");
	}



	public static String getZahl(long Zahl) {
		DecimalFormat df = new DecimalFormat();

		String zahl = df.format(Zahl);

		return zahl.replace(",", ".");
	}

	public static void addItem(Player p, ItemStack item) {
		if (p.getInventory().firstEmpty() == -1) {
			p.getWorld().dropItemNaturally(p.getLocation(), item);
		} else {
			p.getInventory().addItem(new ItemStack[]{item});
		}
	}

	public static String getUserRank(String playerName) {
		// Get the User object for the player
		LuckPerms api = LuckPermsProvider.get();
		User user = api.getUserManager().getUser(playerName);

		// Check if the User object is not null
		if (user != null) {
			// Get the user's primary group
			String primaryGroup = user.getPrimaryGroup();

			// You can now use the 'primaryGroup' variable as the user's rank
			return primaryGroup;
		}

		// Return a default value or handle the case where the user doesn't exist
		return "default";
	}

	public static boolean inGroup(Player player, String group) {

		LuckPerms api = null;

		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
			api = provider.getProvider();

		}

		User u = api.getUserManager().getUser(player.getName());

		if (u.getPrimaryGroup().equalsIgnoreCase(group)) {
			return true;

		} else return false;

	}

	public static boolean inGroup(UUID username, String group) {

		LuckPerms api = null;

		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		if (provider != null) {
			api = provider.getProvider();

		}
		User u = api.getUserManager().getUser(username);
		if (u == null) {
			api.getUserManager().loadUser(username);
			u = api.getUserManager().getUser(username);
		}

		if (u.getPrimaryGroup().equalsIgnoreCase(group)) {
			return true;

		} else return false;

	}

	public static int getAvailableItems(Inventory inv, ItemStack item) {
		int counted = 0;
		ItemStack[] arrayOfItemStack;
		int j = (arrayOfItemStack = inv.getContents()).length;
		for (int i = 0; i < j; i++) {
			ItemStack content = arrayOfItemStack[i];
			if ((content != null) && (content.getType() != Material.AIR) && (content.getType() == item.getType())
					&& (content.getDurability() == item.getDurability())) {
				counted += content.getAmount();
			}
		}
		return counted;
	}


	public static boolean hasEnoughItems(Inventory inv, ItemStack item, int amount) {
		return getAvailableItems(inv, item) >= amount;
	}

	public static boolean removeItems(Inventory inv, ItemStack item, int amount) {
		if (!hasEnoughItems(inv, item, amount)) {
			return false;
		}
		int toRemove = amount;

		HashMap<Integer, ItemStack> slots = new HashMap<Integer, ItemStack>();
		ItemStack slotItem;
		for (int slot = 0; slot < inv.getSize(); slot++) {
			slotItem = inv.getItem(slot);
			if ((slotItem != null) && (slotItem.getType() != Material.AIR) && (slotItem.getType() == item.getType())
					&& (slotItem.getDurability() == item.getDurability())) {
				slots.put(Integer.valueOf(slot), slotItem);
			}
		}


		for (Map.Entry<Integer, ItemStack> entrySlots : slots.entrySet()) {
			if (((ItemStack) entrySlots.getValue()).getAmount() <= toRemove) {
				inv.setItem(((Integer) entrySlots.getKey()).intValue(), new ItemStack(Material.AIR));
				toRemove -= ((ItemStack) entrySlots.getValue()).getAmount();
			} else {
				ItemStack invItem = inv.getItem(((Integer) entrySlots.getKey()).intValue());
				invItem.setAmount(invItem.getAmount() - toRemove);
				break;
			}
		}
		return true;
	}

	public static String OnOffTest(String player) {

		Player p = Bukkit.getPlayer(player);

		if (p != null) {
			return "§a" + p.getName();
		} else return "§c" + player;


	}

	public static String OnOffTest(UUID uuid) {

		Player p = Bukkit.getPlayer(uuid);

		if (p != null) {
			return "§a" + p.getName();
		} else return "§c" + new PlayerCache(uuid.toString()).getName();


	}

	public static String getGroupPrefix(String groupName) {
		// Get the User object for the player

		LuckPerms api = LuckPermsProvider.get();

		// Check if the User object is not null
			// Get the primary group for the user
			Group group = api.getGroupManager().getGroup(groupName);

			// Check if the group exists
			if (group != null) {
				// Get the prefix for the group
				String prefix = group.getCachedData().getMetaData().getPrefix();

				// You can now use the 'prefix' variable as the group prefix
				return prefix.replaceAll("&","§");
			}


		// Return a default value or handle the case where the user or group doesn't exist
		return "DefaultPrefix";
	}



public static String isBigger(long bigger, long then) {
		if (bigger >= then) {
			return "§a§a✔";

		} else return "§c§l✖";
	}

	public static String isBigger(int bigger, int then) {
		if (bigger >= then) {
			return "§a§a✔";

		} else return "§c§l✖";
	}

	public static String getCheck(boolean status) {
		if (status) {
			return "§a§a✔";

		} else return "§c§l✖";
	}


	public static String fancy(String input) {
		char[] chars = input.toLowerCase().toCharArray();
		for (int i = 0; i < chars.length; i++) {
			switch (chars[i]) {
				case 'w' -> chars[i] = 'ᴡ';
				case 'e' -> chars[i] = 'ᴇ';
				case 'r' -> chars[i] = 'ʀ';
				case 't' -> chars[i] = 'ᴛ';
				case 'z' -> chars[i] = 'ᴢ';
				case 'u' -> chars[i] = 'ᴜ';
				case 'i' -> chars[i] = 'ɪ';
				case 'o' -> chars[i] = 'ᴏ';
				case 'p' -> chars[i] = 'ᴘ';
				case 'a' -> chars[i] = 'ᴀ';
				case 's' -> chars[i] = 'ꜱ';
				case 'd' -> chars[i] = 'ᴅ';
				case 'f' -> chars[i] = 'ꜰ';
				case 'g' -> chars[i] = 'ɢ';
				case 'h' -> chars[i] = 'ʜ';
				case 'j' -> chars[i] = 'ᴊ';
				case 'k' -> chars[i] = 'ᴋ';
				case 'l' -> chars[i] = 'ʟ';
				case 'y' -> chars[i] = 'ʏ';
				case 'x' -> chars[i] = 'x';
				case 'c' -> chars[i] = 'ᴄ';
				case 'v' -> chars[i] = 'ᴠ';
				case 'b' -> chars[i] = 'ʙ';
				case 'n' -> chars[i] = 'ɴ';
				case 'm' -> chars[i] = 'ᴍ';
			}
		}

		StringBuilder output = new StringBuilder();
		for (char c : chars)
			output.append(c);

		return output.toString();
	}



	public static String calculateRemainingTime(Long banend) {
		// SQL query to retrieve Ban or Mute End time for a specific UUID from the specified table
		if (banend > 0) {
			long currentTimeMillis = System.currentTimeMillis();
			long remainingTimeMillis = banend - currentTimeMillis;

			// Convert remaining time to days, hours, minutes, and seconds
			long days = remainingTimeMillis / (24 * 60 * 60 * 1000);
			long hours = (remainingTimeMillis % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
			long minutes = (remainingTimeMillis % (60 * 60 * 1000)) / (60 * 1000);
			long seconds = (remainingTimeMillis % (60 * 1000)) / 1000;

			StringBuilder formattedTime = new StringBuilder();

			if (days > 0) {
				formattedTime.append(days).append(" day").append(days > 1 ? "s" : "").append(" ");
			}
			if (hours > 0 || days > 0) {
				formattedTime.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(" ");
			}
			if (minutes > 0 || hours > 0 || days > 0) {
				formattedTime.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(" ");
			}
			formattedTime.append(seconds).append(" second").append(seconds > 1 ? "s" : "");

			return formattedTime.toString();
		}


        return "";
    }
	public static String convertSecondsToTime(long seconds) {
		if (seconds < 0) {
			throw new IllegalArgumentException("Time must be a non-negative value.");
		}

		if (seconds == 0) {
			return "0 seconds";
		}

		long minutes = seconds / 60;
		long hours = minutes / 60;
		long days = hours / 24;

		return String.format("%s%s%s%s",
				(days > 0) ? String.format("%02d day%s, ", days, (days > 1) ? "s" : "") : "",
				(hours % 24 > 0) ? String.format("%02d hour%s, ", hours % 24, (hours % 24 > 1) ? "s" : "") : "",
				(minutes % 60 > 0) ? String.format("%02d minute%s, ", minutes % 60, (minutes % 60 > 1) ? "s" : "") : "",
				(seconds % 60 > 0) ? String.format("%02d second%s", seconds % 60, (seconds % 60 > 1) ? "s" : "") : ""
		).trim();
	}

	public static String get(Long l) {
		String num = l.toString();
		return switch (num.charAt(num.length() - 1)) {
			case '1' -> l + "st";
			case '2' -> l + "nd";
			case '3' -> l + "rd";
			default -> l + "th";
		};

	}


	public static boolean hasFreeSpaceInInventory(Player player) {
		Inventory playerInventory = player.getInventory();
		int emptySlots = 0;
		PlayerInventory playerInv = player.getInventory();

		for (int slot = 0; slot < playerInventory.getSize(); slot++) {
			if (!isHandSlot(playerInv, slot) &&
					!isEquipmentSlot(playerInv, slot) &&
					!isItemStackNotEmpty(playerInventory.getItem(slot))) {
				emptySlots++;
			}
		}

		return emptySlots > 1;
	}

	// Helper method to check if a slot corresponds to the MainHand or OffHand
	private static boolean isHandSlot(PlayerInventory playerInventory, int slot) {
		return slot == playerInventory.getHeldItemSlot() || slot == playerInventory.getHeldItemSlot() + 45;
	}

	// Helper method to check if a slot corresponds to equipment items (helmet, leggings, etc.)
	private static boolean isEquipmentSlot(PlayerInventory playerInventory, int slot) {
		return slot >= 36 && slot <= 39; // Example range for equipment slots; adjust as needed.
	}

	// Helper method to check if an ItemStack is not null and not empty
	private static boolean isItemStackNotEmpty(ItemStack itemStack) {
		return itemStack != null && !itemStack.getType().isAir();
	}

	// Helper method to check if a slot corresponds to the off-hand (second-hand)
	private static boolean isOffHandSlot(PlayerInventory playerInventory, int slot) {
		return slot == 45;
	}


	public static void createFolders() {
		for(String folder: List.of("Crates", "Gangs")) {
			File ordner = new File("plugins/Saphirus/" + folder + "/");
			if(!ordner.exists()) {
				ordner.mkdir();
			}

		}
	}
}
