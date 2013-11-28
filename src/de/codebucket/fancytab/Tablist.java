package de.codebucket.fancytab;

import de.codebucket.fancytab.event.PlayerTablistRefreshEvent;
import de.codebucket.fancytab.packet.PacketHandler;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

public class Tablist 
{
	FancyTab plugin;
	PacketHandler packet;
	private static Tablist instance;
	private ArrayList<String> tablistSlots = new ArrayList<>();
	private ArrayList<Player> useCustomTablist = new ArrayList<>();
	private Map<Player, List<String>> playerTablists = new HashMap<Player, List<String>>();
	private Map<Player, List<String>> playerCustomTablists = new HashMap<Player, List<String>>();

	public Tablist(FancyTab plugin)
	{
		this.plugin = plugin;
		this.packet = new PacketHandler(plugin);
		instance = this;
	}

	public void setTablist(Player player) 
	{
		if (!this.useCustomTablist.contains(player) && !this.playerCustomTablists.containsKey(player) && !this.playerTablists.containsKey(player)) 
		{
			List<String> tablist = new ArrayList<String>();

			for (String string : this.tablistSlots) 
			{
				tablist.add(string);
			}
			
			for (int i = 0; i < tablist.size(); i++) 
			{
				String slot = tablist.get(i);
				slot = userValues(slot, player);
				slot = textValues(slot);
				slot = editText(slot);
				
				tablist.set(i, slot);
			}

			for (Player p : Bukkit.getOnlinePlayers()) 
			{
				this.packet.sendPacketRequest(player, p.getPlayerListName(), false);
			}

			PlayerTablistRefreshEvent event = new PlayerTablistRefreshEvent(player, tablist);
			Bukkit.getPluginManager().callEvent(event);

			if (!event.isCancelled())
			{
				for (String s : event.getTablist()) 
				{
					this.packet.sendPacketRequest(player, s, true);
				}

				this.playerTablists.put(player, event.getTablist());
			}

			for (Player p : Bukkit.getOnlinePlayers()) 
			{
				this.packet.sendPacketRequest(player, p.getPlayerListName(), true);
			}
		}
	}

	public void refreshTablist(Player player) 
	{
		if (!this.useCustomTablist.contains(player) && !this.playerCustomTablists.containsKey(player) && this.playerTablists.containsKey(player)) 
		{
			List<String> tablist = new ArrayList<String>();

			for (String string : this.tablistSlots) 
			{
				tablist.add(string);
			}

			for (int i = 0; i < tablist.size(); i++)
			{
				String slot = tablist.get(i);
				slot = userValues(slot, player);
				slot = textValues(slot);
				slot = editText(slot);

				tablist.set(i, slot);
			}

			for (String s : this.playerTablists.get(player)) 
			{ 
				this.packet.sendPacketRequest(player, s, false);
			}
			
			this.playerTablists.remove(player);

			for (Player p : Bukkit.getOnlinePlayers())
			{
				this.packet.sendPacketRequest(player, p.getPlayerListName(), false);
			}

			PlayerTablistRefreshEvent event = new PlayerTablistRefreshEvent(player, tablist);
			Bukkit.getPluginManager().callEvent(event);

			if (!event.isCancelled())
			{
				for (String s : event.getTablist()) 
				{
					this.packet.sendPacketRequest(player, s, true);
				}

				this.playerTablists.put(player, event.getTablist());
			}

			for (Player p : Bukkit.getOnlinePlayers()) 
			{
				this.packet.sendPacketRequest(player, p.getPlayerListName(), true);
			}
		}
	}

	public void removeTablist(Player player)
	{
		if (!this.useCustomTablist.contains(player) && !this.playerCustomTablists.containsKey(player) && this.playerTablists.containsKey(player)) 
		{
			List<String> tablist = new ArrayList<String>();

			for (String string : this.tablistSlots) 
			{
				tablist.add(string);
			}

			for (int i = 0; i < tablist.size(); i++) 
			{
				String slot = tablist.get(i);
				slot = userValues(slot, player);
				slot = textValues(slot);
				slot = editText(slot);

				tablist.set(i, slot);
			}
			
			for (String slot : this.playerTablists.get(player)) 
			{
				this.packet.sendPacketRequest(player, slot, false);
			}
			
			this.playerTablists.remove(player);
		}
	}

	public void setCustomTablist(Player player, List<String> slots) 
	{
		if (!this.useCustomTablist.contains(player) && !this.playerCustomTablists.containsKey(player)) 
		{
			List<String> tablist = new ArrayList<String>();

			for (String string : slots) 
			{
				tablist.add(string);
			}
			
			for (int i = 0; i < tablist.size(); i++)
			{
				String slot = tablist.get(i);
				slot = userValues(slot, player);
				slot = textValues(slot);
				slot = editText(slot);

				tablist.set(i, slot);
			}

			for (Player p : Bukkit.getOnlinePlayers()) 
			{
				this.packet.sendPacketRequest(player, p.getPlayerListName(), false);
			}

			for (String s : tablist) 
			{
				this.packet.sendPacketRequest(player, s, true);
			}

			for (Player p : Bukkit.getOnlinePlayers()) 
			{
				this.packet.sendPacketRequest(player, p.getPlayerListName(), true);
			}

			this.useCustomTablist.add(player);
			this.playerCustomTablists.put(player, tablist);
		}
	}

