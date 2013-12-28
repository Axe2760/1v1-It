package us.axe2760.pvprequests;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerState {

	private String name;
	private ItemStack[] invData;
	private ItemStack[] armor;
	private Location position;
	private float xp = 0;
	
	public PlayerState(Player player){
		this.invData = player.getInventory().getContents();
		this.position = player.getLocation();
		this.xp = player.getExp();
		this.name = player.getName();
		this.armor = player.getInventory().getArmorContents();
	}
	
	public ItemStack[] getData(){
		return invData;
	}
	
	public Location getPosition(){
		return position;
	}
	
	public float getXp(){
		return xp;
	}
	
	public String getPlayerName(){
		return name;
	}
	public ItemStack[] getArmor(){
		return armor;
	}
	
}
