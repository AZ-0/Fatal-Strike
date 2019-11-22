package fr.az.fatalstrike.core.game;

import de.gurkenlabs.litiengine.annotation.CollisionInfo;
import de.gurkenlabs.litiengine.annotation.EntityInfo;
import de.gurkenlabs.litiengine.annotation.MovementInfo;
import de.gurkenlabs.litiengine.entities.Creature;
import de.gurkenlabs.litiengine.physics.Collision;

@EntityInfo(width = 32, height = 32)
@MovementInfo(velocity = 30)
@CollisionInfo(collisionBoxWidth = 32, collisionBoxHeight = 32, collisionType = Collision.DYNAMIC, collision = true)
public class PlayerEntity extends Creature
{
	PlayerEntity(String name)
	{
		super(name);
	}
}
