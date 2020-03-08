package com.hackquest.shenlong55.instancesplugin.utility;

import java.io.File;

public final class DirectoryRemover
{
	private final File directory;

	public DirectoryRemover(final File directory)
	{
		this.directory = directory;
	}

	public void removeDirectory()
	{
		removeDirectory(directory);
	}

	private void removeDirectory(final File directory)
	{
		final String[] files = directory.list();
		for (final String file : files)
		{
			final File currentFile = new File(directory, file);

			if (currentFile.isDirectory())
			{
				removeDirectory(currentFile);
			}
			else
			{
				currentFile.delete();
			}
		}

		directory.delete();
	}
}