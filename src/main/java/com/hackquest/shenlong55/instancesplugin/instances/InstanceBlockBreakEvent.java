package com.hackquest.shenlong55.instancesplugin.instances;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.hackquest.shenlong55.instancesplugin.InstanceManager;

public final class InstanceBlockBreakEvent implements Listener
{
	private final InstanceManager instanceManager;

	public InstanceBlockBreakEvent(final InstanceManager instanceManager)
	{
		this.instanceManager = instanceManager;
	}

	@EventHandler
	public void onBlockBreak(final BlockBreakEvent event)
	{
		final Player player = event.getPlayer();
		final Instance instance = instanceManager.getPlayerInstance(player);
		if ((instance != null) && (instance.getInstancePlayer(player) != null) && instance.isPreserved())
		{
			event.setCancelled(true);
			player.sendMessage("You may not break blocks in this instance.");
		}
	}
}