package com.hackquest.shenlong55.instances.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hackquest.shenlong55.instances.InstanceManager;
import com.hackquest.shenlong55.instances.instances.Instance;
import com.hackquest.shenlong55.instances.instances.InstancePlayer;
import com.hackquest.shenlong55.instances.instances.InstancePrototype;

public class EditInstance implements CommandExecutor
{
	private final InstanceManager instanceManager;

	public EditInstance(final InstanceManager instanceManager)
	{
		this.instanceManager = instanceManager;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		// instanceName is a required argument
		if (args.length != 1)
		{
			return false;
		}

		if (sender instanceof Player)
		{
			final Player player = (Player) sender;

			final InstancePrototype prototype = instanceManager.getInstancePrototype(args[0]);
			if (prototype == null)
			{
				sender.sendMessage("No instance prototype found with that name.");
				return true;
			}

			final Instance instance = prototype.getInstance(true);

			InstancePlayer instancePlayer = instanceManager.getInstancePlayer(player);
			if (instancePlayer == null)
			{
				instancePlayer = new InstancePlayer(player, instance);
			}
			else
			{
				instancePlayer.setInstance(instance);
			}
		}

		return true;
	}
}