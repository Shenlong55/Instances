package com.hackquest.shenlong55.instances.instances;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import com.hackquest.shenlong55.ddpluginlibrary.DirectoryRemover;
import com.hackquest.shenlong55.ddpluginlibrary.FileCopier;

public final class InstancePrototype
{
	private final List<Instance> instances = new ArrayList<>();

	private final File		folder;
	private final String	name;

	private Integer	editableInstanceId;
	private boolean	initialized;

	public InstancePrototype(final File prototypesFolder, final String name)
	{
		this.name = name;

		folder = new File(prototypesFolder, name);
		// If the prototype folder does not exist...
		if (!folder.exists())
		{
			// Create it
			folder.mkdir();
		}
		else if (folder.list().length != 0)// If files exist in the folder...
		{
			// Set initialized to true and...
			initialized = true;

			// Delete the Bukkit world id file if it exists
			final File bukkitWorldId = new File(folder, "uid.dat");
			if (bukkitWorldId.exists())
			{
				bukkitWorldId.delete();
			}
		}
	}

	public Instance getInstance(final boolean editable) throws InstanceError
	{
		if (initialized)
		{
			if (editable && (editableInstanceId != null))
			{
				return instances.get(editableInstanceId);
			}

			final String instanceName = name + "_" + instances.size();
			final Instance instance = new Instance(this, instanceName, editable);
			instance.load();

			instances.add(instance);

			return instance;
		}
		else
		{
			Bukkit.getLogger().info("Instance prototype needs to be initialized.");
			throw new InstanceError("Instance prototype needs to be initialized.");
		}

	}

	public List<Instance> getInstances()
	{
		return instances;
	}

	public String getName()
	{
		return name;
	}

	public void initialize()
	{
		initialize(true);
	}

	public void initialize(final boolean keepLoaded)
	{
		if (initialized)
		{
			Bukkit.getLogger().info("Instance prototype has already been initialized.");
			return;

		}
		else
		{
			// Create a new world
			final String worldName = name + "_0";
			final WorldCreator worldCreator = new WorldCreator(worldName);
			worldCreator.type(WorldType.FLAT);
			worldCreator.generateStructures(false);

			final World world = worldCreator.createWorld();

			// Set up the world border
			final WorldBorder worldBorder = world.getWorldBorder();
			worldBorder.setCenter(world.getSpawnLocation());
			worldBorder.setSize(128);

			if (keepLoaded)
			{
				final Instance instance = new Instance(this, worldName, true, world);

				instances.add(instance);
				editableInstanceId = 0;

			}
			else
			{
				// Unload the world from Bukkit memory
				Bukkit.unloadWorld(world, true);

				try
				{
					// Delete the Bukkit world id file if it exists
					final File bukkitWorldId = new File(folder, "uid.dat");
					if (bukkitWorldId.exists())
					{
						bukkitWorldId.delete();
					}

					// Save the world to the prototype folder
					final FileCopier fileCopier = new FileCopier(world.getWorldFolder(), folder);
					fileCopier.copyFiles();
				}
				catch (final IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Delete the world folder
				final DirectoryRemover directoryRemover = new DirectoryRemover(world.getWorldFolder());
				directoryRemover.removeDirectory();
			}

			initialized = true;
		}
	}

	public void unloadInstances()
	{
		final Iterator<Instance> iterator = instances.iterator();
		while (iterator.hasNext())
		{
			final Instance instance = iterator.next();
			unloadInstance(instance);
		}
	}

	protected File getPrototypeFolder()
	{
		return folder;
	}

	protected boolean isInitialized()
	{
		return initialized;
	}

	protected void unloadInstance(final Instance instance)
	{
		if (instance.isEditable())
		{
			editableInstanceId = null;
		}

		instance.unload();
		instances.remove(instance);
	}
}