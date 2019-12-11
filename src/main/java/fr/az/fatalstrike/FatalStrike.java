package fr.az.fatalstrike;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFrame;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.resources.Resources;

import fr.az.fatalstrike.core.game.Race;
import fr.az.fatalstrike.ui.InputListener;
import fr.az.fatalstrike.ui.component.SlideMenu;
import fr.az.fatalstrike.ui.screen.IngameScreen;
import fr.az.fatalstrike.ui.screen.MenuScreen;
import fr.az.fatalstrike.ui.screen.SelectionScreen;

public enum FatalStrike
{
	;

	private static final String GAME_INFO_FILE = "game.xml";
	private static final String GAME_FILE = "game.litidata";

	private static JFrame WINDOW;
	private static GameManager GAME_MANAGER;
	private static UIManager UI_MANAGER;

	public static JFrame window() { return WINDOW; }
	public static GameManager gameManager() { return GAME_MANAGER; }
	public static UIManager uiManager() { return UI_MANAGER; }

	public static Dimension getEffectiveWindowSize()
	{
		Dimension window = FatalStrike.WINDOW.getSize();

		try
		{
			Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(FatalStrike.WINDOW.getGraphicsConfiguration());
			window.setSize(window.width - insets.left - insets.right, window.height - insets.top - insets.bottom);
		} catch (HeadlessException e) { System.err.println("WARN: the game window is headless > insets computation skipped"); }

		return window;
	}

	public static synchronized void main(String... args)
	{
		Game.setInfo(GAME_INFO_FILE);

		Game.init();
		Race.init();
		InputListener.init();

		FatalStrike.GAME_MANAGER = new GameManager();
		FatalStrike.UI_MANAGER = new UIManager();
		FatalStrike.WINDOW = (JFrame) Game.window().getHostControl();
		FatalStrike.WINDOW.setExtendedState(Frame.MAXIMIZED_BOTH);

		Resources.load(GAME_FILE);
		SlideMenu.ARROW_LEFT = UIManager.Images.ARROW_LEFT.getImage();
		SlideMenu.ARROW_RIGHT = UIManager.Images.ARROW_RIGHT.getImage();
		SlideMenu.FOCUS = UIManager.Images.FOCUS.getImage();

		Game.screens().add(MenuScreen.screen());
		Game.screens().add(SelectionScreen.screen());
		Game.screens().add(IngameScreen.screen());

		Game.graphics().setBaseRenderScale(2.1f * FatalStrike.WINDOW.getHeight() / 512f);
		Game.window().setIconImage(Resources.images().get("logo.png"));

		Game.start();
	}

	public static enum Path
	{
		GUI("ui"),
		FONTS(GUI + "fonts"),
		IMAGES(GUI + "images"),
		SPRITES("sprites"),
		;

		private final String path;

		private Path(String path)
		{
			this.path = path + File.separatorChar;
		}

		/** @return the path to this folder */
		public String getPath() { return this.path; }

		/** Same as {@linkplain Path#getPath()} */
		@Override public String toString() { return this.getPath(); }
	}
}
