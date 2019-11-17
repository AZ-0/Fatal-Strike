package fr.az.fatalstrike.ui.component;

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

public class SlideMenu<T> extends GuiComponent
{
	private static final int SLIDE_DELAY = 200;
	private static final int DEFAULT_MAX_RENDER_AMOUNT = 3;
	private static final int DEFAULT_SIZE = 128;
	private static final int MIN_SIZE = 2;

	public static BufferedImage ARROW_LEFT;
	public static BufferedImage ARROW_RIGHT;

	private int focus = 0;
	private long lastSlide = 0;
	private boolean mousePressingButtonLeft = false;
	private boolean mousePressingButtonRight = false;
	private boolean renderChanged = true;
	private Set<Consumer<SlideMenu<T>>> onRenderChanged = new HashSet<>();

	private final BufferedImage arrowLeft;
	private final BufferedImage arrowRight;
	protected ImageComponent buttonLeft;
	protected ImageComponent buttonRight;

	protected int maxRenderAmount = DEFAULT_MAX_RENDER_AMOUNT;
	protected int itemHeight;
	protected int itemWidth;

	protected final HashMap<T, ImageComponent> items;
	protected T selected;

	public SlideMenu(double x, double y) {
		this(x, y, DEFAULT_SIZE, DEFAULT_SIZE); }

	public SlideMenu(double x, double y, int itemWidth, int itemHeight) {
		this(x, y, itemWidth, itemHeight, null); }

	public SlideMenu(double x, double y, double width, double height) {
		this(x, y, width, height, null); }

	@SafeVarargs
	public SlideMenu(double x, double y, double width, double height, Function<T,Image> iconProvider, T... items) {
		this(x, y, (int) width / 5, (int) height, iconProvider, items); }

	@SafeVarargs
	public SlideMenu(double x, double y, int itemWidth, int itemHeight, Function<T,Image> iconProvider, T... items)
	{
		super(x, y, itemWidth * DEFAULT_MAX_RENDER_AMOUNT, itemHeight);
		this.items = new HashMap<>();
		this.arrowLeft = ARROW_LEFT;
		this.arrowRight = ARROW_RIGHT;

		this.itemWidth = itemWidth;
		this.itemHeight = itemHeight;

		for (T item : items) this.addItem(item, iconProvider.apply(item));
		if (items.length != 0) this.selected = items[0];

		this.buttonLeft.setImage(this.arrowLeft);
		this.buttonRight.setImage(this.arrowRight);
		this.buttonLeft.setDimension(this.arrowLeft.getWidth(), this.arrowRight.getHeight());
		this.buttonRight.setDimension(this.arrowRight.getWidth(), this.arrowRight.getHeight());
	}

	@Override
	public void initializeComponents()
	{
		this.buttonLeft = new ImageComponent(0, 0, 0, 0);
		this.buttonLeft.onMouseLeave(e -> this.mousePressingButtonLeft = false);
		this.buttonLeft.onMouseReleased(e -> this.mousePressingButtonLeft = false);
		this.buttonLeft.onMousePressed(e ->
		{
			this.slide(-1);
			this.mousePressingButtonLeft = true;
		});

		this.buttonRight = new ImageComponent(0, 0, 0, 0);
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
		while (this.focus < 0) this.focus += this.items.size();
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
				this.render(bWidth, items, rendered, i ->
				i < (this.focus + this.maxRenderAmount) % items.size() + this.maxRenderAmount - this.maxRenderAmount % items.size());

			this.buttonLeft.setWidth(bWidth);
			this.buttonLeft.setHeight(bWidth);
			this.buttonLeft.setImage(this.arrowLeft.getScaledInstance(bWidth, bWidth, Image.SCALE_SMOOTH));
			this.buttonLeft.setLocation(this.getX(), this.getY() + this.itemHeight / 2f - bWidth / 2f);

			this.buttonRight.setWidth(bWidth);
			this.buttonRight.setHeight(bWidth);
			this.buttonRight.setImage(this.arrowRight.getScaledInstance(bWidth, bWidth, Image.SCALE_SMOOTH));
			this.buttonRight.setLocation(bWidth + this.getX() + rendered.size() * this.itemWidth, this.getY() + this.itemHeight / 2f - bWidth / 2f);

			this.renderChanged = false;
			this.onRenderChanged.forEach(c -> c.accept(this));
		}

