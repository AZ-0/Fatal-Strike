package fr.az.fatalstrike.core.loader;

import fr.az.fatalstrike.core.game.Action;
import fr.az.util.parsing.string.ListParser;
import fr.az.util.parsing.string.Parser;

public class Parsers
{
	Parser<Action> ACTION = Action::get;
	ListParser<Action> ACTION_LIST = new ListParser<>(this.ACTION, "Unknown action: "+ ListParser.REPLACE_BY_INPUT);
}
