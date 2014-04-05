package de.codebucket.fancytab.packet;

import de.codebucket.fancytab.FancyTab;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketHandler
{
	FancyTab plugin;
	private Class<?> packetPlayOutPlayerInfo;
	private Method getPlayerHandle;
	private Field getPlayerConnection;
	private Method sendPacket;

	public PacketHandler(FancyTab plugin)
	{
		try 
		{
			this.plugin = plugin;
			packetPlayOutPlayerInfo = getMCClass("PacketPlayOutPlayerInfo");
			getPlayerHandle = getCraftClass("entity.CraftPlayer").getMethod("getHandle");
			getPlayerConnection = getMCClass("EntityPlayer").getDeclaredField("playerConnection");
			sendPacket = getMCClass("PlayerConnection").getMethod("sendPacket", getMCClass("Packet"));
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public Object createTablistPacket(String text, boolean cancel, int ping) 
	{
		try
		{
			Object packet = packetPlayOutPlayerInfo.newInstance();
			Field a = packetPlayOutPlayerInfo.getDeclaredField("a");
			a.setAccessible(true);
			a.set(packet, text);
			Field b = packetPlayOutPlayerInfo.getDeclaredField("b");
			b.setAccessible(true);
			b.set(packet, cancel);
			Field c = packetPlayOutPlayerInfo.getDeclaredField("c");
			c.setAccessible(true);
			c.set(packet, ping);
			return packet;
		}
		catch (Exception e) {}
		return null;
	}
	
	public Object createTablistPacket(String text, boolean cancel) 
	{
		return createTablistPacket(text, cancel, 0);
	}

	public void sendPackets(final Player player, final List<Object> packets) 
	{
		try
		{
			for(Object packet : packets)
			{
				sendPacket.invoke(getPlayerConnection.get(getPlayerHandle.invoke(player)), packet);
			}
		}
		catch (Exception e) 
		{
			FancyTab.logConsole(Level.SEVERE, "An error has occurred whilst sending the packets. Is Bukkit up to date?");
			FancyTab.logConsole(Level.SEVERE, e.getMessage());
		}
	}
	
	public void sendPackets(final List<Object> packets) 
	{
		try
		{
			for(Player player : Bukkit.getOnlinePlayers())
			{
				for(Object packet : packets)
				{
					sendPacket.invoke(getPlayerConnection.get(getPlayerHandle.invoke(player)), packet);
				}
			}
		}
		catch (Exception e) 
		{
			FancyTab.logConsole(Level.SEVERE, "An error has occurred whilst sending the packets. Is Bukkit up to date?");
			FancyTab.logConsole(Level.SEVERE, e.getMessage());
		}
	}

	private Class<?> getMCClass(String name) throws ClassNotFoundException 
	{
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		String className = "net.minecraft.server." + version + name;
		return Class.forName(className);
	}

	private Class<?> getCraftClass(String name) throws ClassNotFoundException 
	{
		String version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + ".";
		String className = "org.bukkit.craftbukkit." + version + name;
		return Class.forName(className);
	}

}
