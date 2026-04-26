package com.customprojectilehider;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CustomProjectileHiderPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CustomProjectileHiderPlugin.class);
		RuneLite.main(args);
	}
}
