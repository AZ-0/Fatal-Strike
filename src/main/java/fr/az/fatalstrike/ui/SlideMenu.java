package fr.az.fatalstrike.ui;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.ImageScaleMode;
import de.gurkenlabs.litiengine.resources.Resources;

public class SlideMenu<T> extends GuiComponent
{
	private static final BufferedImage ARROW_LEFT = Resources.images().get("ui/arrow_left.png");
	private static final BufferedImage ARROW_RIGHT = Resources.images().get("ui/arrow_right.png");
	private static final int SLIDE_DELAY = 200;
	private static final int DEFAULT_SIZE = 112;

	private int focus = 0;
	private long lastSlide = 0;
	private boolean mousePressingButtonLeft = false;
	private boolean mousePressingButtonRight = false;
	private boolean renderChanged = true;
	private Set<Consumer<SlideMenu<T>>> onRenderChanged = new HashSet<>();
	
	protected ImageComponent buttonLeft;
	protected ImageComponent buttonRight;

	protected int maxRenderAmount = 3;
	protected int itemHeight;
	protected int itemWidth;
	
	protected HashMap<T, ImageComponent> items;
	protected T selected;

	public SlideMenu(double x, double y) {
		this(x, y, DEFAULT_SIZE * 5, DEFAULT_SIZE, DEFAULT_SIZE, DEFAULT_SIZE); }
	
	public SlideMenu(double x, double y, double width, double height, int itemWidth, int itemHeight) {
		this(x, y, width, height, itemWidth, itemHeight, null); }

	@SafeVarargs
	public SlideMenu(double x, double y, double width, double height, Function<T,Image> iconProvider, T... items) {
		this(x, y, width, height, DEFAULT_SIZE, DEFAULT_SIZE, iconProvider, items); }

	@SafeVarargs
	public SlideMenu(double x, double y, double width, double height, int itemWidth, int itemHeight, Function<T,Image> iconProvider, T... items)
	{
		super(x, y, width, height);
		this.itemWidth = itemWidth;
		this.itemHeight = itemHeight;
		
		for (T item : items) this.addItem(item, iconProvider.apply(item));
		if (items.length > 0) selected = items[0];
	}
	
	{
		this.setItemHeight(itemHeight);
		this.setItemWidth(itemWidth);
	}
	
	@Override
	protected void initializeComponents()
	{
		this.items = new HashMap<>();
		super.initializeComponents();
		
		this.buttonLeft = new ImageComponent(0, 0, ARROW_LEFT.getWidth(), ARROW_LEFT.getHeight(), ARROW_LEFT);
		this.buttonLeft.onMouseLeave(e -> this.mousePressingButtonLeft = false);
		this.buttonLeft.onMouseReleased(e -> this.mousePressingButtonLeft = false);
		this.buttonLeft.onMousePressed(e ->
		{
			this.slide(-1);
			this.mousePressingButtonLeft = true;
		});
		
		this.buttonRight = new ImageComponent(0, 0, ARROW_RIGHT.getWidth(), ARROW_RIGHT.getHeight(), ARROW_RIGHT);
		this.buttonRight.onMouseLeave(e -> this.mousePressingButtonRight = false);
		this.buttonRight.onMouseReleased(e -> this.mousePressingButtonRight = false);
		this.buttonRight.onMousePressed(e ->
		{
			this.slide(1);
			this.mousePressingButtonRight = true;
		});
		
		for (ImageComponent button : new ImageComponent[] { this.buttonLeft, this.buttonRight })
		{
			button.setVisible(true);
			button.setImageAlign(Align.CENTER);
			button.setImageValign(Valign.MIDDLE);
			this.getComponents().add(button);
		}
	}
	
	public void slide(int amount)
	{
		if (this.items.isEmpty()) return;
		
		this.focus += amount;
		while (focus < 0) focus += this.items.size();
		this.focus %= this.items.size();
		this.lastSlide = Game.time().now();
	}
	
	@Override
	public void render(final Graphics2D g)
	{
		if (this.isSuspended() || !this.isVisible())
			return;

		if (this.renderChanged)
		{
			final ArrayList<T> items = new ArrayList<>(this.items.keySet()), rendered = new ArrayList<>();
			int bWidth = this.getButtonWidth();
		
			this.render(bWidth, items, rendered, i -> i >= this.focus && i < this.focus + this.maxRenderAmount);
			
			if (this.focus + this.maxRenderAmount > this.items.size())
				render(bWidth, items, rendered, i ->
				i < ((focus + maxRenderAmount) % items.size()) + maxRenderAmount - (maxRenderAmount % items.size()));

			this.buttonLeft.setWidth(bWidth);
			this.buttonLeft.setHeight(bWidth);
			this.buttonLeft.setImage(ARROW_LEFT.getScaledInstance(bWidth, bWidth, Image.SCALE_SMOOTH));
			this.buttonLeft.setLocation(this.getX(), this.getY() + (this.itemHeight / 2f) - (bWidth / 2f));
		
			this.buttonRight.setWidth(bWidth);
			this.buttonRight.setHeight(bWidth);
			this.buttonRight.setImage(ARROW_RIGHT.getScaledInstance(bWidth, bWidth, Image.SCALE_SMOOTH));
			this.buttonRight.setLocation(bWidth + this.getX() + rendered.size() * this.itemWidth, this.getY() + (this.itemHeight / 2f) - (bWidth / 2f));
			
			this.onRenderChanged.forEach(c -> c.accept(this));
		}
		
		if (this.mousePressingButtonLeft && SLIDE_DELAY < Game.time().since(this.lastSlide)) this.slide(-1);
		if (this.mousePressingButtonRight && SLIDE_DELAY < Game.time().since(this.lastSlide)) this.slide(1);
		
		super.render(g);
	}
	
