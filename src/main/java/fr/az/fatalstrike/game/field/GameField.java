package fr.az.fatalstrike.game.field;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import de.gurkenlabs.litiengine.environment.tilemap.ITileLayer;

public class GameField implements Serializable
{
	private static final long serialVersionUID = 5133457378991811549L;
	private static final HashMap<ITileLayer, GameField> instances = new HashMap<>();
	
	public static List<GameField> getInstances() { return new ArrayList<>(instances.values()); }
	
	public static GameField get(IMap map, int layer_id)
	{
		Optional<ITileLayer> layer = map.getTileLayers().stream().filter(l -> l.getId() == layer_id).findAny();
		if (layer.isPresent())
			return GameField.get(layer.get());
		else
			throw new NoSuchElementException("There is no ITileLayer of id "+ layer_id +" in the map "+ map.getName());
	}
	
	public static GameField get(ITileLayer layer)
	{
		if (instances.containsKey(layer)) return instances.get(layer);
		return new GameField(layer.getName(), layer);
	}
	
	private final ArrayList<List<Tile>> field = new ArrayList<>();
	private final String name;
	private final ITileLayer layer;
	
	private GameField(ITileLayer layer) { this(layer.getName(), layer); }
	private GameField(String name, ITileLayer layer)
	{
		this.name = name;
		this.layer = layer;
		
		instances.put(this.layer, this);
		this.reload();
	}
	
	public void reload()
	{
		int x = 0, y = 0;
		Dimension size = layer.getSizeInTiles();
		
		this.field.clear();
		while (x < size.getWidth())
		{
			ArrayList<Tile> column = new ArrayList<>();
			field.add(column);
			
			while (y < size.getHeight())
			{
				column.add(Tile.of(layer.getTile(x, y), new TilePosition(this, x, y)));
				y++;
			}

			y = 0; x++;
		}
	}
	
	public void setTile(Tile tile, int x, int y)
	{
		field.get(y).set(x, tile);
		tile.setPosition(new TilePosition(this, x, y));
	}
	
	public Tile getTile(int x, int y)
	{
		Dimension size = this.getSize();
		if (y < 0 || y >= size.getWidth() || x < 0 || x >= size.getHeight()) return null;
		return field.get(y).get(x);
	}
	
	public Dimension getSize() { return new Dimension(field.size(), field.get(0).size()); }
	
	public ITileLayer getLayer() { return layer; }
	public IMap getMap() { return layer.getMap(); }
	public int getLayerId() { return layer.getId(); }
	
	public List<List<Tile>> getField() { return field; }
	public String getName() { return name; }
	
	@Override public String toString() { return field.stream().map(List::toString).reduce("", (a,b) -> a +'\n'+ b); }
}
