package com.hackquest.shenlong55.instances.instances;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.hackquest.shenlong55.ddpluginlibrary.DirectoryRemover;
import com.hackquest.shenlong55.ddpluginlibrary.FileCopier;

public final class Instance implements Listener
{
	private final List<InstancePlayer>	players		= new ArrayList<>();
	private final boolean				preserved	= false;

	private final boolean			editable;
	private final File				folder;
	private final String			name;
	private final InstancePrototype	prototype;

	private World world;

	protected Instance(final InstancePrototype instancePrototype, final String name, final boolean editable)
	{
		this(instancePrototype, name, editable, null);
	}

	protected Instance(final InstancePrototype prototype, final String name, final boolean editable, final World world)
	{
		this.prototype = prototype;
		this.editable = editable;
		this.name = name;
		this.world = world;
		folder = new File(Bukkit.getWorldContainer(), name);
	}

	public void addPlayer(final InstancePlayer player)
	{
		players.add(player);
	}

	public InstancePlayer getInstancePlayer(final Player player)
	{
		for (final InstancePlayer instancePlayer : players)
		{
			if (instancePlayer.getPlayer().equals(player.getPlayer()))
			{
				return instancePlayer;
			}
		}

		return null;
	}

	public void removePlayer(final InstancePlayer player)
	{
		players.remove(player);

		if (players.isEmpty())
		{
			prototype.unloadInstance(this);
		}
	}

	public void save() throws InstanceError
	{
		if (editable)
		{
			// Save changes to disk
			world.save();

			try
			{
				// Copy the world files from the instance folder
				// to the prototype folder
				final FileCopier fileCopier = new FileCopier(folder, prototype.getPrototypeFolder());
				fileCopier.copyFiles();
			}
			catch (final IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			throw new InstanceError("Instance is not editable.");
		}
	}

	public void teleportPlayerToSpawn(final InstancePlayer instancePlayer)
	{
		instancePlayer.getPlayer().teleport(world.getSpawnLocation());
	}

	protected boolean isEditable()
	{
		return editable;
	}

	protected boolean isPreserved()
	{
		return preserved;
	}

	protected void load()
	{
		if (prototype.isInitialized())
		{
			// Create the instance folder
			folder.mkdir();

			try
			{
				// Copy the files from the prototype folder
				// to the instance folder
				final FileCopier fileCopier = new FileCopier(prototype.getPrototypeFolder(), folder);
				fileCopier.copyFiles();
			}
			catch (final IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Load the world into Bukkit memory
			final WorldCreator worldCreator = new WorldCreator(name);
			world = worldCreator.createWorld();
		}
		else
		{
			Bukkit.getLogger().info("Instance prototype has not been initialized.");
		}
	}

	protected void unload()
	{
		unload(false);
	}

	protected void unload(final boolean saveChanges)
	{
		final boolean saveChangesAndEditable = saveChanges && editable;

		// Unload the world from Bukkit memory
		Bukkit.unloadWorld(world, saveChangesAndEditable);

		if (saveChangesAndEditable)
		{
			try
			{
				// Delete the Bukkit world id file if it exists
				final File bukkitWorldId = new File(folder, "uid.dat");
				if (bukkitWorldId.exists())
				{
					bukkitWorldId.delete();
				}

				// Save the changes to the prototype folder
				final FileCopier fileCopier = new FileCopier(folder, prototype.getPrototypeFolder());
				fileCopier.copyFiles();
			}
			catch (final IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Delete instance folder
		final DirectoryRemover directoryRemover = new DirectoryRemover(folder);
		directoryRemover.removeDirectory();
	}
}