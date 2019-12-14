package fr.az.fatalstrike.ui.screen;

import de.gurkenlabs.litiengine.Game;

public class GameScreen extends de.gurkenlabs.litiengine.gui.screens.GameScreen
{
	public GameScreen() {}
	protected GameScreen(String name) { super(name); }

	public void display() { Game.screens().display(this); }
}
