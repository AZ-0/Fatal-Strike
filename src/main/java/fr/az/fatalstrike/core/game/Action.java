package fr.az.fatalstrike.core.game;

import java.awt.Image;

public class Action
{
	private Image icon;

	public static Action get(String id)
	{
		return null;
	}

	public Action(Image icon) { this.icon = icon; }

	public void apply(Player p)
	{

	}

	public Image getIcon() { return this.icon; }
}
