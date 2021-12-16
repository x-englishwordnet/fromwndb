/*
 * Copyright (c) 2021. Bernard Bou.
 */

package org.oewntk.wndb.in;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

public class SenseToVerbTemplatesParser
{
	private final File inDir;

	public SenseToVerbTemplatesParser(final File inDir)
	{
		this.inDir = inDir;
	}

	public Collection<Entry<String, int[]>> parse() throws IOException
	{
		Collection<Entry<String, int[]>> result = new ArrayList<>();
		parseVerbTemplates(new File(inDir, "sentidx.vrb"), result);
		return result;
	}

	public static void parseVerbTemplates(File file, Collection<Entry<String, int[]>> entries) throws IOException
	{
		// iterate on lines
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)))
		{
			int lineCount = 0;
			String line;
			while ((line = reader.readLine()) != null)
			{
				lineCount++;
				if (line.isEmpty() || line.charAt(0) == ' ')
				{
					continue;
				}

				try
				{
					String[] fields = line.split("[\\s,]+");
					String sensekey = fields[0];
					int[] templateIds = new int[fields.length - 1];
					for (int i = 1; i < fields.length; i++)
					{
						templateIds[i - 1] = Integer.parseInt(fields[i]);
					}
					entries.add(new SimpleEntry<>(sensekey, templateIds));
				}
				catch (final RuntimeException e)
				{
					System.err.println("[E] verb templates at line " + lineCount + " " + e);
				}
			}
		}
	}
}
