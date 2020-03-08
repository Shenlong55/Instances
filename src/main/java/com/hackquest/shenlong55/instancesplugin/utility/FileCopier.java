package com.hackquest.shenlong55.instancesplugin.utility;

import java.io.File;
import java.io.IOException;

import com.google.common.io.Files;

public final class FileCopier
{
	private final File	source;
	private final File	destination;

	public FileCopier(final File source, final File destination)
	{
		this.source = source;
		this.destination = destination;
	}

	public void copyFiles() throws IOException
	{
		copyDirectory(source, destination);
	}

	private void copyDirectory(final File source, final File destination) throws IOException
	{
		if (source.isDirectory())
		{
			if (!destination.exists())
			{
				destination.mkdir();
			}

			for (final String child : source.list())
			{
				copyDirectory(new File(source, child), new File(destination, child));
			}
		}
		else
		{
			Files.copy(source, destination);
		}
	}
}