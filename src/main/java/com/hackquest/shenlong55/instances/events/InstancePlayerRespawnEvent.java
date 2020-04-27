package com.hackquest.shenlong55.instances.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.hackquest.shenlong55.instances.InstanceManager;
import com.hackquest.shenlong55.instances.instances.InstancePlayer;

public final class InstancePlayerRespawnEvent implements Listener
{
	private final InstanceManager instanceManager;

	public InstancePlayerRespawnEvent(final InstanceManager instanceManager)
	{
		this.instanceManager = instanceManager;
	}

	@EventHandler
	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		final Player player = event.getPlayer();
		final InstancePlayer instancePlayer = instanceManager.getInstancePlayer(player);
		if (instancePlayer != null)
		{
			final Location playerSpawnLocation = player.getBedSpawnLocation();
			if ((playerSpawnLocation == null) || (playerSpawnLocation.getWorld() != instancePlayer.getInstance().getWorld()))
			{
				instancePlayer.restoreState();
			}
		}
	}
}