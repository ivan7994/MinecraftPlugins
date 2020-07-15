package me.ivan7994.moneyperevents.listeners;

import java.text.DecimalFormat;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerFishEvent;

import me.ivan7994.moneyperevents.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.EconomyResponse;

public class EventListeners implements Listener{
	

	private static Main pluginL;
	
	public EventListeners(Main plugin) {
		this.pluginL = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
		
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void BreakBlock(BlockBreakEvent event) {
		Player plr = event.getPlayer();
		EconomyResponse r = null;
			if(!event.isCancelled() && plr.hasPermission(getCfgText("perm_money_by_blocks"))) {	
				
				//Rewards by ores
				switch(event.getBlock().getBlockData().getMaterial()) {
					case COAL_ORE: r = Main.getEconomy().depositPlayer(plr, RandomDouble("min_pay_coal_ore", "max_pay_coal_ore"));
						break;
					case IRON_ORE: r = Main.getEconomy().depositPlayer(plr, RandomDouble("min_pay_iron_ore", "max_pay_iron_ore"));
						break;
					case GOLD_ORE: r = Main.getEconomy().depositPlayer(plr, RandomDouble("min_pay_gold_ore", "max_pay_gold_ore"));
						break;
					case DIAMOND_ORE: r = Main.getEconomy().depositPlayer(plr, RandomDouble("min_pay_diamond_ore", "max_pay_diamond_ore"));
						break;
					case ANCIENT_DEBRIS: r = Main.getEconomy().depositPlayer(plr, RandomDouble("min_pay_ancient_debris", "max_pay_ancient_debris"));
						break;
					case EMERALD_ORE: r = Main.getEconomy().depositPlayer(plr, RandomDouble("min_pay_emerald_ore", "max_pay_emerald_ore"));
						break;
					default: r = Main.getEconomy().depositPlayer(plr, RandomDouble("min_pay_block", "max_pay_block"));
						break;		
				}
				
				//Format
				DecimalFormat df = new DecimalFormat("#.00");
				String playerMoney = df.format(Main.getEconomy().getBalance(plr));
				//Success Add money or fail
				if(r.transactionSuccess()) {
					ActionBar("§a" + getCfgText("actual_money_message") + playerMoney, plr);
				}
				else {
					plr.sendMessage("§4" + getCfgText("error_text"));
				}
		}
	}
	
	@EventHandler
	public void KillEntity(EntityDeathEvent event) {
		
		Entity e = event.getEntity();	
		if(e.getLastDamageCause() instanceof EntityDamageByEntityEvent)
		{
			EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) e.getLastDamageCause();
				
			if(nEvent.getDamager() instanceof Player)
			{
				Player plr = (Player)nEvent.getDamager();
				if(plr.hasPermission(getCfgText("perm_money_by_mobs"))) {
					EconomyResponse r = Main.getEconomy().depositPlayer(plr, RandomDouble("min_pay_mob", "max_pay_mob"));
					DecimalFormat df = new DecimalFormat("#.00");
					String playerMoney = df.format(Main.getEconomy().getBalance(plr));
		        	if(r.transactionSuccess()) {
		        		ActionBar("§a" + getCfgText("actual_money_message") + playerMoney, plr);
		        	}
		        	else {
		        		plr.sendMessage("§4" + getCfgText("error_text"));
		        	}
				}
			}
		}
	}
	
	@EventHandler
	public void Pescando(PlayerFishEvent event) {
		 if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
			 	Player plr = event.getPlayer();
			 	if(plr.hasPermission(getCfgText("perm_money_by_fishing"))) {	
			 		EconomyResponse r = Main.getEconomy().depositPlayer(plr, RandomDouble("min_pay_fishing", "max_pay_fishing"));
			 		DecimalFormat df = new DecimalFormat("#.00");
			 		String playerMoney = df.format(Main.getEconomy().getBalance(plr));
			 		if(r.transactionSuccess()) {
			 			ActionBar("§a" + getCfgText("actual_money_message") + playerMoney, plr);
			 		}
			 		else {
			 			plr.sendMessage("§4" + getCfgText("error_text"));
			 		}
			 	}
		 }
		 else {
			 //do nothing
		 }
		
	}
	
	//Utils
	
	public void ActionBar(String message, Player p) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}
	
	public double RandomDouble(String min, String max) {
	    double minim = pluginL.getConfig().getDouble(min);
	    double maxim = pluginL.getConfig().getDouble(max);
	    double generatedDouble = minim + new Random().nextDouble() * (maxim- minim);    
	    return generatedDouble;	     
	}
	
    public String getCfgText(String texto) {
    	return pluginL.getConfig().getString(texto);
    }
    
}
