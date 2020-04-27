package com.hackquest.shenlong55.instances.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.hackquest.shenlong55.instances.InstanceManager;
import com.hackquest.shenlong55.instances.instances.Instance;
import com.hackquest.shenlong55.instances.instances.InstancePlayer;

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
		final InstancePlayer instancePlayer = instanceManager.getInstancePlayer(player);
		if (instancePlayer != null)
		{
			final Instance instance = instancePlayer.getInstance();
			if (instance.isPreserved())
			{
				event.setCancelled(true);
				player.sendMessage("You may not break blocks in this instance.");
			}
		}

	}
}