		if (this.mousePressingButtonLeft && SLIDE_DELAY < Game.time().since(this.lastSlide)) this.slide(-1);
		else if (this.mousePressingButtonRight && SLIDE_DELAY < Game.time().since(this.lastSlide)) this.slide(1);

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
				icon.setLocation(buttonWidth + this.getX() + this.itemWidth * (rendered.size() -1), this.getY());
				icon.setDimension(this.itemWidth, this.itemHeight);
				icon.setVisible(true);
			} else
				icon.setVisible(false);
		}
	}

	public void adjustWidthToHeight() { this.setItemWidth(this.itemHeight); }
	public void adjustHeightToWidth() { this.setItemHeight(this.itemWidth); }

	public void clearItems()
	{
		this.getComponents().removeAll(this.items.values());
		this.items.clear();
	}

	public void removeItems(Collection<T> items) { items.forEach(this::removeItem); }
	public void removeItem(T item) { this.getComponents().remove(this.items.remove(item)); this.renderChanged = true; }

	@SafeVarargs
	public final void addItems(Function<T,Image> iconProvider, T... items) { for (T item : items) this.addItem(item, iconProvider.apply(item)); }
	public void addItems(Function<T, Image> iconProvider, Collection<T> items) {
		items.forEach(t -> this.addItem(t, iconProvider.apply(t))); }

	public void addItem(T item, Image icon)
	{
		if (this.items.containsKey(item))
			return;

		ImageComponent img = new ImageComponent(this.getX(), this.getY(), this.itemWidth, this.itemHeight, icon);
		img.setImageScaleMode(ImageScaleMode.STRETCH);
		img.setVisible(false);
		img.getAppearance().setTransparentBackground(true);

		img.onMouseReleased(e -> this.items.forEach((act, comp) ->
		{
			if (comp == img)
			{
				img.setSelected(true);
				img.setEnabled(false);
				this.selected = act;
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

	public void onRenderChanged(Consumer<SlideMenu<T>> action) { this.onRenderChanged.add(action); }

	@Override public void setLocation(Point2D location) { super.setLocation(location); this.renderChanged = true; }
	@Override public void setLocation(double x, double y) { super.setLocation(x, y); this.renderChanged = true; }
	@Override public void setX(double x) { super.setX(x); this.renderChanged = true; }
	@Override public void setY(double y) { super.setY(y); this.renderChanged = true; }

	@Override public void setDimension(double width, double height) { this.setHeight(height); this.setWidth(width); }
	@Override public void setHeight(double height)
	{
		if (height < MIN_SIZE) height = MIN_SIZE;
		super.setHeight(height);
		this.itemHeight = (int) height;
	}

	@Override
	public void setWidth(double width)
	{
		int bWidth = this.getButtonWidth();
		int renderAmount = Math.min(this.items == null ? this.maxRenderAmount : this.items.size(), this.maxRenderAmount);

		if (width < bWidth * (renderAmount + 2)) width =  bWidth * (renderAmount + 2);
		super.setWidth(width);

		this.internalSetItemWidth(renderAmount < 1 ? MIN_SIZE : (int) ((width - bWidth * 2) / renderAmount));
	}

	public void setItemDimension(int width, int height) { this.setItemWidth(width); this.setItemHeight(height); }
	public void setItemHeight(int height) { this.setHeight(height); }
	public void setItemWidth(int width)
	{
		int bWidth = this.getButtonWidth();
		int renderAmount = Math.min(this.items == null ? this.maxRenderAmount : this.items.size(), this.maxRenderAmount);
		if (width < bWidth) width = bWidth;

		super.setWidth(width * renderAmount + 2 * bWidth);
		this.internalSetItemWidth(width);
	}

	protected void internalSetItemWidth(int width)
	{
		if(width < MIN_SIZE) width = MIN_SIZE;
		this.itemWidth = width;
		this.renderChanged = true;
	}

	public void setMaxItemRenderingAmount(int amount) { this.maxRenderAmount = amount < 0 ? 0 : amount; }

	public boolean hasRenderChanged() { return this.renderChanged; }

	public int getFocus() { return this.focus; }
	public T getSelectedItem() { return this.selected; }
	public Set<T> getItems() { return this.items.keySet(); }

	public Dimension getItemDimension() { return new Dimension(this.itemWidth, this.itemHeight); }
	public int getItemWidth() { return this.itemWidth; }
	public int getItemHeight() { return this.itemHeight; }

	public int getMaxItemRenderingAmount() { return this.maxRenderAmount; }
	public int getButtonWidth() { return this.itemHeight / 2; }
}
