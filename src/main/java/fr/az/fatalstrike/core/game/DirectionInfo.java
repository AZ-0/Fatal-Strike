package fr.az.fatalstrike.core.game;

import java.awt.image.BufferedImage;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.resources.Resources;

import fr.az.fatalstrike.FatalStrike.Path;

public class DirectionInfo
{
	private final static String DIR_PATH = "dir/";
	public final static BufferedImage IMG_DOWN = Resources.images().get(Path.GUI.getPath() + DIR_PATH +"down.png");
	public final static BufferedImage IMG_LEFT = Resources.images().get(Path.GUI.getPath() + DIR_PATH + "left.png");
	public final static BufferedImage IMG_RIGHT = Resources.images().get(Path.GUI.getPath() + DIR_PATH + "right.png");
	public final static BufferedImage IMG_UP = Resources.images().get(Path.GUI.getPath() + DIR_PATH + "up.png");

	public static BufferedImage provideImage(Direction direction)
	{
		switch(direction)
		{
			case DOWN: return IMG_DOWN;
			case LEFT: return IMG_LEFT;
			case RIGHT: return IMG_RIGHT;
			case UP: return IMG_UP;
			default: return null;
		}
	}

	protected Direction direction = Direction.UNDEFINED;

	public DirectionInfo() {}
	public DirectionInfo(Direction direction) { this.direction = direction; }

	public BufferedImage provideImage() { return DirectionInfo.provideImage(this.getDirection()); }

	public void setDirection(Direction direction) { this.direction = direction; }
	public Direction getDirection() { return this.direction; }
}
