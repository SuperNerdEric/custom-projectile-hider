package com.customprojectilehider;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("customprojectilehider")
public interface CustomProjectileHiderConfig extends Config
{
	@ConfigItem(
		keyName = "hideProjectiles",
		name = "Hide projectiles",
		description = "Hide projectiles with IDs listed below"
	)
	default boolean hideProjectiles()
	{
		return true;
	}

	@ConfigItem(
		keyName = "projectileIds",
		name = "Projectile IDs",
		description = "Comma-separated projectile IDs to hide (example: 1, 2, 3)"
	)
	default String projectileIds()
	{
		return "";
	}

	@ConfigItem(
		keyName = "showProjectileIds",
		name = "Show projectile IDs",
		description = "Display projectile IDs above active projectiles"
	)
	default boolean showProjectileIds()
	{
		return false;
	}
}
