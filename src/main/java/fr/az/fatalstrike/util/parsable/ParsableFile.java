package fr.az.fatalstrike.util.parsable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class ParsableFile extends ParsableReader<File>
{
	private static final long serialVersionUID = 3963135360901580723L;

	public ParsableFile(File file) { super(file); }

	@Override
	protected Reader produce(File readable) throws IOException
	{
		return new FileReader(readable);
	}
}
