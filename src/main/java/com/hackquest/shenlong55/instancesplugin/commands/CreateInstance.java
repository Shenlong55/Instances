package com.hackquest.shenlong55.instancesplugin.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hackquest.shenlong55.instancesplugin.InstanceManager;
import com.hackquest.shenlong55.instancesplugin.instances.Instance;
import com.hackquest.shenlong55.instancesplugin.instances.InstanceError;
import com.hackquest.shenlong55.instancesplugin.instances.InstancePlayer;
import com.hackquest.shenlong55.instancesplugin.instances.InstancePrototype;

public final class CreateInstance implements CommandExecutor
{
	private final InstanceManager instanceManager;

	public CreateInstance(final InstanceManager instanceManager)
	{
		this.instanceManager = instanceManager;
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args)
	{
		if ((args.length == 0) || (args.length > 2))
		{
			return false;
		}

		final String name = args[0];
		if (name.length() > 15)
		{
			sender.sendMessage("Name is too long, please choose a shorter name.");
			return true;
		}

		final InstancePrototype prototype = new InstancePrototype(instanceManager.getPrototypesFolder(), name);
		instanceManager.addInstancePrototype(prototype);
		prototype.initialize();

		if (sender instanceof Player)
		{
			final InstancePlayer player = new InstancePlayer(prototype, (Player) sender);
			try
			{
				// Save the players current state
				// (inventory, game mode, location)
				player.saveState();

				// Get an editable instance of the new prototype and teleport
				// the player to the spawn point
				final Instance instance = prototype.getInstance(true);
				instance.addPlayer(player);
				instance.teleportPlayerToSpawn(player);

				// set the player to creative mode and clear their inventory
				player.setGameMode(GameMode.CREATIVE);
				player.clearInventory();
			}
			catch (final InstanceError e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return true;
	}
}