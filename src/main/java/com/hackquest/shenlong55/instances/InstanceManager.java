package com.hackquest.shenlong55.instances;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import com.hackquest.shenlong55.instances.instances.Instance;
import com.hackquest.shenlong55.instances.instances.InstancePrototype;

public interface InstanceManager
{
	public void addInstancePrototype(InstancePrototype instancePrototype);

	public InstancePrototype getInstancePrototypeByName(String name);

	public Logger getLogger();

	public Instance getPlayerInstance(Player player);

	public File getPrototypesFolder();
}