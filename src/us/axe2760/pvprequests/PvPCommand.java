package us.axe2760.pvprequests;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PvPCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if (cmd.getName().equalsIgnoreCase("1v1")){
			
			
			if (sender instanceof Player){
				Player player = (Player)sender;
				
				if (Manager.isInBattle(player)){
					sender.sendMessage(ChatColor.RED + "Cannot perform this command while in battle!");
					return true;
				}
				
				if (args.length == 0){
					player.sendMessage(Messages.INFO_MESSAGE);
				}
				
				if (args.length >= 1){
					String playerName = args[0];
					
					if (args[0].equalsIgnoreCase("accept")){
						if (Manager.hasRequest(sender.getName())){
							Request request = Manager.getCurrentRequested(sender.getName());
							if (request == null) return true;
							if (!Manager.hasRequested(request.getRequester())) return false;

							Player p1 = Bukkit.getPlayer(request.getRequester());
							Player p2 = Bukkit.getPlayer(request.getRequestee());
							if (p1 == null || p2 == null) return false;
							
							Manager.startBattle(p1, p2);
						}
						else sender.sendMessage(ChatColor.RED + "You do not have a pending request!");
					}
					
					else if (args[0].equalsIgnoreCase("deny")){
						if (Manager.hasRequest(sender.getName())){
							Request request = Manager.getCurrentRequested(sender.getName());
							request.deny();
							Manager.requests.remove(request);
							sender.sendMessage(ChatColor.GOLD + "Denied request.");
						}
						else sender.sendMessage(ChatColor.RED + "You do not have a pending request!");
					}
					
					else if (args[0].equalsIgnoreCase("help")){
						return false;
					}
					else if (args[0].equalsIgnoreCase("admin")){
						if(!sender.isOp()){
							sender.sendMessage(ChatColor.RED + "You must have OP to perform this command!");
							return true;
						}
						
						if (args.length == 1){
							sender.sendMessage("/1v1 admin spawn1 - set spawn 1\n/1v1 admin spawn2 - set spawn 2");
						}
						else{
							if (args[1].equalsIgnoreCase("spawn1")){
								PvPRequests.spawn1 = player.getLocation();
								sender.sendMessage("Set spawn1");
							}
							else if (args[1].equalsIgnoreCase("spawn2")){
								PvPRequests.spawn2 = player.getLocation();
								sender.sendMessage("Set spawn2");
							}
						}
					}
					
					else if (args[0].equalsIgnoreCase(sender.getName())){
						sender.sendMessage("You can't 1v1 yourself bro!");
					}
					else{
						
						if (Bukkit.getPlayer(playerName) == null){
							
							sender.sendMessage(ChatColor.RED + "Said player is not online!");
							return true;
						}
						else{
							if (!Bukkit.getPlayer(playerName).isOnline()){
								sender.sendMessage(ChatColor.RED + "Said player is not online!");
								return true;
							}
						}
						
						//request
						Request request = new Request(sender.getName(), playerName);
						sender.sendMessage(ChatColor.GREEN + "Sent request!");

						if (Manager.hasRequest(playerName)){
							Manager.requests.remove(Manager.getCurrentRequested(playerName));
						}
						
						Manager.requests.add(request);
					}
				}
			}
			else{
				sender.sendMessage(ChatColor.RED + "You cannot fight someone from console! Nice try :P");
			}
		}
		return true;
	}
	
}
