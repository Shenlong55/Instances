package com.hackquest.shenlong55.instances.instances;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class InstancePlayer
{
	private final Player player;

	private ItemStack[]	armor;
	private GameMode	gameMode;
	private ItemStack[]	inventory;
	private Location	location;

	public InstancePlayer(final InstancePrototype prototype, final Player player)
	{
		this.player = player;
	}

	public void clearInventory()
	{
		player.getInventory().clear();
	}

	public Player getPlayer()
	{
		return player;
	}

	public void restoreState()
	{
		player.teleport(location);
		player.setGameMode(gameMode);
		player.getInventory().setContents(inventory);
		player.getInventory().setArmorContents(armor);
	}

	public void saveState()
	{
		armor = player.getInventory().getArmorContents();
		gameMode = player.getGameMode();
		inventory = player.getInventory().getContents();
		location = player.getLocation();
	}

	public void setGameMode(final GameMode gameMode)
	{
		player.setGameMode(gameMode);
	}
}