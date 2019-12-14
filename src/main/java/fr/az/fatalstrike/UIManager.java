package fr.az.fatalstrike;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.resources.Resources;

import fr.az.fatalstrike.FatalStrike.Path;

public final class UIManager
{
	private static final String MENU_PATH = "menu" + File.separatorChar;

	public static final Graphics GRAPHICS = new BufferedImage(1, 1, 1).createGraphics();

	UIManager()
	{
		Game.window().getRenderComponent().setBackground(Colors.BACKGROUND.get());
	}

	public static enum Colors
	{
		BACKGROUND(0x1E, 0x21, 0x2D),
		;

		private Color color;

		private Colors(int r, int g, int b) { this.color = new Color(r, g, b); }
		private Colors(Color color) { this.color = color; }

		public Color get() { return this.color; }

		@Override public String toString() { return Integer.toHexString(this.color.getRGB()); }
	}

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
		public BufferedImage get() { return this.image; }

		@Override public String toString() { return this.file; }
	}

	public static enum Fonts
	{
		CHILLER("chiller"),
		TIMES_NEW_ROMAN("times_new_roman"),

		GUI(CHILLER, 50f),
		GUI_SMALL(CHILLER, 40f),
		GUI_TITLE(CHILLER, 70f),
		;

		public static Font get(String resource) { return Resources.fonts().get(resource); }

		private final String file;
		private final Font font;

		private Fonts(String file)
		{
			this.file = Path.FONTS.getPath() + file + ".ttf";
			this.font = Resources.fonts().get(this.file);
		}

		private Fonts(Fonts base, float size)
		{
			this.file = base.file;
			this.font = base.font.deriveFont(size);
		}

		public String getFile() { return this.file; }
		public Font get() { return this.font; }
		public Font get(float size) { return this.font.deriveFont(size); }

		@Override public String toString() { return this.file; }
	}
}
