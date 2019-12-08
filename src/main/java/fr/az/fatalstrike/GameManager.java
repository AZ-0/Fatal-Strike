package fr.az.fatalstrike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;

import fr.az.fatalstrike.core.game.DirectionInfo;
import fr.az.fatalstrike.core.game.Player;
import fr.az.fatalstrike.core.game.field.GameField;
import fr.az.fatalstrike.ui.screen.IngameScreen;

public final class GameManager
{
	private final List<Player> players = new ArrayList<>();
	private final List<Player> playersUnmodifiable = Collections.unmodifiableList(this.players);
	private GameField field;
	private GameState state;

	static
	{
		Game.screens().onScreenChanged(s ->
		{
			if (s == IngameScreen.screen())
				IngameScreen.screen().actionBar().getDirections()
				.forEach(d -> d.addItems(DirectionInfo::provideImage, Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT));
		});
	}

	GameManager()
	{
		Game.world().addLoadedListener(e ->
		{
			Game.world().camera().setFocus(e.getCenter());
			this.field = GameField.get(e.getMap().getTileLayers().get(0));
		});
	}

	public void setState(GameState state) { this.state = state; }

	public List<Player> getPlayers() { return this.playersUnmodifiable; }
	public GameField field() { return this.field; }
	public GameState state() { return this.state; }

	public static enum GameState
	{
		MENU,
		PLAYING,
		;
	}

	public static enum Map
	{
		TITLE("title"),
		FIGHT("fight"),
		;

		private final String name;

		private Map(String name)
		{
			this.name = name;
		}

		public void load() { Game.world().loadEnvironment(this.getName()); }
		public String getName() { return this.name; }
	}
}
