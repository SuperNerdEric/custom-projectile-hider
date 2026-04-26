package com.customprojectilehider;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import javax.inject.Singleton;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Projectile;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.Perspective;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

@Singleton
public class CustomProjectileHiderOverlay extends Overlay
{
	private final Client client;
	private final CustomProjectileHiderConfig config;

	@Inject
	private CustomProjectileHiderOverlay(Client client, CustomProjectileHiderConfig config)
	{
		this.client = client;
		this.config = config;

		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	@SuppressWarnings("deprecation")
	public Dimension render(Graphics2D graphics)
	{
		if (!config.showProjectileIds())
		{
			return null;
		}

		for (Projectile projectile : client.getProjectiles())
		{
			final String text = "(ID: " + projectile.getId() + ")";
			final LocalPoint point = new LocalPoint((int) projectile.getX(), (int) projectile.getY());
			final Point textLocation = Perspective.getCanvasTextLocation(client, graphics, point, text, 0);

			if (textLocation != null)
			{
				OverlayUtil.renderTextLocation(graphics, textLocation, text, Color.RED);
			}
		}

		return null;
	}
}
