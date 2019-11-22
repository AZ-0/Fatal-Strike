package fr.az.fatalstrike.util.parsable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class ParsableInputStream extends ParsableReader<InputStream>
{
	private static final long serialVersionUID = 2986326569068895719L;

	public ParsableInputStream(InputStream stream)
	{
		super(stream);
	}

	@Override
	protected Reader produce(InputStream stream) throws IOException
	{
		return new InputStreamReader(stream);
	}
}
