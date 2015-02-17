package de.codebucket.fancytab;

import de.codebucket.fancytab.packet.PacketHandler;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.mcstats.Metrics;

public class FancyTab extends JavaPlugin 
{
	Tablist tab;
	PacketHandler packet;
	ServerListener server;
	private static FancyTab instance;
	private String serverVersion = "UNKNOWN";
	private String bukkitVersion = "UNKNOWN";
	private int interval;
	private boolean metrics;
	private static boolean logconsole;
	private boolean autorefresh;
	
	@Override
	public void onEnable() 
	{
		instance = this;

		checkBukkitVersion();
		checkServerVersion();

		saveDefaultConfig();

		reloadConfig();

		this.interval = getConfig().getInt("refreshInterval");
		this.metrics = getConfig().getBoolean("useMetrics");
		logconsole = getConfig().getBoolean("logConsole");
		this.autorefresh = getConfig().getBoolean("autoRefresh");

		if (this.metrics) 
		{
			try 
			{
				Metrics metrics = new Metrics(this);
				metrics.start();
			} 
			catch (IOException e)
			{ 
				
			}
		}

		this.tab = new Tablist(this);
		this.packet = new PacketHandler(this);
		this.server = new ServerListener(this);
		Bukkit.getPluginManager().registerEvents(this.server, this);

		this.tab.getTablist().clear();

		List<String> tablist = getConfig().getStringList("Slots");
		Tablist.getInstance().loadTablist(tablist);

		reloadTablist();
		if (this.autorefresh) 
		{
			Bukkit.getScheduler().runTaskTimer(this, startRefreshTablist(), 1L, this.interval * 20L);
		}
		Bukkit.getScheduler().runTaskTimer(this, startRefreshPlayers(), 1L, 10L);

		getLogger().info("Version 2.2 by Codebucket");
	}
	
	@Override
	public void onDisable() 
	{
		Bukkit.getScheduler().cancelTasks(this);
		
		unloadTablist();
		this.tab.getTablist().clear();

		getLogger().info("FancyTab disabled!");
		getLogger().info("Thank you for using FancyTab.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if (command.getName().equalsIgnoreCase("tabreload")) 
		{
			String pre = "§2[§aFancy§bTab§2] §r";

			if (sender.hasPermission("fancytab.reload"))
			{
				alertOperators(sender, "§e§oReloading FancyTab...§7§o");
				sender.sendMessage(pre + "§eReloading FancyTab...");
				reloadConfig();
				Bukkit.getPluginManager().disablePlugin(this);
				Bukkit.getPluginManager().enablePlugin(this);
				alertOperators(sender, "§a§oFancyTab sucessfully reloaded.§7§o");
				sender.sendMessage(pre + "§aPlugin sucessfully reloaded.");
				return true;
			}

			sender.sendMessage(pre + "§cYou don't have permission to execute this command!");
			return true;
		}

		return true;
	}
	
	private void reloadTablist() 
	{
		for (Player player : Bukkit.getOnlinePlayers()) 
		{
			Tablist.getInstance().setTablist(player);
		}
	}
	
	private void unloadTablist()
	{
		for (Player player : Bukkit.getOnlinePlayers())
		{
			Tablist.getInstance().removeTablist(player);
		}
	}

	private void checkServerVersion() 
	{
		try 
		{
			for (Package pa : Package.getPackages()) 
			{
				if (pa.getName().startsWith("net.minecraft.server."))
				{
					this.serverVersion = pa.getName().split("\\.")[3];
					getLogger().info("This Server is running with CraftBukkit version " + this.bukkitVersion + "!");
					break;
				}
			}
		}
		catch (Exception e) 
		{
			getLogger().severe("Unknown or unsupported CraftBukkit version! Is the Plugin up to date?");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

	private void checkBukkitVersion() 
	{
		String version = Bukkit.getVersion();
		version = version.replace("(", "");
		version = version.replace(")", "");
		version = version.split(" ")[2];
		this.bukkitVersion = version;
	}

	public String getBukkitVersion() 
	{
		return this.bukkitVersion;
	}

	public String getServerVersion() 
	{
		return this.serverVersion;
	}

	private void alertOperators(CommandSender sender, String alert)
	{
		for (Player player : Bukkit.getOnlinePlayers()) 
		{
			if (player.isOp()) 
			{
				if (!sender.getName().equals(player.getName())) 
				{
					player.sendMessage("§7§o[" + sender.getName() + ": " + alert + "]");
				}
			}
		}
	}

	private BukkitRunnable startRefreshTablist() 
	{
		BukkitRunnable runnable = new BukkitRunnable() 
		{
			public void run()
			{
				for (Player player : Bukkit.getOnlinePlayers())
				{
					Tablist.getInstance().refreshTablist(player);
				}
			}
		};
		return runnable;
	}
	
	private BukkitRunnable startRefreshPlayers() 
	{
		BukkitRunnable runnable = new BukkitRunnable() 
		{
			public void run()
			{
				for (Player player : Bukkit.getOnlinePlayers())
				{
					Tablist.getInstance().refreshPlayers(player);
				}
			}
		};
		return runnable;
	}

	public static FancyTab getInstance()
	{
		return instance;
	}

	public static void logConsole(Level level, String error)
	{
		if (logconsole) 
		{
			Bukkit.getLogger().log(level, "[FancyTab] " + error);
		}
	}
}
