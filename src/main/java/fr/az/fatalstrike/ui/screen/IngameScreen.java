package fr.az.fatalstrike.ui.screen;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import de.gurkenlabs.litiengine.Align;
import de.gurkenlabs.litiengine.Direction;
import de.gurkenlabs.litiengine.Game;
import de.gurkenlabs.litiengine.Valign;
import de.gurkenlabs.litiengine.environment.tilemap.IMap;
import de.gurkenlabs.litiengine.gui.GuiComponent;
import de.gurkenlabs.litiengine.gui.ImageComponent;
import de.gurkenlabs.litiengine.gui.screens.GameScreen;
import de.gurkenlabs.litiengine.resources.Resources;
import fr.az.fatalstrike.FatalStrike;
import fr.az.fatalstrike.FatalStrike.IMAGES;
import fr.az.fatalstrike.FatalStrike.MAPS;
import fr.az.fatalstrike.game.Action;
import fr.az.fatalstrike.game.Player;
import fr.az.fatalstrike.ui.SlideMenu;
import fr.az.fatalstrike.util.Tuple2;

public final class IngameScreen extends GameScreen
{
	private static final IngameScreen screen = new IngameScreen();
	public static final String NAME = "screen.ingame";
	
	public static IngameScreen screen() { return screen; }
	
	private ActionBar actionBar;
	private Set<Consumer<IngameScreen>> onPlayerValidated = new HashSet<>();
	
	private int player = 0;
	
	private IngameScreen() { super(NAME); }
	
	@Override
	protected void initializeComponents()
	{
		super.initializeComponents();
		
		this.actionBar = new ActionBar(0, 0);
		this.actionBar.buttonValidation.onClicked(e ->
		{
			Game.window().getRenderComponent().fadeOut(200);
			this.onPlayerValidated.forEach(c -> c.accept(this));
			
			Game.loop().perform(200, () ->
			{
				Player p = FatalStrike.manager().players().get(this.player++);
				this.actionBar.getActions().forEach(SlideMenu::clearItems);
				this.actionBar.getActions().forEach(s -> s.addItems(Action::getIcon, p.getCharacter().getActions()));
			});
		});
		this.getComponents().add(actionBar);
	}
	
	@Override
	public void prepare()
	{
		super.prepare();
		Game.world().camera().setClampToMap(false);
		Game.world().loadEnvironment(MAPS.FIGHT);
		
		IMap map = Game.world().environment().getMap();
		Point2D focus = Game.world().environment().getCenter();
		Dimension screen = FatalStrike.getEffectiveWindowSize();
		
		//Display Computations
		float mapWidth = map.getWidth() * map.getTileWidth() * Game.graphics().getBaseRenderScale();
		float mapHeight = map.getHeight() * map.getTileHeight() * Game.graphics().getBaseRenderScale();
		
		float offset = (screen.height - mapHeight) / 2;
		float actionBarX = mapWidth + 2*offset -1;
		
		//Map Display
		focus.setLocation((screen.width/2f - offset) / Game.graphics().getBaseRenderScale(), focus.getY());
		Game.world().camera().setFocus(focus);
		
		//Action Bar Display
		this.actionBar.setLocation(actionBarX, offset +1);
		this.actionBar.setDimension(screen.width - actionBarX - offset, mapHeight);
	}
	
	public void onPlayerValidated(Consumer<IngameScreen> action) { this.onPlayerValidated.add(action); }
	public ActionBar getActionBar() { return this.actionBar; }
	
	
	public static class ActionBar extends GuiComponent
	{
		private static final BufferedImage BUTTON_VALIDATION = Resources.images().get("ui/validation.png");
		
		private ArrayList<Tuple2<SlideMenu<Action>, SlideMenu<Direction>>> slides = new ArrayList<>();
		private ImageComponent buttonValidation;
		
		private double offset;
		private double offsetIn;
		
		private boolean dimensionChanged = true;
		private boolean positionChanged = false;
		
		protected ActionBar(double x, double y) { super(x, y); }
		protected ActionBar(double x, double y, double width, double height) { super(x, y, width, height); }
		
