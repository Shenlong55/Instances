package com.hackquest.shenlong55.instances;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.hackquest.shenlong55.ddpluginlibrary.DDPlugin;
import com.hackquest.shenlong55.instances.commands.CreateInstance;
import com.hackquest.shenlong55.instances.commands.EditInstance;
import com.hackquest.shenlong55.instances.commands.ExitInstance;
import com.hackquest.shenlong55.instances.commands.SaveInstance;
import com.hackquest.shenlong55.instances.commands.TestInstance;
import com.hackquest.shenlong55.instances.events.InstanceBlockBreakEvent;
import com.hackquest.shenlong55.instances.events.InstancePlayerQuitEvent;
import com.hackquest.shenlong55.instances.events.InstancePlayerRespawnEvent;
import com.hackquest.shenlong55.instances.instances.InstancePlayer;
import com.hackquest.shenlong55.instances.instances.InstancePrototype;

/**
 * @author shenlong55
 */
public final class InstancesPlugin extends DDPlugin implements InstanceManager
{
	private final List<InstancePrototype>	instancePrototypes	= new ArrayList<>();
	private final File						prototypesFolder	= new File(getDataFolder(), "prototypes");;

	@Override
	public void addInstancePrototype(final InstancePrototype instancePrototype)
	{
		instancePrototypes.add(instancePrototype);
	}

	@Override
	public InstancePlayer getInstancePlayer(final Player player)
	{
		for (final InstancePrototype prototype : instancePrototypes)
		{
			final InstancePlayer instancePlayer = prototype.getInstancePlayer(player);
			if (instancePlayer != null)
			{
				return instancePlayer;
			}
		}

		return null;
	}

	@Override
	public InstancePrototype getInstancePrototype(final String name)
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
	public File getPrototypesFolder()
	{
		return prototypesFolder;
	}

	@Override
	public void onDisable()
	{
		// Use a copy of the instancePrototypes list to avoid ConcurrentModificationExceptions
		final List<InstancePrototype> instancePrototypes = new ArrayList<>(this.instancePrototypes);
		for (final InstancePrototype instancePrototype : instancePrototypes)
		{
			instancePrototype.unload();
		}
	}

	@Override
	public void removeInstancePrototype(final InstancePrototype instancePrototype)
	{
		instancePrototypes.remove(instancePrototype);
	}

	@Override
	protected void preEnable()
	{
		setUpFolders();
		loadPrototypes();
	}

	@Override
	protected void registerCommands()
	{
		registerCommand("CreateInstance", new CreateInstance(this));
		registerCommand("EditInstance", new EditInstance(this));
		registerCommand("ExitInstance", new ExitInstance(this));
		registerCommand("SaveInstance", new SaveInstance(this));
		registerCommand("TestInstance", new TestInstance(this));
	}

	@Override
	protected void registerEvents()
	{
		registerEvent(new InstanceBlockBreakEvent(this));
		registerEvent(new InstancePlayerRespawnEvent(this));
		registerEvent(new InstancePlayerQuitEvent(this));
	}

	private void loadPrototypes()
	{
		getLogger().info("Loading prototypes...");
		for (final String prototypeName : prototypesFolder.list())
		{
			addInstancePrototype(new InstancePrototype(this, prototypeName));
			logDebug("Loaded prototype " + prototypeName + ".");
		}
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