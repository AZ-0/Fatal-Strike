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
import fr.az.fatalstrike.ui.screen.IngameScreen;
import fr.az.fatalstrike.ui.screen.MenuScreen;

public enum FatalStrike
{
	;

	private static final String GAME_INFO_FILE = "game.xml";
	private static final String GAME_FILE = "game.litidata";

	private static JFrame window;
	private static GameManager gameManager;
	private static UIManager uiManager;

	public static JFrame window() { return window; }
	public static GameManager gameManager() { return gameManager; }
	public static UIManager uiManager() { return uiManager; }

	public static Dimension getEffectiveWindowSize()
	{
		Dimension window = FatalStrike.window.getSize();

		try
		{
			Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(FatalStrike.window.getGraphicsConfiguration());
			window.setSize(window.width - insets.left - insets.right, window.height - insets.top - insets.bottom);
		} catch (HeadlessException e) { System.err.println("WARN: the game window is headless > insets computation skipped"); }

		return window;
	}

	public static synchronized void main(String... args)
	{
		Game.setInfo(GAME_INFO_FILE);
		Game.init();

		FatalStrike.gameManager = new GameManager();
		FatalStrike.uiManager = new UIManager();
		FatalStrike.window = (JFrame) Game.window().getHostControl();
		FatalStrike.window.setExtendedState(Frame.MAXIMIZED_BOTH);

		Resources.load(GAME_FILE);
		Game.screens().add(MenuScreen.screen());
		Game.screens().add(IngameScreen.screen());

		Game.graphics().setBaseRenderScale(2.1f * FatalStrike.window.getHeight() / 512f);
		Game.window().setIconImage(Resources.images().get("logo.png"));

		InputListener.init();
		Race.init();
		Game.start();
	}

	private FatalStrike() {}

	public static enum Path
	{
		FONTS("fonts"),
		GUI("ui"),
		SPRITES("sprites"),
		;

		private final String path;

		private Path(String path)
		{
			this.path = path + File.separatorChar;
		}

		public String getPath() { return this.path; }
	}
}
