package fr.az.fatalstrike.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author A~Z
 * @since Minecraft GDK 0.0.1
 */
public class Util
{
	@SafeVarargs
	public static <T> Set<T> newSet(T ... values)
	{
		Set<T> set = new HashSet<>();
		set.addAll(Arrays.asList(values));
		return set;
	}
	
	public static class PATH
	{
		public final static String IMAGE = "/img/";
		
		public final static String VIEW = "/view/";
			public final static String COMPONENT = "/view/component/";
			public final static String FXML = "/view/fxml/";
	}
}