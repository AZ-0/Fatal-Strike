package fr.az.fatalstrike.ui.component;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.gui.Menu;
import de.gurkenlabs.litiengine.input.Input;

import fr.az.fatalstrike.UIManager;

public class KeyboardMenu extends Menu
{
	public static final Color ROMAN_RED = new Color(200, 16, 16);
	public static final Color BUTTON_YELLOW = new Color(255, 255, 0);
	public static final Color BUTTON_BLACK = new Color(0, 0, 0, 200);
	public static final int DELAY = 180;

	private final CopyOnWriteArrayList<Consumer<Integer>> confirmations = new CopyOnWriteArrayList<>();
	private final Theme theme;
	private long lastInput = 0;
	private int focus = -1;

	public KeyboardMenu(double x, double y, double width, double height, Theme theme, String... items)
	{
		super(x, y, width, height, items);
		this.theme = theme;

		Input.keyboard().onKeyReleased(e ->
		{
			if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_E)
			{
				if (this.isLocked())
					return;

				this.confirmed();
				this.lastInput = Game.time().now();
			}
		});

		Input.keyboard().onKeyPressed(KeyEvent.VK_UP, e ->
		{
			if (this.isLocked()) return;
			this.changeFocus(-1);
		});

		Input.keyboard().onKeyPressed(KeyEvent.VK_DOWN, e ->
		{
			if (this.isLocked()) return;
			this.changeFocus(1);
		});
	}

	public void changeFocus(int amount)
	{
		int size = this.getCellComponents().size();
		if (size == 0) return;

		this.setFocus(this.getCellComponents().size() + this.focus + amount % this.getCellComponents().size());
	}

	public void setFocus(int index)
	{
		if (this.isLocked()) return;
		if (this.getCellComponents().size() == 0)
		{
			this.focus = -1;
			return;
		}

		this.focus = index % this.getCellComponents().size();
		this.updateFocus();
	}

	public void updateFocus()
	{
		if (this.focus < 0 || this.focus > this.getCellComponents().size()) return;

		this.setCurrentSelection(this.focus);
		for (int i = 0; i < this.getCellComponents().size(); i++)
			this.getCellComponents().get(i).setHovered(i == this.focus);

		this.lastInput = Game.time().now();
	}

	public int onConfirmation(Consumer<Integer> action)
	{
		this.confirmations.add(action);
		return this.confirmations.size() -1;
	}

	public Consumer<Integer> cancelOnConfirmation(int id) { return this.confirmations.remove(id); }
	private void confirmed() { this.confirmations.forEach(c -> c.accept(this.focus)); }

	@Override
	public void prepare()
	{
		super.prepare();
		this.setForwardMouseEvents(false);
		this.getCellComponents().forEach(c -> c.setForwardMouseEvents(false));

		this.getCellComponents().forEach(c ->
		{
			c.setFont(UIManager.Fonts.GUI.get());
			c.getAppearance().setForeColor(this.theme.fore);
			c.getAppearance().setBackgroundColor1(this.theme.bg);
			c.getAppearance().setTransparentBackground(false);
			c.getAppearance().setTextAntialiasing(true);

			c.getAppearanceHovered().setForeColor(this.theme.hoveredFore);
			c.getAppearanceHovered().setBackgroundColor1(this.theme.hoveredBg);
			c.getAppearanceHovered().setTextAntialiasing(true);
		});

		if (!this.getCellComponents().isEmpty())
		{
			this.focus = 0;
			this.getCellComponents().get(0).setHovered(true);
		}
	}

	public boolean isLocked() { return this.isSuspended() || !this.isVisible() || !this.isEnabled() || Game.time().since(this.lastInput) < DELAY; }

	public Theme getTheme() { return this.theme; }

	public static enum Theme
	{
		PITCH_BLACK(Color.WHITE, UIManager.Colors.BACKGROUND.get(), Color.YELLOW, UIManager.Colors.BACKGROUND.get()),
		;

		private final Color fore;
		private final Color bg;
		private final Color hoveredFore;
		private final Color hoveredBg;

		private Theme(Color fore, Color bg, Color hoveredFore, Color hoveredBg)
		{
			this.fore = fore;
			this.bg = bg;
			this.hoveredFore = hoveredFore;
			this.hoveredBg = hoveredBg;
		}

		public Color getForeColor() { return this.fore; }
		public Color getBackgroundColor() { return this.bg; }
		public Color getHoveredForeColor() { return this.hoveredFore; }
		public Color getHoveredBackgroundColor() { return this.hoveredBg; }
	}
}
