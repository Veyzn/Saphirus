package com.saphirus.commands;

import com.saphirus.main.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;



public class FixHandCMD implements CommandExecutor {
	 public static HashMap<Player, Long> spam = new HashMap<Player, Long>();
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		
		
		Player p = (Player) sender;
		
		
		
			if(args.length == 0) {
				if(p.getInventory().getItemInHand() != null) {
					 if(p.hasPermission("saphirus.fixhand")) {
						ItemStack item = new ItemStack(p.getItemInHand().getType());
						p.getInventory().getItemInHand().setDurability((short) (item.getDurability()));
						p.sendMessage(Data.Prefix + "You have fixed the item!");
					
					}
				} else p.sendMessage(Data.Prefix + "You have to hold an item in your hadn!");
			} else p.sendMessage(Data.Usage + "/fixhand" );
		
		
		
		
		return false;
	}

}
