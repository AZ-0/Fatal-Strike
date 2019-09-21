package fr.az.fatalstrike.game;

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

@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 30)
@CollisionInfo(collisionBoxWidth = 32, collisionBoxHeight = 32, collisionType = Collision.DYNAMIC, collision = true)
@CombatInfo(hitpoints = 5)
public class Player extends Creature
{
	private final Character character;
	private Tile tile;
	
	public Player(Character character)
	{
		super(character.name);
		this.character = character;
	}

	public void setTile(Tile tile) { this.tile = tile; }
	
	public Character getCharacter() { return character; }
	public Tile getTile() { return tile; }
	
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
		
		private final String name;
		private final String sprite;
		private final List<Action> actions;
		
		private Character(String name, Action... actions)
		{
			this.name = name;
			this.sprite = name.toLowerCase();
			this.actions = Arrays.asList(actions);
		}
		
		public Spritesheet getSprite(Direction dir, State state)
		{
			String sprite = this.sprite +'-'+ state.name +'-'+ dir.name().toLowerCase() +".png";
			return Resources.spritesheets().contains(sprite) ? Resources.spritesheets().get(sprite)
					: Resources.spritesheets().get(this.sprite +"-up.png");
		}
		
		public String getName() { return this.name; }
		public String getSpriteName() { return this.sprite; }
		public List<Action> getActions() { return actions; }
	}
}
