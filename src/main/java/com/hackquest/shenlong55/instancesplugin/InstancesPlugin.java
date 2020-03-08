package com.hackquest.shenlong55.instancesplugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.hackquest.shenlong55.instancesplugin.commands.CreateInstance;
import com.hackquest.shenlong55.instancesplugin.commands.EditInstance;
import com.hackquest.shenlong55.instancesplugin.commands.ExitInstance;
import com.hackquest.shenlong55.instancesplugin.commands.SaveInstance;
import com.hackquest.shenlong55.instancesplugin.commands.TestInstance;
import com.hackquest.shenlong55.instancesplugin.instances.Instance;
import com.hackquest.shenlong55.instancesplugin.instances.InstanceBlockBreakEvent;
import com.hackquest.shenlong55.instancesplugin.instances.InstancePrototype;

/**
 * @author shenlong55
 */
public final class InstancesPlugin extends JavaPlugin implements InstanceManager
{
	private final List<InstancePrototype>	instancePrototypes	= new ArrayList<>();
	private final File						prototypesFolder	= new File(getDataFolder(), "prototypes");;
	private final boolean					verbose				= true;

	@Override
	public void addInstancePrototype(final InstancePrototype instancePrototype)
	{
		instancePrototypes.add(instancePrototype);
	}

	@Override
	public InstancePrototype getInstancePrototypeByName(final String name)
	{
		for (final InstancePrototype prototype : instancePrototypes)
		{
			if (prototype.getName().equalsIgnoreCase(name))
			{
				return prototype;
			}
		}

		return null;
	}

	@Override
	public Instance getPlayerInstance(final Player player)
	{
		for (final InstancePrototype instancePrototype : instancePrototypes)
		{
			for (final Instance instance : instancePrototype.getInstances())
			{
				if (instance.getInstancePlayer(player) != null)
				{
					return instance;
				}
			}
		}

		return null;
	}

	@Override
	public File getPrototypesFolder()
	{
		return prototypesFolder;
	}

	@Override
	public void onDisable()
	{
		for (final InstancePrototype prototype : instancePrototypes)
		{
			prototype.unloadInstances();
		}
	}

	@Override
	public void onEnable()
	{
		setUpFolders();
		registerCommands();
		registerEvents();
		loadPrototypes();
	}

	private void loadPrototypes()
	{
		getLogger().info("Loading prototypes...");
		for (final String prototypeName : prototypesFolder.list())
		{
			addInstancePrototype(new InstancePrototype(prototypesFolder, prototypeName));

			if (verbose)
			{
				getLogger().info("Loaded prototype " + prototypeName + ".");
			}
		}
	}

	private void registerCommands()
	{
		getCommand("CreateInstance").setExecutor(new CreateInstance(this));
		getCommand("EditInstance").setExecutor(new EditInstance(this));
		getCommand("ExitInstance").setExecutor(new ExitInstance(this));
		getCommand("SaveInstance").setExecutor(new SaveInstance(this));
		getCommand("TestInstance").setExecutor(new TestInstance(this));
	}

	private void registerEvents()
	{
		getServer().getPluginManager().registerEvents(new InstanceBlockBreakEvent(this), this);
	}

	private void setUpFolders()
	{
		if (!getDataFolder().exists())
		{
			getDataFolder().mkdir();
		}

		if (!prototypesFolder.exists())
		{
			prototypesFolder.mkdir();
		}
	}
}