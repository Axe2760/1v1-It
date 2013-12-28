package us.axe2760.pvprequests;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Request {

	public BukkitTask timer;
	public int time = 200;
	private String requester;
	private String requestee;
	private Request instance;
	
	public Request(String requester, String requestee){
		this.requester = requester;
		this.requestee = requestee;
		instance = this;
		this.startTimer();
	}
	
	public void startTimer(){
		Player sent = Bukkit.getPlayer(requestee);
		if (sent != null) sent.sendMessage(ChatColor.YELLOW + "You have received a 1v1 request from "+requester+"! Do "+ChatColor.RED+"/1v1 accept" + ChatColor.YELLOW + " to accept it, and "+ChatColor.RED + "/1v1 deny"+ ChatColor.YELLOW + " to deny it. Request will time out in 200 seconds.");
		
		timer = new BukkitRunnable(){
			public void run(){
				if (time <= 0){
					Player prequester = Bukkit.getPlayer(requester);
					if (prequester == null) return;
					
					prequester.sendMessage(ChatColor.RED + "Request timed out.");
					Manager.requests.remove(instance);
					timer.cancel();
				}
			}
		}.runTaskTimer(PvPRequests.getInstance(), 20L, 20L);
	}
	
	public String getRequester(){
		return requester;
	}
	
	public String getRequestee(){
		return requestee;
	}
	
	public void deny(){
		timer.cancel();
	}
	
	public String serialize(){
		return "Request: \nRequester: "+requester +"\nRequestee: "+requestee+"\nTime left: "+String.valueOf(time);
	}
}
