package com.hackquest.shenlong55.instances.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.hackquest.shenlong55.instances.InstanceManager;
import com.hackquest.shenlong55.instances.instances.InstancePlayer;

public final class InstancePlayerQuitEvent implements Listener
{
	private final InstanceManager instanceManager;

	public InstancePlayerQuitEvent(final InstanceManager instanceManager)
	{
		this.instanceManager = instanceManager;
	}

	@EventHandler
	public void onPlayerQuit(final PlayerQuitEvent event)
	{
		final InstancePlayer instancePlayer = instanceManager.getInstancePlayer(event.getPlayer());
		if (instancePlayer != null)
		{
			instancePlayer.restoreState();
		}
	}
}
