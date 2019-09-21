package fr.az.fatalstrike;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.resources.Resources;
import fr.az.fatalstrike.game.DirectionInfo;
import fr.az.fatalstrike.game.Player;
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
	
	public static Dimension getEffectiveWindowSize()
	{
		Dimension window = FatalStrike.window.getSize();
		
		try
		{
			Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(FatalStrike.window.getGraphicsConfiguration());
			window.setSize(window.width - insets.left - insets.right, window.height - insets.top - insets.bottom);
		} catch (HeadlessException e) {}
		
		return window;
	}
	
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
		
		private List<Player> players = new ArrayList<>();
		private GameState state;
		
		{
			Game.screens().onScreenChanged(s ->
			{
				if (s == IngameScreen.screen())
				{
					ActionBar bar = IngameScreen.screen().getActionBar();
					bar.getDirections().forEach(d -> d.addItems(DirectionInfo::provideImage, Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT));
				}
			});
			
			players.add(new Player(Player.Character.ELF));
			players.add(new Player(Player.Character.OGRE));
		}
		
		private GameManager() {}
		
		public void setState(GameState state) { this.state = state; }
		public GameState state() { return state; }
		public List<Player> players() { return Collections.unmodifiableList(this.players); }
		
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
		public static final String GUI = "ui" + File.separatorChar;
		public static final String SPRITES = "sprites" + File.separatorChar;
	}
	
	public static final class IMAGES
	{
		private IMAGES() {}
		public static final BufferedImage UI_ARROW_LEFT = Resources.images().get(PATHS.GUI + "arrow_left.png");
		public static final BufferedImage UI_ARROW_RIGHT = Resources.images().get(PATHS.GUI + "arrow_right.png");
	}
}
