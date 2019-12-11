package fr.az.fatalstrike;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import de.gurkenlabs.litiengine.resources.Resources;

import fr.az.fatalstrike.FatalStrike.Path;

public final class UIManager
{
	private static final String MENU_PATH = "menu" + File.separatorChar;

	public static final Graphics GRAPHICS = new BufferedImage(1, 1, 1).createGraphics();

	public static final Font FONT_GUI = Fonts.CHILLER.getFont().deriveFont(50f);
	public static final Font FONT_GUI_SMALL = FONT_GUI.deriveFont(40f);
	public static final Font FONT_GAME_TITLE = FONT_GUI.deriveFont(70f);

	UIManager() {}

	public static enum Images
	{
		ARROW_LEFT(MENU_PATH +"arrow_left"),
		ARROW_RIGHT(MENU_PATH +"arrow_right"),
		FOCUS(MENU_PATH +"focused");
		;

		private final String file;
		private final BufferedImage image;

		private Images(String file)
		{
			this.file = Path.IMAGES.getPath() + file + ".png";
			this.image = Resources.images().get(this.file);
		}

		public String getFile() { return this.file; }
		public BufferedImage getImage() { return this.image; }

		@Override public String toString() { return this.file; }
	}

	public static enum Fonts
	{
		CHILLER("chiller"),
		TIMES_NEW_ROMAN("times_new_roman"),
		;

		private final String file;
		private final Font font;

		private Fonts(String file)
		{
			this.file = Path.FONTS.getPath() + file + ".ttf";
			this.font = Resources.fonts().get(this.file);
		}

		public String getFile() { return this.file; }
		public Font getFont() { return this.font; }

		@Override public String toString() { return this.file; }
	}
}
