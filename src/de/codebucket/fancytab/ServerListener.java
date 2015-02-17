package de.codebucket.fancytab;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
		Tablist.getInstance().addPlayer(e.getPlayer());
		Tablist.getInstance().setTablist(e.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void playerKick(PlayerKickEvent e) 
	{
		Tablist.getInstance().removeTablist(e.getPlayer());
		Tablist.getInstance().removePlayer(e.getPlayer());
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void playerQuit(PlayerQuitEvent e) 
	{
		Tablist.getInstance().removeTablist(e.getPlayer());
		Tablist.getInstance().removePlayer(e.getPlayer());
	}
}
