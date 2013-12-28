package us.axe2760.pvprequests;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public class PvPRequests extends JavaPlugin{

	private static PvPRequests instance; 
	private static boolean enabled = true;
	public static Location spawn1;
	public static Location spawn2;
	
	public void onEnable(){
		instance = this;
		
		this.getCommand("1v1").setExecutor(new PvPCommand());
		this.getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		this.saveDefaultConfig();
		this.saveConfig();
		
		spawn1=deserializeLocation(this.getConfig().getString("spawn1"));
		spawn2=deserializeLocation(this.getConfig().getString("spawn2"));
		
		
	}
	
	public void onDisable(){
		this.getConfig().set("spawn1", serializeLocation(spawn1));
		this.getConfig().set("spawn2", serializeLocation(spawn2));
		this.saveConfig();
	}
	
	public Location deserializeLocation(String in){
		String[] args = in.split(",");
		return new Location(Bukkit.getWorld(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]), Float.valueOf(args[4]), Float.valueOf(args[5]));
	}
	
	public String serializeLocation(Location in){
		return in.getWorld().getName() + "," + String.valueOf(in.getX()) + "," + String.valueOf(in.getY()) + "," + String.valueOf(in.getZ()) + "," + String.valueOf(in.getYaw()) + "," + String.valueOf(in.getPitch());
	}
	public static PvPRequests getInstance(){
		return instance;
	}
	
	public static boolean isRequestsEnabled(){
		return enabled;
	}
	

}
