package fr.az.fatalstrike.game.field;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import de.gurkenlabs.litiengine.entities.CombatEntity;
import de.gurkenlabs.litiengine.entities.IEntity;
import de.gurkenlabs.litiengine.environment.tilemap.ITile;
import fr.az.fatalstrike.game.field.tiles.StartTile;
import fr.az.fatalstrike.game.field.tiles.VoidTile;

public abstract class Tile
{
	private final static ArrayList<Tile> instances = new ArrayList<>();

	public static List<Tile> getInstances() { return Collections.unmodifiableList(instances); }
	
	public static Tile of(ITile tile, TilePosition position)
	{
		return tile.getEnumValue("tile_type", TileType.class, TileType.VOID).getTile(position);
	}
	
	private boolean reachable;
	private TilePosition pos;
	private TileType type;
	
	private Set<IEntity> occupants = new HashSet<>();
	
	{
		instances.add(this);
	}
	
	protected Tile(boolean reachable, TilePosition pos)
	{
		this.reachable = reachable;
		this.pos = pos;
	}
	
	public de.gurkenlabs.litiengine.environment.tilemap.xml.Tile toLitiTile()
	{
		de.gurkenlabs.litiengine.environment.tilemap.xml.Tile tile = new de.gurkenlabs.litiengine.environment.tilemap.xml.Tile();
		tile.setValue("tile_type", this.type);
		tile.getTilesetEntry();
		return tile;
	}
	
	public void setPosition(TilePosition pos) { this.pos = pos; }
	public boolean addOccupant(IEntity occupant) { return this.occupants.add(occupant); }
	public boolean removeOccupant(IEntity occupant) { return this.occupants.remove(occupant); }
	
	public boolean isReachable() { return reachable; }
	public TilePosition getPosition() { return pos; }
	public Set<IEntity> getOccupants() { return occupants; }
	public Set<CombatEntity> getLivingOccupants() { return occupants.stream().filter(e -> e instanceof CombatEntity).map(e -> (CombatEntity) e).collect(Collectors.toSet()); }
	
	public static enum TileType
	{
		VOID(VoidTile::new),
		START(StartTile::new),
		;
		
		private final Function<TilePosition, ? extends Tile> getter;
		private TileType(Function<TilePosition, ? extends Tile> getter) { this.getter = getter; }
		public Tile getTile(TilePosition position) { return getter.apply(position); }
	}
	
	protected class LitiTile extends de.gurkenlabs.litiengine.environment.tilemap.xml.Tile
	{
		public LitiTile() {}
		public LitiTile(int gidBitmask) { super(gidBitmask); }
	}
}
