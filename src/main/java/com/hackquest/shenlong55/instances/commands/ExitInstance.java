package com.hackquest.shenlong55.instances.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hackquest.shenlong55.instances.InstanceManager;
import com.hackquest.shenlong55.instances.instances.InstancePlayer;

public class ExitInstance implements CommandExecutor
{
	private final InstanceManager instanceManager;

	public ExitInstance(final InstanceManager instanceManager)
	{
		this.instanceManager = instanceManager;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length > 0)
		{
			return false;
		}

		if (sender instanceof Player)
		{
			final InstancePlayer instancePlayer = instanceManager.getInstancePlayer((Player) sender);
			if (instancePlayer != null)
			{
				instancePlayer.restoreState();
			}
		}

		return true;
	}
}