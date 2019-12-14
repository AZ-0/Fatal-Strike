package fr.az.fatalstrike.ui.screen;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.screens.Screen;
import de.gurkenlabs.litiengine.input.Input;

import fr.az.fatalstrike.GameManager.Map;
import fr.az.fatalstrike.ui.component.KeyboardMenu;
import fr.az.fatalstrike.ui.component.KeyboardMenu.Theme;

public class MenuScreen extends Screen
{
	public static final String NAME = "screen.menu";

	private final static MenuScreen screen = new MenuScreen(IngameScreen.screen());

	public static MenuScreen screen() { return screen; }

	private GameScreen forward;
	private KeyboardMenu menu;

	private MenuScreen(GameScreen forward)
	{
		super(NAME);
		this.forward = forward;
		Input.keyboard().onKeyPressed(KeyEvent.VK_ESCAPE, e -> { if (this.isVisible()) System.exit(0); });
	}

	private void startGame()
	{
		this.menu.setEnabled(false);
		this.forward.display();
	}

	@Override
	protected void initializeComponents()
	{
		super.initializeComponents();
		final double cX = Game.window().getResolution().getWidth()	/ 2f;
		final double cY = Game.window().getResolution().getHeight()	/ 2f;
		final double buttonWidth = 450;

		this.menu = new KeyboardMenu(cX - buttonWidth / 2, cY, buttonWidth, cY / 2, Theme.PITCH_BLACK, "Play", "Credits", "Exit");
		this.getComponents().add(this.menu);

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

		Map.TITLE.load();
		Game.world().camera().setFocus(Game.world().environment().getCenter());
	}

	@Override
	public void render(final Graphics2D g)
	{
//		Game.world().environment().render(g);

//		g.setFont(UIManager.Fonts.GUI.getFont());
//		g.setColor(Color.WHITE);

		super.render(g);
	}

	public GameScreen getForwardScreen() { return this.forward; }
	public void setForwardScreen(GameScreen screen) { this.forward = screen; }
}
