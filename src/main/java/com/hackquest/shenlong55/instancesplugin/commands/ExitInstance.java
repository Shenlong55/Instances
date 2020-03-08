package com.hackquest.shenlong55.instancesplugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hackquest.shenlong55.instancesplugin.InstanceManager;
import com.hackquest.shenlong55.instancesplugin.instances.Instance;
import com.hackquest.shenlong55.instancesplugin.instances.InstancePlayer;

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
		if (args.length > 1)
		{
			return false;
		}

		if (sender instanceof Player)
		{
			final Player player = (Player) sender;
			final Instance instance = instanceManager.getPlayerInstance(player);
			final InstancePlayer instancePlayer = instance.getInstancePlayer(player);

			instancePlayer.restoreState();
			instance.removePlayer(instancePlayer);
		}

		return true;
	}
}