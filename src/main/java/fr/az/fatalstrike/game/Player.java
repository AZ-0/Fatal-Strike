package fr.az.fatalstrike.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.annotation.CollisionInfo;
import de.gurkenlabs.litiengine.annotation.CombatInfo;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.annotation.MovementInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.physics.Collision;
import de.gurkenlabs.litiengine.resources.Resources;

import fr.az.fatalstrike.game.field.Tile;
import fr.az.fatalstrike.util.Tuple2;

@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 30)
@CollisionInfo(collisionBoxWidth = 32, collisionBoxHeight = 32, collisionType = Collision.DYNAMIC, collision = true)
@CombatInfo(hitpoints = 5)
public class Player extends Creature
{
	private final List<Tuple2<Action,Direction>> actions = new ArrayList<>();
	private final Character character;
	private Tile tile;

	public Player(Character character)
	{
		super(character.name);
		this.character = character;
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
	public Character getCharacter() { return this.character; }
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

	public static enum Character
	{
		ELF("Elf", new Action(Resources.images().get("old/elf.png")), new Action(Resources.images().get("old/ogre.png")), new Action(Resources.images().get("old/arrow.png")), new Action(Resources.images().get("ui/validation.png"))),
		OGRE("Ogre"),
		;

		public static final int SPRITE_WIDTH = 32;
		public static final int SPRITE_HEIGHT = 32;

		private final String name;
		private final String sprite;
		private final List<Action> actions;

		private Character(String name, Action... actions)
		{
			this.name = name;
			this.sprite = name.toLowerCase();
			this.actions = Arrays.asList(actions);
		}

		public Spritesheet getSprite(Direction dir, State state) { return this.getSprite(dir, state, SPRITE_WIDTH, SPRITE_HEIGHT); }
		public Spritesheet getSprite(Direction dir, State state, int spriteWidth, int spriteHeight)
		{
			String sprite = this.sprite +'-'+ state.name +'-'+ dir.name().toLowerCase() +".png";

			if (Resources.spritesheets().contains(sprite))
				return Resources.spritesheets().get(sprite);

			try { return Resources.spritesheets().load(sprite, spriteWidth, spriteHeight); }
			catch (Exception e) { return Resources.spritesheets().get(this.sprite +"-up.png"); }
		}

		public String getName() { return this.name; }
		public String getSpriteName() { return this.sprite; }
		public List<Action> getActions() { return this.actions; }
	}
}
