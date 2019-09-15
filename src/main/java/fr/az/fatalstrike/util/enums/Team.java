package fr.az.fatalstrike.util.enums;

public enum Team
{
	BLUE('B'),
	RED('R'),
	;
	
	private char id;
	
	private Team(char id)
	{
		this.id = id;
	}
	
	public char getId()
	{
		return id;
	}
}
