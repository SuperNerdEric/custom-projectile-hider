package com.customprojectilehider;

import com.google.inject.Provides;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Projectile;
import net.runelite.api.Renderable;
import net.runelite.client.callback.Hooks;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Custom Projectile Hider",
	description = "Hide specific projectile IDs",
	tags = {"projectile", "hider", "hide"}
)
@SuppressWarnings("deprecation")
public class CustomProjectileHiderPlugin extends Plugin
{
	@Inject
	private CustomProjectileHiderConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private CustomProjectileHiderOverlay overlay;

	@Inject
	private Hooks hooks;

	private final Hooks.RenderableDrawListener drawListener = this::shouldDraw;

	private Set<Integer> hiddenProjectileIds = Collections.emptySet();
	private boolean hideProjectiles;

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
		updateConfig();
		hooks.registerRenderableDrawListener(drawListener);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		hooks.unregisterRenderableDrawListener(drawListener);
		hiddenProjectileIds = Collections.emptySet();
		hideProjectiles = false;
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged configChanged)
	{
		if (!"customprojectilehider".equals(configChanged.getGroup()))
		{
			return;
		}

		if ("projectileIds".equals(configChanged.getKey()))
		{
			updateConfig();
		}
		else if ("hideProjectiles".equals(configChanged.getKey()))
		{
			updateConfig();
		}
	}

	private void updateConfig()
	{
		hideProjectiles = config.hideProjectiles();
		hiddenProjectileIds = parseProjectileIds(config.projectileIds());
	}

	private boolean shouldDraw(Renderable renderable, boolean drawingUi)
	{
		if (!(renderable instanceof Projectile))
		{
			return true;
		}

		if (!hideProjectiles || hiddenProjectileIds.isEmpty())
		{
			return true;
		}

		Projectile projectile = (Projectile) renderable;
		return !hiddenProjectileIds.contains(projectile.getId());
	}

	private Set<Integer> parseProjectileIds(String rawIds)
	{
		if (rawIds == null || rawIds.trim().isEmpty())
		{
			return Collections.emptySet();
		}

		return Arrays.stream(rawIds.split(","))
			.map(String::trim)
			.filter(id -> !id.isEmpty())
			.map(this::tryParseId)
			.filter(id -> id >= 0)
			.collect(Collectors.toSet());
	}

	private int tryParseId(String value)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch (NumberFormatException ex)
		{
			log.debug("Skipping invalid projectile id '{}'", value);
			return -1;
		}
	}

	@Provides
	CustomProjectileHiderConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CustomProjectileHiderConfig.class);
	}
}
