package me.ivan7994.moneyperevents.listeners;

import java.text.DecimalFormat;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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
	
	
	@EventHandler
	public void BreakBlock(BlockBreakEvent event) {
		Player plr = event.getPlayer();
			if(plr.hasPermission(pluginL.getConfig().getString("perm_money_by_blocks"))) {		
				EconomyResponse r = Main.getEconomy().depositPlayer(plr, RandomDouble("min_pay_block", "max_pay_block"));
				DecimalFormat df = new DecimalFormat("#.00");
				String playerMoney = df.format(Main.getEconomy().getBalance(plr));
				if(r.transactionSuccess()) {
					ActionBar("§a" + pluginL.getConfig().getString("actual_money_message") + playerMoney, plr);
				}
				else {
					plr.sendMessage("§4" + "Ocurrio un error en el plugin de economia, avisale al administrador");
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
				if(plr.hasPermission(pluginL.getConfig().getString("perm_money_by_mobs"))) {
					EconomyResponse r = Main.getEconomy().depositPlayer(plr, RandomDouble("min_pay_mob", "max_pay_mob"));
					DecimalFormat df = new DecimalFormat("#.00");
					String playerMoney = df.format(Main.getEconomy().getBalance(plr));
		        	if(r.transactionSuccess()) {
		        		ActionBar("§a" + pluginL.getConfig().getString("actual_money_message") + playerMoney, plr);
		        	}
		        	else {
		        		plr.sendMessage("§4" + "Ocurrio un error en el plugin de economia, avisale al administrador");
		        	}
				}
			}
		}
	}
	
	@EventHandler
	public void Pescando(PlayerFishEvent event) {
		 if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
			 	Player plr = event.getPlayer();
			 	if(plr.hasPermission(pluginL.getConfig().getString("perm_money_by_fishing"))) {	
			 		EconomyResponse r = Main.getEconomy().depositPlayer(plr, RandomDouble("min_pay_fishing", "max_pay_fishing"));
			 		DecimalFormat df = new DecimalFormat("#.00");
			 		String playerMoney = df.format(Main.getEconomy().getBalance(plr));
			 		if(r.transactionSuccess()) {
			 			ActionBar("§a" + pluginL.getConfig().getString("actual_money_message") + playerMoney, plr);
			 		}
			 		else {
			 			plr.sendMessage("§4" + "Ocurrio un error en el plugin de economia, avisale al administrador");
			 		}
			 	}
		 }
		 else {
			 //do nothing
		 }
		
	}
	
	public void ActionBar(String message, Player p) {
		p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}
	
	public double RandomDouble(String min, String max) {
	    double minim = pluginL.getConfig().getDouble(min);
	    double maxim = pluginL.getConfig().getDouble(max);
	    double generatedDouble = minim + new Random().nextDouble() * (maxim- minim);    
	    return generatedDouble;	     
	}
	
    
    
}