	public void refreshCustomTablist(Player player, List<String> slots)
	{
		if (this.useCustomTablist.contains(player) && this.playerCustomTablists.containsKey(player)) 
		{
			List<String> tablist = new ArrayList<String>();

			for (String string : slots)
			{
				tablist.add(string);
			}

			for (int i = 0; i < tablist.size(); i++) 
			{
				String slot = (String)tablist.get(i);
				slot = userValues((String)slot, player);
				slot = textValues((String)slot);
				slot = editText((String)slot);

				tablist.set(i, slot);
			}
			
			for (String s : playerCustomTablists.get(player)) 
			{
				this.packet.sendPacketRequest(player, s, false);
			}
			
			this.playerCustomTablists.remove(player);

			for (Player p : Bukkit.getOnlinePlayers())
			{
				this.packet.sendPacketRequest(player, p.getPlayerListName(), false);
			}

			for (String s : tablist) 
			{
				this.packet.sendPacketRequest(player, s, true);
			}

			for (Player p : Bukkit.getOnlinePlayers())
			{
				this.packet.sendPacketRequest(player, p.getPlayerListName(), true);
			}
			
			this.playerCustomTablists.put(player, tablist);
		}
	}

	public void removeCustomTablist(Player player) 
	{
		if (this.useCustomTablist.contains(player) && this.playerCustomTablists.containsKey(player)) 
		{
			for (String s : playerCustomTablists.get(player)) 
			{
				this.packet.sendPacketRequest(player, s, false);
			}

			for (Player p : Bukkit.getOnlinePlayers()) 
			{
				this.packet.sendPacketRequest(player, p.getPlayerListName(), false);
			}

			this.useCustomTablist.remove(player);
			this.playerCustomTablists.remove(player);
		}
	}

	public void loadTablist(List<String> tablist) 
	{
		for (String s : tablist) 
		{
			this.tablistSlots.add(s);
		}
	}

	public List<String> getTablist() 
	{
		return this.tablistSlots;
	}

	public List<Player> getCustomTablistPlayers() 
	{
		return this.useCustomTablist;
	}

	public Map<Player, List<String>> getPlayerTablists() 
	{
		return this.playerTablists;
	}

	private String textValues(String line) 
	{
		line = line.replaceAll("%sl1", "–„ –ˆ –ˆ –ˆ –€");
		line = line.replaceAll("%sl2", "–€ –ˆ –ˆ –ˆ –„");
		line = line.replaceAll("%sl3", "–„–„ – –  –€–€");
		line = line.replaceAll("%sl4", "–€–€ – –  –„–„");
		line = line.replaceAll("%sl5", "–¼ –¼ –¼");
		line = line.replaceAll("%sl6", "–  –¡ – ");
		line = line.replaceAll("%sl7", "–¡ –  –¡");
		line = line.replaceAll("%sl8", "–ˆ –ˆ –ˆ");
		line = ChatColor.translateAlternateColorCodes('&', line);
		line = line.replaceAll("&&", "&");
		line = line.replaceAll("%a", "Ã¤");
		line = line.replaceAll("%A", "Ã„");
		line = line.replaceAll("%o", "Ã¶");
		line = line.replaceAll("%O", "Ã–");
		line = line.replaceAll("%u", "Ã¼");
		line = line.replaceAll("%U", "Ãœ");
		line = line.replaceAll("%s", "ÃŸ");

		return line;
	}

	@SuppressWarnings("deprecation")
	private String userValues(String line, Player player)
	{
		Calendar cal = Calendar.getInstance();

		String d = new SimpleDateFormat("dd_MM_yyyy").format(Calendar.getInstance().getTime());
		String[] date = d.split("_");

		String ip_adress = player.getAddress().getAddress().toString();
		ip_adress = ip_adress.replaceAll("/", "");

		PluginDescriptionFile descFile = this.plugin.getDescription();
		String version = descFile.getVersion();

		String bukkitVersion = this.plugin.getBukkitVersion();
		String serverVersion = this.plugin.getServerVersion();

		line = line.replaceAll("%username%", player.getName());
		line = line.replaceAll("%displayname%", player.getDisplayName());
		line = line.replaceAll("%numplayers%", String.valueOf(this.plugin.getServer().getOnlinePlayers().length));
		line = line.replaceAll("%maxplayers%", String.valueOf(this.plugin.getServer().getMaxPlayers()));
		line = line.replaceAll("%ip-adress%", ip_adress);
		line = line.replaceAll("%day%", fixFormat(String.valueOf(date[0])));
		line = line.replaceAll("%month%", fixFormat(String.valueOf(date[1])));
		line = line.replaceAll("%year%", fixFormat(String.valueOf(date[2])));
		line = line.replaceAll("%hours%", fixFormat(String.valueOf(cal.getTime().getHours())));
		line = line.replaceAll("%minutes%", fixFormat(String.valueOf(cal.getTime().getMinutes())));
		line = line.replaceAll("%seconds%", fixFormat(String.valueOf(cal.getTime().getSeconds())));
		line = line.replaceAll("%version%", version);
		line = line.replaceAll("%mc-version%", bukkitVersion);
		line = line.replaceAll("%serverversion%", serverVersion);
		line = line.replaceAll("%servername%", this.plugin.getServer().getServerName());
		line = line.replaceAll("%serverip%", this.plugin.getServer().getIp());
		line = line.replaceAll("%motd%", this.plugin.getServer().getMotd());

		return line;
	}

	private String fixFormat(String input) 
	{
		if (input.length() == 1) 
		{
			input = "0" + input;
		}

		return input;
	}

	private String editText(String text) 
	{
		int length = text.length();

		if (length > 16)
		{
			text = text.substring(0, 16);
		}

		length = text.length();

		if (length != 16)
		{
			length = 16 - length;

			for (int i = 0; i < length; i++) 
			{
				text = text + " ";
			}
		}

		return text;
	}

	public static Tablist getInstance() 
	{
		return instance;
	}
}
