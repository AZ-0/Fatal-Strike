package fr.az.fatalstrike.core.game;

import java.util.ArrayList;
import java.util.List;

import de.gurkenlabs.litiengine.Direction;

import fr.az.fatalstrike.core.game.field.Tile;
import fr.az.util.misc.tuples.Tuple2;
import fr.az.util.parsing.property.PropertyHolder;

public final class Player extends PropertyHolder
{
	private static final long serialVersionUID = -3181755536463791769L;

	private final List<Tuple2<Action, Direction>> actions = new ArrayList<>();
	private final PlayerEntity handle;
	private final Race race;

	private State state = State.IDLE;
	private Tile tile;

	public Player(Race race)
	{
		super(race.getPropertyBuilders());
		this.handle = new PlayerEntity(race.getName());
		this.race = race;
	}

	@Override
	public void updateProperties()
	{

	}

	public PlayerEntity getHandle() { return this.handle; }
	public State getState() { return this.state; }

	public static enum State
	{
		IDLE("idle"),
		WALKING("walk"),
		;

		private final String name;
		private State(String name) { this.name = name; }
		public String getFileName() { return this.name; }
	}

	public void play()
	{
		for (Tuple2<Action, Direction> tuple : this.actions)
		{
			tuple.a.apply(this);
			this.handle.setFacingDirection(tuple.b);
		}

		this.actions.clear();
	}

	public void setTile(Tile tile) { this.tile = tile; }

	public List<Tuple2<Action, Direction>> scheduledActions() { return this.actions; }
	public Race getRace() { return this.race; }
	public Tile getTile() { return this.tile; }
}
