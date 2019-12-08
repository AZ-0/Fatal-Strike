package fr.az.fatalstrike.ui.screen;

import java.awt.Dimension;
import java.awt.geom.Point2D;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;

import fr.az.fatalstrike.core.game.Race;
import fr.az.fatalstrike.ui.component.SlideMenu;

public class SelectionScreen extends GameScreen
{
	private static final SelectionScreen screen = new SelectionScreen();
	public static final String NAME = "screen.selection";

	public static SelectionScreen screen() { return screen; }

	private SlideMenu<Race> selection;

	private SelectionScreen() { super(NAME); }

	@Override
	protected void initializeComponents()
	{
		super.initializeComponents();
		Dimension iconSize = Race.getSelectionIconSize();

		this.selection = new SlideMenu<>(0, 0, iconSize.width, iconSize.height);
		this.selection.addItems(Race::getSelectionIcon, Race.all().values());
		this.getComponents().add(this.selection);
	}

	@Override
	public void prepare()
	{
		super.prepare();
		Point2D center = Game.window().getCenter();
		this.selection.setLocation(center.getX() - this.selection.getWidth() / 2f, center.getY() - this.selection.getHeight() / 2f);
	}
}
