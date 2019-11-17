package fr.az.fatalstrike.core.game;

import java.util.ArrayList;
import java.util.List;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.annotation.CollisionInfo;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.annotation.MovementInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.physics.Collision;

import fr.az.fatalstrike.core.game.field.Tile;
import fr.az.util.misc.tuples.Tuple2;

@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 30)
@CollisionInfo(collisionBoxWidth = 32, collisionBoxHeight = 32, collisionType = Collision.DYNAMIC, collision = true)
public class Player extends Creature
{
	private final List<Tuple2<Action,Direction>> actions = new ArrayList<>();
	private final Race race;
	private Tile tile;

	Player(Race race)
	{
		super(race.getName());
		this.race = race;
	}

	public void play()
	{
		for (Tuple2<Action, Direction> tuple : this.scheduledActions())
		{
			tuple.a.apply(this);
			this.setFacingDirection(tuple.b);
		}

		this.scheduledActions().clear();
	}

	public void setTile(Tile tile) { this.tile = tile; }

	public List<Tuple2<Action,Direction>> scheduledActions() { return this.actions; }
	public Race getRace() { return this.race; }
	public Tile getTile() { return this.tile; }

	public static enum State
	{
		IDLE("idle"),
		WALKING("walk"),
		;

		private final String name;
		private State(String name) { this.name = name; }
		public String getFileName() { return this.name; }
	}
}
