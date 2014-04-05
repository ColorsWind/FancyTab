package de.codebucket.fancytab;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerListener implements Listener 
{
	FancyTab plugin;

	public ServerListener(FancyTab plugin) 
	{
		this.plugin = plugin;
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void playerJoin(PlayerJoinEvent e) 
	{
		Tablist.getInstance().setTablist(e.getPlayer());
		for (Player player : Bukkit.getOnlinePlayers())
		{
			Tablist.getInstance().refreshTablist(player);
		}
	}
}
