package fr.az.fatalstrike.core.game;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.graphics.Spritesheet;
import de.gurkenlabs.litiengine.resources.Resources;

import fr.az.fatalstrike.FatalStrike;
import fr.az.fatalstrike.core.game.Player.State;
import fr.az.util.parsing.property.PropertyBuilder;
import fr.az.util.parsing.string.Parser;

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

	public static void init()
	{
		PropertyBuilder<Integer> health = new PropertyBuilder<>("health", 5, Integer.class, Parser.POSITIVE_INTEGER);


		List<PropertyBuilder<?>> basicBuilders = Arrays.asList(health);
		new Race(Race.loadSelectionImage(ELF), ELF, basicBuilders);
		new Race(Race.loadSelectionImage(OGRE), OGRE, basicBuilders);
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
	 * @return an immutable Map containing all Race instances
	 */
	public static Map<String, Race> all() { return Collections.unmodifiableMap(RACES); }

	private final List<PropertyBuilder<?>> propertyBuilders;
	private final BufferedImage selectionIcon;
	private final String name, sprite;
	private final List<Action> actions;

	private Race(BufferedImage selectionIcon, String name, List<PropertyBuilder<?>> propertyBuilders, Action... actions)
	{
		this.selectionIcon = selectionIcon;
		this.name = name;
		this.sprite = name.toLowerCase();
		this.propertyBuilders = Collections.unmodifiableList(propertyBuilders);
		this.actions = Arrays.asList(actions);

		RACES.put(name, this);
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
	public List<PropertyBuilder<?>> getPropertyBuilders() { return this.propertyBuilders; }
	public List<Action> getActions() { return this.actions; }
}
