package fr.az.fatalstrike.game;

import java.awt.Image;

public class Action
{
	private Image icon;
	
	public Action(Image icon) { this.icon = icon; }
	
	public void apply(Player p)
	{
		
	}
	
	public Image getIcon() { return icon; }
}
