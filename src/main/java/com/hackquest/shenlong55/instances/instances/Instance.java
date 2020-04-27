package com.hackquest.shenlong55.instances.instances;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.hackquest.shenlong55.ddpluginlibrary.DirectoryRemover;
import com.hackquest.shenlong55.ddpluginlibrary.FileCopier;

public final class Instance
{
	private final List<InstancePlayer> instancePlayers = new ArrayList<>();

	private final boolean			editable;
	private final File				instanceFolder;
	private final String			instanceName;
	private final InstancePrototype	instancePrototype;

	private boolean	preserved;
	private World	world;

	protected Instance(final InstancePrototype instancePrototype, final String name, final boolean editable)
	{
		this(instancePrototype, name, editable, null);
	}

	protected Instance(final InstancePrototype instancePrototype, final String instanceName, final boolean editable, final World world)
	{
		this.instancePrototype = instancePrototype;
		this.instanceName = instanceName;
		this.editable = editable;
		this.world = world;

		instanceFolder = new File(Bukkit.getWorldContainer(), instanceName);
	}

	public World getWorld()
	{
		return world;
	}

	public boolean isPreserved()
	{
		return preserved;
	}

	public boolean save()
	{
		if (editable)
		{
			// Save changes to disk
			world.save();

			try
			{
				// Copy the world files from the instance folder to the prototype folder
				final File prototypeFolder = instancePrototype.getPrototypeFolder();
				final FileCopier fileCopier = new FileCopier(instanceFolder, prototypeFolder);
				fileCopier.copyFiles();

				// Delete the Bukkit world id file
				instancePrototype.deleteWorldId();
			}
			catch (final IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

			return true;
		}

		return false;
	}

	protected void addInstancePlayer(final InstancePlayer player)
	{
		instancePlayers.add(player);
	}

	protected InstancePlayer getInstancePlayer(final Player player)
	{
		for (final InstancePlayer instancePlayer : instancePlayers)
		{
			if (instancePlayer.getPlayer().equals(player))
			{
				return instancePlayer;
			}
		}

		return null;
	}

	protected Location getSpawnLocation()
	{
		return world.getSpawnLocation();
	}

	protected boolean isEditable()
	{
		return editable;
	}

	protected void load()
	{
		// Create the instance folder
		instanceFolder.mkdir();

		try
		{
			// Copy the files from the prototype folder to the instance folder
			final FileCopier fileCopier = new FileCopier(instancePrototype.getPrototypeFolder(), instanceFolder);
			fileCopier.copyFiles();
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Load the world into Bukkit memory
		final WorldCreator worldCreator = new WorldCreator(instanceName);
		world = worldCreator.createWorld();
	}

	protected void removePlayer(final InstancePlayer player)
	{
		instancePlayers.remove(player);

		if (instancePlayers.isEmpty())
		{
			instancePrototype.removeInstance(this);
			unload();
		}
	}

	protected void removePlayers()
	{
		// Use a copy of the instancePlayers list to avoid ConcurrentModificationExceptions
		final List<InstancePlayer> instancePlayers = new ArrayList<>(this.instancePlayers);
		for (final InstancePlayer instancePlayer : instancePlayers)
		{
			instancePlayer.restoreState();
		}
	}

	protected void unload()
	{
		if (!instancePlayers.isEmpty())
		{
			removePlayers();
			return;
		}

		// Unload the world from Bukkit memory
		Bukkit.unloadWorld(world, false);

		// Delete instance folder
		final DirectoryRemover directoryRemover = new DirectoryRemover(instanceFolder);
		directoryRemover.removeDirectory();
	}
}