package fr.az.fatalstrike;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.swing.JFrame;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.resources.Resources;
import fr.az.fatalstrike.game.Action;
import fr.az.fatalstrike.game.field.GameField;
import fr.az.fatalstrike.ui.screen.IngameScreen;
import fr.az.fatalstrike.ui.screen.IngameScreen.ActionBar;
import fr.az.fatalstrike.ui.screen.MenuScreen;

public final class FatalStrike
{
	public static final String GAME_INFO_FILE = "game.xml";
	public static final String GAME_FILE = "game.litidata";
	
	public static final Graphics CRAPHICS = new BufferedImage(1, 1, 1).createGraphics();
	
	public static final Font FONT_GUI = Resources.fonts().get(PATHS.FONTS + "times_new_roman.ttf").deriveFont(40f);
	public static final Font FONT_GUI_SMALL = FONT_GUI.deriveFont(30f);
	public static final Font FONT_GAME = FONT_GUI;
	
	private static JFrame window;
	private static GameManager manager;
	protected static GameField field;
	
	public static JFrame window() { return window; }
	public static GameManager manager() { return manager; }
	public static GameField field() { return field; }
	
	private FatalStrike() {}
	
	public static synchronized void main(String... args)
	{	
		Game.setInfo(GAME_INFO_FILE);
		Game.init();
		
		FatalStrike.manager = new GameManager();
		FatalStrike.window = (JFrame) Game.window().getHostControl();
		
		FatalStrike.window.setResizable(false);
		FatalStrike.window.setExtendedState(JFrame.MAXIMIZED_BOTH);

		Resources.load(GAME_FILE);
		Game.screens().add(MenuScreen.screen());
		Game.screens().add(IngameScreen.screen());
		
		Game.graphics().setBaseRenderScale(6f * Game.window().getResolutionScale());
		Game.window().setIconImage(Resources.images().get("logo.png"));
		
		Game.world().addLoadedListener(e ->
		{
			Game.world().camera().setFocus(e.getCenter());
			FatalStrike.field = GameField.get(e.getMap().getTileLayers().get(0));
		});
		
		InputListener.init();
		Game.start();
	}
	
	public static final class GameManager
	{
		public static final Graphics CRAPHICS = new BufferedImage(1, 1, 1).createGraphics();
		
		public static final Font FONT_GUI = Resources.fonts().get(PATHS.FONTS + "times_new_roman.ttf").deriveFont(40f);
		public static final Font FONT_GUI_SMALL = FONT_GUI.deriveFont(30f);
		public static final Font FONT_GAME_TITLE = FONT_GUI.deriveFont(50f);
		
		private GameState state;
		
		{
			Game.screens().onScreenChanged(s ->
			{
				if (s == IngameScreen.screen())
				{
					ActionBar bar = IngameScreen.screen().getActionBar();
					bar.getActions().forEach(a -> a.addItems(Arrays.asList(new Action(Resources.images().get("old/elf.png")), new Action(Resources.images().get("old/ogre.png")), new Action(Resources.images().get("old/arrow.png")), new Action(Resources.images().get("ui/validation.png"))), Action::getIcon));
				}
			});
		}
		
		private GameManager() {}
		
		public void setState(GameState state) { this.state = state; }
		public GameState state() { return state; }
		
		public static enum GameState
		{
			MENU,
			PLAYING,
			;
		}
	}
	
	public static final class MAPS
	{
		private MAPS() {}
		public static final String TITLE = "title";
		public static final String FIGHT = "fight";
	}
	
	public static final class PATHS
	{
		private PATHS() {}
		public static final String FONTS = "fonts" + File.separatorChar;
		public static final String SPRITES = "sprites" + File.separatorChar;
	}
}
