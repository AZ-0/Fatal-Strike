package fr.az.fatalstrike.core.game.field.tiles;

import fr.az.fatalstrike.core.game.field.Tile;
import fr.az.fatalstrike.core.game.field.TilePosition;

public class VoidTile extends Tile
{
	public VoidTile(TilePosition pos)
	{
		super(false, pos);
	}
}
