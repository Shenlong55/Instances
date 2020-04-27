package com.hackquest.shenlong55.instances;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import com.hackquest.shenlong55.instances.instances.InstancePlayer;
import com.hackquest.shenlong55.instances.instances.InstancePrototype;

public interface InstanceManager
{
	public void addInstancePrototype(InstancePrototype instancePrototype);

	public InstancePlayer getInstancePlayer(Player player);

	public InstancePrototype getInstancePrototype(String name);

	public Logger getLogger();

	public File getPrototypesFolder();

	public void removeInstancePrototype(InstancePrototype instancePrototype);
}