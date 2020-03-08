package com.hackquest.shenlong55.instancesplugin;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.entity.Player;

import com.hackquest.shenlong55.instancesplugin.instances.Instance;
import com.hackquest.shenlong55.instancesplugin.instances.InstancePrototype;

public interface InstanceManager
{
	public void addInstancePrototype(InstancePrototype instancePrototype);

	public InstancePrototype getInstancePrototypeByName(String name);

	public Logger getLogger();

	public Instance getPlayerInstance(Player player);

	public File getPrototypesFolder();
}