package com.saphirus.commands;

import com.saphirus.main.Data;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class FixCMD implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	  {
	    if ((sender instanceof Player))
	    {
	      Player p = (Player)sender;
	      if (p.hasPermission("saphirus.fix"))
	      {
	        int repair = 0;
	        ItemStack[] arrayOfItemStack;
	        int j = (arrayOfItemStack = p.getInventory().getContents()).length;
	        for (int i = 0; i < j; i++)
	        {
	          ItemStack contents = arrayOfItemStack[i];
	          if ((contents != null) && 
	            (contents.getType() != Material.AIR) && 
	            (contents.getType().getMaxDurability() != 0) && 
	            (contents.getDurability() != 0))
	          {
	            contents.setDurability((short)0);
	            repair++;
	          }
	        }
	        j = (arrayOfItemStack = p.getInventory().getArmorContents()).length;
	        for (int i = 0; i < j; i++)
	        {
	          ItemStack armorContents = arrayOfItemStack[i];
	          if ((armorContents != null) && 
	            (armorContents.getType() != Material.AIR) && 
	            (armorContents.getType().getMaxDurability() != 0) && 
	            (armorContents.getDurability() != 0))
	          {
	            armorContents.setDurability((short)0);
	            repair++;
	          }
	        }
	        if (repair == 0)
	        	
	        
	        
	        	
	        {
	          p.sendMessage(Data.Prefix + "No items have been fixed!");
	        }
	        else
	        {
	          p.sendMessage(Data.Prefix + "§c" + repair + "x Items §fhave been fixed.");
	          
	        }
	      }
	      else
	      {
	        p.sendMessage(Data.noPerms);
	      }
	    }
	    return false;
	  }

}
