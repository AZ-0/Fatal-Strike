package fr.az.fatalstrike.util.parsable;

import java.io.IOException;
import java.io.Reader;

import fr.az.fatalstrike.util.Util;
import fr.az.util.parsing.Parsable;

public abstract class ParsableReader<T> implements Parsable
{
	private static final long serialVersionUID = -8338873700571863422L;

	protected T readable;

	public ParsableReader(T readable)
	{
		this.readable = readable;
	}

	@Override
	public String asParsableString()
	{
		try (Reader reader = this.produce(this.readable))
		{
			return Util.readAll(reader);
		} catch (IOException e)
		{
			e.printStackTrace();
			System.err.println("> Returned empty String instead");
		}

		return "";
	}

	protected abstract Reader produce(T readable) throws IOException;

	public void set(T readable) { this.readable = readable; }
	public T get() { return this.readable; }
}
