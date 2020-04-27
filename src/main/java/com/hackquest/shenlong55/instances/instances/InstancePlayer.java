package com.hackquest.shenlong55.instances.instances;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class InstancePlayer
{
	private final Player		player;
	private final ItemStack[]	armor;
	private final GameMode		gameMode;
	private final ItemStack[]	inventory;
	private final Location		location;

	private Instance instance;

	public InstancePlayer(final Player player, final Instance instance)
	{
		this.player = player;

		// Save the players state before they enter an instance
		armor = player.getInventory().getArmorContents();
		gameMode = player.getGameMode();
		inventory = player.getInventory().getContents();
		location = player.getLocation();

		// Set the players initial instance
		setInstance(instance);
	}

	public Instance getInstance()
	{
		return instance;
	}

	public void restoreState()
	{
		// Restore the players previous state
		player.teleport(location);
		player.setGameMode(gameMode);
		player.getInventory().setContents(inventory);
		player.getInventory().setArmorContents(armor);

		// Remove the player from their current instance
		instance.removePlayer(this);
	}

	public void setInstance(final Instance instance)
	{
		// Teleport the player to the instances spawn point and add this player to the instance
		player.teleport(instance.getSpawnLocation());
		instance.addInstancePlayer(this);

		// If the instance is editable set the player to creative mode and clear their inventory
		if (instance.isEditable())
		{
			player.setGameMode(GameMode.CREATIVE);
			player.getInventory().clear();
		}

		// Remove the player from their previous instance before changing their stored instance
		if (this.instance != null)
		{
			this.instance.removePlayer(this);
		}
		this.instance = instance;
	}

	protected Player getPlayer()
	{
		return player;
	}
}