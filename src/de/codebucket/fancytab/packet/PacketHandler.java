package de.codebucket.fancytab.packet;

import de.codebucket.fancytab.FancyTab;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketHandler
 {
	FancyTab plugin;
	private String version = "";

	public PacketHandler(FancyTab plugin) 
	{
		this.plugin = plugin;
		this.version = plugin.getServerVersion();
	}

	public void sendPacketRequest(Player player, String text, boolean cancel) 
	{
		NMSPacket packet = null;
		try
		{
			packet = new NMSPacket("Packet201PlayerInfo");
		} 
		catch (Exception e)
		{
			FancyTab.logConsole(Level.SEVERE, "Unknown or unsupported CraftBukkit version! Is the Plugin up to date?");
			FancyTab.logConsole(Level.SEVERE, e.getMessage());
		}

		packet.setField("a", text);
		packet.setField("b", Boolean.valueOf(cancel));
		packet.setField("c", Integer.valueOf(0));

		sendPacket(player, packet.getPacket());
	}
	
	public void sendPacketRequest(String text, boolean cancel) 
	{
		NMSPacket packet = null;
		try
		{
			packet = new NMSPacket("Packet201PlayerInfo");
		} 
		catch (Exception e)
		{
			FancyTab.logConsole(Level.SEVERE, "Unknown or unsupported CraftBukkit version! Is the Plugin up to date?");
			FancyTab.logConsole(Level.SEVERE, e.getMessage());
		}

		packet.setField("a", text);
		packet.setField("b", Boolean.valueOf(cancel));
		packet.setField("c", Integer.valueOf(0));

		sendPackets(packet.getPacket());
	}

	public void sendPacket(Player player, Object o) 
	{
		try 
		{
			Class<?> packet = Class.forName("net.minecraft.server." + this.version + ".Packet");
			Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + this.version + ".entity.CraftPlayer");

			if (!packet.isAssignableFrom(o.getClass())) 
			{
				throw new IllegalArgumentException("Object o wasn't a packet!");
			}

			Object cp = craftPlayer.cast(player);
			Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke(cp, new Object[0]);
			Object con = handle.getClass().getField("playerConnection").get(handle);
			con.getClass().getMethod("sendPacket", new Class[] { packet }).invoke(con, new Object[] { o });
		} 
		catch (Exception e)
		{
			FancyTab.logConsole(Level.SEVERE, "An error has occurred whilst sending the packets. Is Bukkit up to date?");
			FancyTab.logConsole(Level.SEVERE, e.getMessage());
		}
	}

	public void sendPackets(Object o)
	{
		try 
		{
			Class<?> packet = Class.forName("net.minecraft.server." + this.version + ".Packet");
			Class<?> craftPlayer = Class.forName("org.bukkit.craftbukkit." + this.version + ".entity.CraftPlayer");
			for (Player player : Bukkit.getOnlinePlayers()) 
			{
				if (!packet.isAssignableFrom(o.getClass())) 
				{
					throw new IllegalArgumentException("Object o wasn't a packet!");
				}

				Object cp = craftPlayer.cast(player);
				Object handle = craftPlayer.getMethod("getHandle", new Class[0]).invoke(cp, new Object[0]);
				Object con = handle.getClass().getField("playerConnection").get(handle);
				con.getClass().getMethod("sendPacket", new Class[] { packet }).invoke(con, new Object[] { o });
			}
		} 
		catch (Exception e) 
		{
			FancyTab.logConsole(Level.SEVERE, "An error has occurred whilst sending the packets. Is Bukkit up to date?");
			FancyTab.logConsole(Level.SEVERE, e.getMessage());
		}
	}
}
