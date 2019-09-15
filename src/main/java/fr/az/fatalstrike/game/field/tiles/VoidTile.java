package fr.az.fatalstrike.game.field.tiles;

import fr.az.fatalstrike.game.field.Tile;
import fr.az.fatalstrike.game.field.TilePosition;

public class VoidTile extends Tile
{
	public VoidTile(TilePosition pos)
	{
		super(false, pos);
	}
}
