package us.axe2760.pvprequests;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Battle {

	private short winner = 0;
	private String player1;
	private String player2;
	private int time_left = 300;
	public BukkitTask timer;
	
	private PlayerState p1State;
	private PlayerState p2State;
	
	public Battle(Player p1, Player p2){
		this.player1 = p1.getName();
		this.player2 = p2.getName();
	}
	
	public String getPlayer1(){
		return player1;
	}
	
	public String getPlayer2(){
		return player2;
	}
	
	public void setPlayer1(String player){
		this.player1 = player;
	}

	public void setPlayer2(String player){
		this.player2 = player;
	}
	
	public void tick(){
		time_left--;
	}
	
	public int getTimeLeft(){
		return time_left;
	}
	
	public void setTimeLeft(int time){
		time_left = time;
	}

	public PlayerState getP2State() {
		return p2State;
	}

	public void setP2State(PlayerState p2State) {
		this.p2State = p2State;
	}

	public PlayerState getP1State() {
		return p1State;
	}

	public void setP1State(PlayerState p1State) {
		this.p1State = p1State;
	}
	
	public void setWinner(Short player){
		winner = player;
	}
	
	public short getWinner(){
		return winner;
	}
}
