package fr.az.fatalstrike.core.game.field;

import java.awt.geom.Point2D;

import de.gurkenlabs.litiengine.Direction;

public class TilePosition
{
	private int x, y;
	private GameField gf;
	
	private Direction dir = Direction.UNDEFINED;
	
	public TilePosition(GameField gf, int x, int y)
	{
		this.x = x;
		this.y = y;
		this.gf = gf;
	}
	
	public TilePosition(GameField gf, int x, int y, Direction dir)
	{
		this(gf, x, y);
		this.dir = dir;
	}
	
	public TilePosition offset(int x, int y) { return new TilePosition(this.gf, this.x + x, this.y + y); }
	
	public Tile getTile(Direction... dirs)
	{
		int x = this.x, y = this.y;
		
		for (Direction o : dirs)
		{
			switch (o)
			{
				case UP:	y++; break;
				case DOWN:	y--; break;
				case RIGHT: x++; break;
				case LEFT:	x--; break;
				default: break;
			}
		}
		
		return gf.getTile(x, y);
	}
	
	public Point2D toPoint2D() { return null; }
	
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public void setGameField(GameField gf) { this.gf = gf; }
	public void setDirection(Direction dir) { this.dir = dir; }

	public boolean isValid() { return gf.getTile(x, y).getPosition().equals(this); }
	public int getX() { return x; }
	public int getY() { return y; }
	public GameField getGameField() { return gf; }
	public Direction getDirection() { return dir; }
	
	public boolean equals(TilePosition pos) { return pos != null && pos.gf == gf && pos.x == x && pos.y == y; }
}