		{
			IMAGES.UI_ARROW_LEFT.getHeight();
			SlideMenu.ARROW_LEFT = IMAGES.UI_ARROW_LEFT;
			SlideMenu.ARROW_RIGHT = IMAGES.UI_ARROW_RIGHT;
			
			for (int i = 0; i < 3; i++)
			{
				Tuple2<SlideMenu<Action>, SlideMenu<Direction>> tuple = new Tuple2<>(
						new SlideMenu<>(0, 0, this.getWidth(), this.getHeight() / 7, null),
						new SlideMenu<>(0, 0, this.getWidth(), this.getHeight() / 7, null));
				
				tuple.a.onRenderChanged(SlideMenu::adjustWidthToHeight);
				tuple.b.onRenderChanged(SlideMenu::adjustWidthToHeight);
				
				this.getComponents().add(tuple.a);
				this.getComponents().add(tuple.b);
				
				this.slides.add(tuple);
			}
		}
		
		@Override
		protected void initializeComponents()
		{
			super.initializeComponents();
			
			this.buttonValidation = new ImageComponent(0, 0, BUTTON_VALIDATION);
			this.buttonValidation.setImageAlign(Align.CENTER);
			this.buttonValidation.setImageValign(Valign.MIDDLE);
			this.getComponents().add(this.buttonValidation);
		}
		
		@Override
		public void render(Graphics2D g)
		{
			if (this.dimensionChanged)
			{
				final double height = Math.min(this.getWidth() / 4, this.getHeight() / 7);
				this.slides.forEach(tuple ->
				{
					tuple.a.setDimension(this.getWidth(), height);
					tuple.b.setDimension(this.getWidth(), height);
				});
				
				int cheight = (int) (Math.ceil(height) < 1 ? 1 : Math.ceil(height));
				this.buttonValidation.setDimension(cheight, cheight);
				this.buttonValidation.setImage(BUTTON_VALIDATION.getScaledInstance(cheight, cheight, Image.SCALE_SMOOTH));
				
				this.offset = this.getHeight() - 7 * height;
				this.offsetIn = this.offset * .1; //this.offset * .3 / 3
				this.offset = (this.offset - 3 * offsetIn) / 3;
				
				this.dimensionChanged = false;
				this.positionChanged = true;
			}
			
			if (this.positionChanged)
			{
				double y = this.getY();
				double demiwidth = this.getWidth() / 2;
				
				for (Tuple2<SlideMenu<Action>, SlideMenu<Direction>> tuple : this.slides)
				{
					tuple.a.setLocation(this.getX() + demiwidth - tuple.a.getWidth() / 2, y);
					y += this.offsetIn + tuple.a.getHeight();
					
					tuple.b.setLocation(this.getX() + demiwidth - tuple.b.getWidth() / 2, y);
					y += this.offset + tuple.b.getHeight();
				}
				
				this.buttonValidation.setLocation(this.getX() + demiwidth - this.buttonValidation.getWidth() / 2, y);
			}
			
			super.render(g);
		}
		
		@Override public void setDimension(double width, double height) { super.setDimension(width, height); dimensionChanged = true; }
		@Override public void setWidth(double width) { super.setWidth(width); this.dimensionChanged = true; }
		@Override public void setHeight(double height) { super.setHeight(height); this.dimensionChanged = true; }

		@Override public void setLocation(Point2D location) { super.setLocation(location); this.positionChanged = true; }
		@Override public void setLocation(double x, double y) { super.setLocation(x, y); this.positionChanged = true; }
		@Override public void setX(double x) { super.setX(x); this.positionChanged = true; }
		@Override public void setY(double y) { super.setY(y); this.positionChanged = true; }
		
		public List<SlideMenu<Action>> getActions() {
			return Arrays.asList(this.slides.get(0).a, this.slides.get(1).a, this.slides.get(2).a); }
		
		public List<SlideMenu<Direction>> getDirections() {
			return Arrays.asList(this.slides.get(0).b, this.slides.get(1).b, this.slides.get(2).b); }
		
		public List<Tuple2<SlideMenu<Action>, SlideMenu<Direction>>> getSlides() { return Collections.unmodifiableList(this.slides); }
	}
}
