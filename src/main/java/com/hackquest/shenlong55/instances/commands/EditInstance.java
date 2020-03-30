package com.hackquest.shenlong55.instances.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.hackquest.shenlong55.instances.InstanceManager;
import com.hackquest.shenlong55.instances.instances.Instance;
import com.hackquest.shenlong55.instances.instances.InstanceError;
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
		if ((args.length == 0) || (args.length > 1))
		{
			return false;
		}

		if (sender instanceof Player)
		{
			final InstancePrototype prototype = instanceManager.getInstancePrototypeByName(args[0]);

			if (prototype == null)
			{
				sender.sendMessage("No instance with that name found.");
				return true;
			}

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