	private void render(int buttonWidth, ArrayList<T> items, ArrayList<T> rendered, Predicate<Integer> condition)
	{
		for (int i = 0; i < items.size(); i++)
		{
			if (rendered.contains(items.get(i))) continue;
			ImageComponent icon = this.items.get(items.get(i));

			if (condition.test(i))
			{
				rendered.add(items.get(i));
				icon.setLocation(buttonWidth + this.getX() + itemWidth * (rendered.size() -1), this.getY());
				icon.setDimension(this.itemWidth, this.itemHeight);
				icon.setVisible(true);
			} else
				icon.setVisible(false);
		}
	}
	
	public void adjustWidthToHeight() { this.setItemWidth(this.itemHeight); }
	public void adjustHeightToWidth() { this.setItemHeight(this.itemWidth); }
	
	public void removeItems(Collection<T> items) { items.forEach(this::removeItem); }
	public void removeItem(T item) { this.getItems().remove(this.items.remove(item)); this.setWidth(this.getWidth()); }
	
	public void addItems(Collection<T> items, Function<T,Image> iconProvider) {
		items.forEach(t -> this.addItem(t, iconProvider.apply(t))); }
	
	public void addItem(T item, Image icon)
	{
		if (items.containsKey(item))
			return;
		
		ImageComponent img = new ImageComponent(this.getX(), this.getY(), itemWidth, itemHeight, icon);
		img.setImageScaleMode(ImageScaleMode.STRETCH);
		img.setVisible(false);
		img.getAppearance().setTransparentBackground(true);
		
		img.onMouseReleased(e -> this.items.forEach((act, comp) ->
		{
			if (comp == img)
			{
				img.setSelected(true);
				img.setEnabled(false);
				selected = act;
			} else
			{
				comp.setSelected(false);
				comp.setEnabled(true);
			}
		}));

		this.items.put(item, img);
		this.getComponents().add(img);
		this.setWidth(this.getWidth());
	}
	
	@Override public void setLocation(Point2D location) { super.setLocation(location); this.renderChanged = true; }
	@Override public void setLocation(double x, double y) { super.setLocation(x, y); this.renderChanged = true; }
	@Override public void setX(double x) { super.setX(x); this.renderChanged = true; }
	@Override public void setY(double y) { super.setY(y); this.renderChanged = true; }
	
	@Override public void setDimension(double width, double height) { this.setHeight(height); this.setWidth(width); }
	@Override public void setHeight(double height) { this.setItemHeight((int) height); super.setHeight(height); }
	
	@Override
	public void setWidth(double width)
	{
		int bWidth = this.getButtonWidth();
		int renderAmount = Math.min(items == null ? maxRenderAmount : items.size(), maxRenderAmount);
		
		if (width < bWidth * (renderAmount + 2)) width =  bWidth * (renderAmount + 2);
		super.setWidth(width);
		
		this.internalSetItemWidth(renderAmount < 1 ? 0 : (int) ((width - bWidth * 2) / renderAmount));
	}
	
	public void setMaxItemRenderingAmount(int amount) { this.maxRenderAmount = amount; }
	public void setItemDimension(int width, int height) { this.setItemWidth(width); this.setItemHeight(height); }
	public void setItemWidth(int width)
	{
		int bWidth = this.getButtonWidth();
		int renderAmount = Math.min(items == null ? maxRenderAmount : items.size(), maxRenderAmount);
		if (width < bWidth) width = bWidth;
		
		super.setWidth(width * renderAmount + 2 * bWidth);
		this.internalSetItemWidth(width);
	}
	
	public void setItemHeight(int height)
	{
		super.setHeight(height);
		this.itemHeight = height;
		this.renderChanged = true;
	}
	
	protected void internalSetItemWidth(int width) { this.itemWidth = width; this.renderChanged = true; }
	
	public boolean hasRenderChanged() { return this.renderChanged; }
	
	public int getFocus() { return focus; }
	public T getSelectedItem() { return selected; }
	public Set<T> getItems() { return items.keySet(); }
	
	public Dimension getItemDimension() { return new Dimension(this.itemWidth, this.itemHeight); }
	public int getItemWidth() { return this.itemWidth; }
	public int getItemHeight() { return this.itemHeight; }
	
	public int getMaxItemRenderingAmount() { return this.maxRenderAmount; }
	public int getButtonWidth() { return this.itemHeight / 2; }
}
