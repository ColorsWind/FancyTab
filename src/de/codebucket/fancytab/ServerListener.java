package de.codebucket.fancytab;

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
	}
}
