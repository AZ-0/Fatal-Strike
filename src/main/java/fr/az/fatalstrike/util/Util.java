package fr.az.fatalstrike.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

/**
 * @author A~Z
 */
public class Util
{
	@SafeVarargs
	public static <T> Set<T> newSet(T ... values)
	{
		Set<T> set = new HashSet<>();

		for (T value : values)
			set.add(value);

		return set;
	}

	public static String readAll(Reader r) throws IOException
	{
		BufferedReader reader = new BufferedReader(r);

		StringBuilder sb = new StringBuilder();
		String line;

		while ((line = reader.readLine()) != null)
			sb.append(line +'\n');

		reader.close();
		return sb.substring(0, sb.length() -1);
	}
}