package de.codebucket.fancytab.event;

import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerTablistRefreshEvent extends Event implements Cancellable 
{
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private List<String> tablist;
	private boolean cancelled;

	public PlayerTablistRefreshEvent(Player player, List<String> tablist)
	{
		this.player = player;
		this.tablist = tablist;
	}
	
	@Override
	public HandlerList getHandlers()
	{
		return handlers;
	}

	public static HandlerList getHandlerList()
	{
		return handlers;
	}

	public Player getPlayer()
	{
		return this.player;
	}

	public List<String> getTablist()
	{
		return this.tablist;
	}

	public void setTablist(List<String> tablist)
	{
		this.tablist = tablist;
	}
	
	@Override
	public void setCancelled(boolean cancel)
	{
		this.cancelled = cancel;
	}
	
	@Override
	public boolean isCancelled()
	{
		return this.cancelled;
	}
}
