package us.axe2760.pvprequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Manager {

	private static List<Battle> battles = new ArrayList<>();
	private static List<String> queue = new ArrayList<>();
	public static List<Request> requests = new ArrayList<>();
	public static HashMap<String, PlayerState> restores = new HashMap<>();
	public static List<String> battling = new ArrayList<>();
	
	
	public static boolean isInBattle(Player who){
		return battling.contains(who.getName());
	}
	
	public static List<Battle> getBattles(){
		return battles;
	}
	
	public static short getPlayerNumber(Battle battle, String who){
		if (battle.getPlayer1().equals(who)) return 1;
		else if (battle.getPlayer2().equals(who)) return 2;
		return 0;
	}
	public static Battle getBattle(Player who){
		for (Battle b : battles){
			if(b.getPlayer1().equalsIgnoreCase(who.getName()) || b.getPlayer2().equalsIgnoreCase(who.getName())){
				return b;
			}
		}
		return null;
	}
	
	public static void startBattle(Player p1, Player p2){
		final Battle b = new Battle(p1, p2);
		
		b.setP1State(new PlayerState(p1));
		b.setP2State(new PlayerState(p2));

		restores.put(p1.getName(), new PlayerState(p1));
		restores.put(p2.getName(), new PlayerState(p2));

		p1.sendMessage(Messages.MATCH_MESSAGE.replaceAll("%name%", p2.getName()));
		p2.sendMessage(Messages.MATCH_MESSAGE.replaceAll("%name%", p1.getName()));
		
		p1.teleport(PvPRequests.spawn1);
		p2.teleport(PvPRequests.spawn2);
		
		b.timer = new BukkitRunnable(){
			public void run(){
				b.tick();
				
				if (b.getTimeLeft() <= 0){
					//end the game, cancel runnable
					
					Player p1 = Bukkit.getPlayer(b.getPlayer1());
					Player p2 = Bukkit.getPlayer(b.getPlayer2());
					
					if (p1 != null && p2 != null){
						endBattle(b, (short)0);
					}
					
					this.cancel();
				}
			}
		}.runTaskTimer(PvPRequests.getInstance(),20L, 20L);
		
		
		battles.add(b);
		battling.add(p1.getName());
		battling.add(p2.getName());
	}
	
	/*public static void endBattleFromTime(Battle b){
		b.timer.cancel();
		Player p1 = Bukkit.getPlayer(b.getPlayer1());
		Player p2 = Bukkit.getPlayer(b.getPlayer2());
		
		if (p1 != null && p2 != null){
			p1.sendMessage(Messages.TIME_UP);
			p2.sendMessage(Messages.TIME_UP);

			restorePlayer(b.getP1State());
			restorePlayer(b.getP2State());
		}
		
		battles.remove(b);
		battling.remove(p1.getName());
		battling.remove(p2.getName());
	}*/
	public static void endBattle(Battle b, short winner){
		b.timer.cancel();
		Player p1 = Bukkit.getPlayer(b.getPlayer1());
		Player p2 = Bukkit.getPlayer(b.getPlayer2());
		b.setWinner(winner);
		if (p1 != null && p2 != null){
			p1.sendMessage("The battle is over.");
			p2.sendMessage("The battle is over.");
			if (winner==0){
				p1.sendMessage("Time is up! It's a draw!");
				p2.sendMessage("Time is up! It's a draw!");
				restorePlayerInstant(b.getP1State());
				restorePlayerInstant(b.getP2State());
			}
			if (winner==2){
				restorePlayer(b.getP2State());
				restorePlayerInstant(b.getP1State());
				p1.sendMessage("You have won the battle!");
				p2.sendMessage(p1.getName() + " has won the battle!");
			}
			if (winner==1) {
				restorePlayer(b.getP1State());
				restorePlayerInstant(b.getP2State());
				p2.sendMessage("You have won the battle!");
				p1.sendMessage(p2.getName() + " has won the battle!");
			}
			
		}
		
		battles.remove(b);
		battling.remove(p1.getName());
		battling.remove(p2.getName());
	}
	public static void addToQueue(Player who){
		queue.add(who.getName());
	}
	
	public static void matchFromQueue(){
		if (queue.size() >= 2){
			Player p1 = Bukkit.getPlayer(queue.get(0));
			Player p2 = Bukkit.getPlayer(queue.get(1));
			
			if (p1 != null && p2 != null){
				//messages
				p1.sendMessage(Messages.MATCH_MESSAGE);
				p2.sendMessage(Messages.MATCH_MESSAGE);
				
				//teleport
				//TODO: run start game
				
				
				//make battle
				startBattle(p1, p2);
			}
		}
	}
	

	
	public static void restorePlayer(PlayerState state){
			restores.put(state.getPlayerName(), state);
	}
	
	public static void restorePlayerInstant(PlayerState state){
		
		Player player = Bukkit.getPlayer(state.getPlayerName());
		if (player == null) return;
		if (!player.isDead()){
			player.getInventory().setContents(state.getData());
			player.getInventory().setArmorContents(state.getArmor());
			
			player.setExp(state.getXp());
	
			player.teleport(state.getPosition());
			
			if(restores.containsKey(state.getPlayerName())){
				restores.remove(state.getPlayerName());
			}
		}
	}
	public static boolean hasRequest(String who){
		for (Request request : requests){
			if (request.getRequestee().equalsIgnoreCase(who)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean hasRequested(String who){
		for (Request request : requests){
			if (request.getRequester().equalsIgnoreCase(who)) return true;
		}
		return false;
	}
	
	public static Request getCurrentRequested(String player){
		for (Request request : requests){
			if (request.getRequestee().equalsIgnoreCase(player)) return request;
		}
		return null;
	}
	
	
}
