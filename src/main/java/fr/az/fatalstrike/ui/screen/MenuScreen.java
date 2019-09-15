package fr.az.fatalstrike.ui.screen;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.graphics.TextRenderer;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.input.Input;

import fr.az.fatalstrike.FatalStrike.MAPS;
import fr.az.fatalstrike.FatalStrike.GameManager;
import fr.az.fatalstrike.ui.KeyboardMenu;
import fr.az.fatalstrike.ui.KeyboardMenu.Theme;

public class MenuScreen extends Screen
{
	public static final String NAME = "MENU";
	public static final String AUTHORS = "By Justin \"A~Z\" Carel & Paul \"Heledryon\" Carel";
	
	private final static MenuScreen screen = new MenuScreen();
	
	private KeyboardMenu menu;
	
	public static MenuScreen screen() { return screen; }
	
	private MenuScreen()
	{
		super(NAME);
		Input.keyboard().onKeyPressed(KeyEvent.VK_ESCAPE, e -> { if (this.isVisible()) System.exit(0); });
	}

	private void startGame()
	{
		this.menu.setEnabled(false);
		Game.window().getRenderComponent().fadeOut(1500);
		
		Game.loop().perform(3500, () ->
		{
			Game.screens().display(IngameScreen.screen());
			Game.window().getRenderComponent().fadeIn(2000);
		});
	}
	
	@Override
	protected void initializeComponents()
	{
		super.initializeComponents();
		final double cX = Game.window().getResolution().getWidth()	/ 2f;
		final double cY = Game.window().getResolution().getHeight()	/ 2f;
		final double buttonWidth = 450;
		
		this.menu = new KeyboardMenu(cX - buttonWidth / 2, cY, buttonWidth, cY / 2, Theme.PITCH_BLACK, "Play", "Credits", "Exit");
		this.getComponents().add(menu);
		
		this.menu.onConfirmation(index ->
		{
			switch(index)
			{
				case 0: this.startGame();	break;
				case 1: break;
				case 2: System.exit(0);		break;
			}
		});
	}

	@Override
	public void prepare()
	{
		this.menu.setEnabled(true);
		this.menu.setFocus(0);
		super.prepare();
		
		Game.window().getRenderComponent().setBackground(Color.BLACK);
		Game.world().loadEnvironment(MAPS.TITLE);
		Game.world().camera().setFocus(Game.world().environment().getCenter());
	}
	
	@Override
	public void render(final Graphics2D g)
	{
		Game.world().environment().render(g);
		
		g.setFont(GameManager.FONT_GUI);
		g.setColor(Color.WHITE);
		
		final double sWidth = g.getFontMetrics().stringWidth(AUTHORS);
		TextRenderer.renderWithOutline(g, AUTHORS, Game.world().environment().getCenter().getX() - sWidth / 3, Game.window().getResolution().getHeight() * 19 / 20, Color.BLACK);
		
		super.render(g);
	}
}
