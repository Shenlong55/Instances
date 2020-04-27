package com.hackquest.shenlong55.instances.instances;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import com.hackquest.shenlong55.ddpluginlibrary.DirectoryRemover;
import com.hackquest.shenlong55.instances.InstanceManager;

public final class InstancePrototype
{
	private final List<Instance> instances = new ArrayList<>();

	private final InstanceManager	instanceManager;
	private final File				prototypeFolder;
	private final String			prototypeName;

	private Integer	editableInstanceId;
	private boolean	initialized;

	public InstancePrototype(final InstanceManager instanceManager, final String prototypeName)
	{
		this.prototypeName = prototypeName;
		this.instanceManager = instanceManager;
		instanceManager.addInstancePrototype(this);

		prototypeFolder = new File(instanceManager.getPrototypesFolder(), prototypeName);
		if (!prototypeFolder.exists())
		{
			prototypeFolder.mkdir();
		}
		else if (prototypeFolder.list().length != 0)
		{
			initialized = true;
			deleteWorldId();
		}
	}

	public Instance getInstance()
	{
		return getInstance(false);
	}

	public Instance getInstance(final boolean editable)
	{
		if (!initialized)
		{
			initialize(editable);
		}

		if (editable && (editableInstanceId != null))
		{
			return instances.get(editableInstanceId);
		}

		final String instanceName = prototypeName + "_" + instances.size();
		final Instance instance = new Instance(this, instanceName, editable);
		instance.load();
		instances.add(instance);

		return instance;
	}

	public InstancePlayer getInstancePlayer(final Player player)
	{
		for (final Instance instance : instances)
		{
			final InstancePlayer instancePlayer = instance.getInstancePlayer(player);
			if (instancePlayer != null)
			{
				return instancePlayer;
			}
		}

		return null;
	}

	public String getName()
	{
		return prototypeName;
	}

	public void unload()
	{
		// Use a copy of the instances list to avoid ConcurrentModificationExceptions
		final List<Instance> instances = new ArrayList<>(this.instances);
		for (final Instance instance : instances)
		{
			instance.removePlayers();
		}

		instanceManager.removeInstancePrototype(this);
	}

	protected void deleteExtraFiles()
	{
		DirectoryRemover directoryRemover;

		// Delete the advancements folder if it exists
		final File advancementsFolder = new File(prototypeFolder, "advancements");
		if (advancementsFolder.exists())
		{
			directoryRemover = new DirectoryRemover(advancementsFolder);
			directoryRemover.removeDirectory();
		}

		// Delete the Bukkit world id file if it exists
		final File bukkitWorldIdFile = new File(prototypeFolder, "uid.dat");
		if (bukkitWorldIdFile.exists())
		{
			bukkitWorldIdFile.delete();
		}

		// Delete old level.dat file if it exists
		final File oldLevelDataFile = new File(prototypeFolder, "level.dat_old");
		if (oldLevelDataFile.exists())
		{
			oldLevelDataFile.delete();
		}

		// Delete the player data folder if it exists
		final File playerDataFolder = new File(prototypeFolder, "playerdata");
		if (playerDataFolder.exists())
		{
			directoryRemover = new DirectoryRemover(playerDataFolder);
			directoryRemover.removeDirectory();
		}

		// Delete the session lock file if it exists
		final File sessionLockFile = new File(prototypeFolder, "session.lock");
		if (sessionLockFile.exists())
		{
			sessionLockFile.delete();
		}

		// Delete the stats folder if it exists
		final File statsFolder = new File(prototypeFolder, "stats");
		if (statsFolder.exists())
		{
			directoryRemover = new DirectoryRemover(statsFolder);
			directoryRemover.removeDirectory();
		}
	}

	protected void deleteWorldId()
	{
		// Delete the Bukkit world id file if it exists
		final File bukkitWorldIdFile = new File(prototypeFolder, "uid.dat");
		if (bukkitWorldIdFile.exists())
		{
			bukkitWorldIdFile.delete();
		}
	}

	protected File getPrototypeFolder()
	{
		return prototypeFolder;
	}

	protected void removeInstance(final Instance instance)
	{
		if (instance.isEditable())
		{
			editableInstanceId = null;
		}

		instances.remove(instance);
	}

	private void initialize(final boolean keepLoaded)
	{
		// Create a new world
		final String worldName = prototypeName + "_0";
		final WorldCreator worldCreator = new WorldCreator(worldName);
		worldCreator.type(WorldType.FLAT);
		worldCreator.generateStructures(false);
		final World world = worldCreator.createWorld();

		// Set up the world border
		final WorldBorder worldBorder = world.getWorldBorder();
		worldBorder.setCenter(world.getSpawnLocation());
		worldBorder.setSize(128);

		// Create an instance using the newly created world and save it to the prototype folder
		final Instance instance = new Instance(this, worldName, true, world);
		instance.save();

		if (keepLoaded)
		{
			instances.add(instance);
			editableInstanceId = instances.indexOf(instance);
		}
		else
		{
			instance.unload();
		}

		initialized = true;
	}
}