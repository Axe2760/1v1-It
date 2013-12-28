package us.axe2760.pvprequests;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerListener implements Listener{
	
	private int[] invIds = new int[]{23, 54, 58, 61, 62, 130, 145, 146, 158, 342, 343};
	
	//so players can't break blocks
	@EventHandler
	public void onBreak(BlockBreakEvent event){
		if (Manager.isInBattle(event.getPlayer())){
			event.setCancelled(true);
		}
	}
	
	//detect death
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onDeath(PlayerDeathEvent event){

		
	
		if (Manager.isInBattle(event.getEntity())){
			event.getEntity().sendMessage(ChatColor.GREEN + "You have lost the battle... :(");
			Battle b = Manager.getBattle(event.getEntity());
			
			Manager.endBattle(b, Manager.getPlayerNumber(b, event.getEntity().getName()));
			event.getDrops().clear();
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event){

		if (Manager.restores.containsKey(event.getPlayer().getName())){

			final PlayerState state = Manager.restores.get(event.getPlayer().getName());
			
			final Player player = event.getPlayer();

			player.getInventory().setContents(state.getData());
			player.getInventory().setArmorContents(state.getArmor());
			player.updateInventory();
			
			player.setExp(state.getXp());
	

			player.sendMessage(ChatColor.GREEN + "Restored your state.");
			
			Manager.restores.remove(event.getPlayer().getName());
			new BukkitRunnable(){
				public void run(){
					player.teleport(state.getPosition());
				}
			}.runTaskLater(PvPRequests.getInstance(), 1L);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onDamage(EntityDamageByEntityEvent event){
		if (event.getDamager().getType() != EntityType.PLAYER) return;
		if (event.getEntity().getType() != EntityType.PLAYER)return;
		//both players
		if (Manager.isInBattle((Player)event.getDamager()) || Manager.isInBattle((Player)event.getEntity())){
			if (Manager.isInBattle((Player)event.getDamager()) && Manager.isInBattle((Player)event.getEntity())){
				if (!Manager.getBattle((Player)event.getDamager()).equals(Manager.getBattle((Player)event.getEntity()))){
					event.setCancelled(true);
					((Player)event.getDamager()).sendMessage("You cannot damage someone else while you are in battle!");
				}
				else{
					event.setCancelled(false);
				}
			}
		}
	}
	
	@EventHandler
	public void interact(PlayerInteractEvent e){
		if (e.getClickedBlock() != null){
			if (Manager.isInBattle(e.getPlayer())){
				for (int id : invIds){
					if (e.getClickedBlock().getTypeId() == id){
						e.setCancelled(true);
						return;
					}
				}
				//else, continue
			}
		}
	}
	
	
	
	
	
	
}
