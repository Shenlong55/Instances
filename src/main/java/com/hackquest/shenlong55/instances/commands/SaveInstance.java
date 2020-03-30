package com.hackquest.shenlong55.instances.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hackquest.shenlong55.instances.InstanceManager;
import com.hackquest.shenlong55.instances.instances.Instance;
import com.hackquest.shenlong55.instances.instances.InstanceError;

public class SaveInstance implements CommandExecutor
{
	private final InstanceManager instanceManager;

	public SaveInstance(final InstanceManager instanceManager)
	{
		this.instanceManager = instanceManager;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if (args.length < 0)
		{
			return false;
		}

		if (sender instanceof Player)
		{
			final Instance instance = instanceManager.getPlayerInstance((Player) sender);
			try
			{
				instance.save();
			}
			catch (final InstanceError e)
			{
				sender.sendMessage(e.getMessage());
				instanceManager.getLogger().info(e.getMessage());
			}
		}

		sender.sendMessage("Changes saved to prototype.");

		return true;
	}
}