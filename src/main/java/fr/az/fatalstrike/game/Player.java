package fr.az.fatalstrike.game;

import de.gurkenlabs.litiengine.annotation.CollisionInfo;
import de.gurkenlabs.litiengine.annotation.CombatInfo;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.annotation.MovementInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import fr.az.fatalstrike.game.field.Tile;

@EntityInfo(width = 11, height = 20)
@MovementInfo(velocity = 30)
@CollisionInfo(collisionBoxWidth = 5, collisionBoxHeight = 8, collision = true)
@CombatInfo(hitpoints = 5)
public class Player extends Creature
{
	private Character character;
	private Tile tile;
	
	public Player (Character character)
	{
		this.character = character;
	}

	public void setCharacter(Character character) { this.character = character; }
	public void setTile(Tile tile) { this.tile = tile; }
	
	public Character getCharacter() { return character; }
	public Tile getTile() { return tile; }
}
