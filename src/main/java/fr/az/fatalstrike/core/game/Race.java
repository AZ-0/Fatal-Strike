package fr.az.fatalstrike.core.game;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.resources.Resources;

import fr.az.fatalstrike.FatalStrike;
import fr.az.fatalstrike.core.game.Player.State;

/**
 * By convention, this is a singleton class. Each subclass of Race should have exactly one instance, as they are used as static player
 * templates
 */
public final class Race
{
	public static final int DISPLAYABLE_SELECTION_AMOUNT = 5;
	public static final int SPRITE_WIDTH = 32;
	public static final int SPRITE_HEIGHT = 32;

	public static final String ELF = "Elf";
	public static final String OGRE = "Ogre";

	private static final HashMap<String, Race> RACES = new HashMap<>();
	private static final HashMap<String, List<RacePropertyBuilder<?>>> PROPERTIES = new HashMap<>();

	public static void init()
	{
		new Race(Race.loadSelectionImage(ELF), ELF);
		new Race(Race.loadSelectionImage(OGRE), OGRE);
	}

	private static BufferedImage loadSelectionImage(String NAME) {
		return Resources.images().get("ui/selection/"+ NAME.toLowerCase() +".png"); }

	public static Dimension getSelectionIconSize()
	{
		Dimension size = FatalStrike.getEffectiveWindowSize();
		size.setSize(size.getWidth() / 6d, size.getHeight() / 2d);
		return size;
	}

	/**
	 * Usage Example:
	 * <code><blockquote>
	 * Race elf = Race.all().get(Race.ELF);
	 * </blockquote></code>
	 * @return an immutable Mapp containing all Race instances
	 */
	public static Map<String, Race> all() { return Collections.unmodifiableMap(RACES); }

	private final BufferedImage selectionIcon;
	private final String name, sprite;
	private final List<Action> actions;

	private Race(BufferedImage selectionIcon, String name, Action... actions)
	{
		this.selectionIcon = selectionIcon;
		this.name = name;
		this.sprite = name.toLowerCase();
		this.actions = Arrays.asList(actions);

		RACES.put(name, this);
	}

	public Player newPlayer()
	{
		Player player = new Player(this);

		return player;
	}

	public Spritesheet getSprite(Direction dir, State state) { return this.getSprite(dir, state, SPRITE_WIDTH, SPRITE_HEIGHT); }
	public Spritesheet getSprite(Direction dir, State state, int spriteWidth, int spriteHeight)
	{
		String sprite = this.sprite +'-'+ state.getFileName() +'-'+ dir.name().toLowerCase() +".png";

		if (Resources.spritesheets().contains(sprite))
			return Resources.spritesheets().get(sprite);

		try { return Resources.spritesheets().load(sprite, spriteWidth, spriteHeight); }
		catch (Exception e) { return Resources.spritesheets().get(this.sprite +"-up.png"); }
	}

	public BufferedImage getSelectionIcon() { return this.selectionIcon; }
	public String getName() { return this.name; }
	public String getSpriteName() { return this.sprite; }
	public List<Action> getActions() { return this.actions; }

	final static class RacePropertyBuilder<T>
	{
		private final String id;
		private final BiConsumer<Player, T> action;

		public RacePropertyBuilder(String id, T value, BiConsumer<Player, T> action)
		{
			this.id = id;
			this.action = action;
		}

		public RaceProperty<T> build(T value)
		{
			return new RaceProperty<T>(value) {
				@Override public void apply(Player p) { RacePropertyBuilder.this.action.accept(p, this.value); } };
		}

		public String getId() { return this.id; }
		public BiConsumer<Player, T> getAction() { return this.action; }
	}

	public static class RaceProperty<T>
	{
		T value;
		private RaceProperty(T value) { this.value = value; }

		public void apply(Player p) {}
		public T getValue() { return this.value; }
	}
}
