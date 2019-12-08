package fr.az.fatalstrike;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import de.gurkenlabs.litiengine.resources.Resources;

import fr.az.fatalstrike.FatalStrike.Path;

public final class UIManager
{
	public static final Graphics GRAPHICS = new BufferedImage(1, 1, 1).createGraphics();

	public static final Font FONT_GUI = Resources.fonts().get(Path.FONTS.getPath() + "times_new_roman.ttf").deriveFont(40f);
	public static final Font FONT_GUI_SMALL = FONT_GUI.deriveFont(30f);
	public static final Font FONT_GAME_TITLE = FONT_GUI.deriveFont(50f);

	UIManager() {}

	public static enum Image
	{
		ARROW_LEFT("arrow_left"),
		ARROW_RIGHT("arrow_right"),
		;

		private final String file;
		private final BufferedImage image;

		private Image(String file)
		{
			this.file = file + ".png";
			this.image = Resources.images().get(Path.GUI.getPath() + this.file);
		}

		public String getFileName() { return this.file; }
		public BufferedImage getImage() { return this.image; }
	}
}
