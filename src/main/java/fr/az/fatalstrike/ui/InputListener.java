package fr.az.fatalstrike.ui;

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.input.Input;
import de.gurkenlabs.litiengine.resources.Resources;

public final class InputListener
{
	public static final int KEY_EXIT = KeyEvent.VK_ESCAPE;

	private InputListener() {}

	public static void init()
	{
		Input.mouse().setGrabMouse(true);
		Input.keyboard().onKeyPressed(KEY_EXIT, e -> System.exit(0));

		Image cursor = Resources.images().get("cursor.png");
		Game.window().getRenderComponent().setCursor(Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "custom"));
	}
}
