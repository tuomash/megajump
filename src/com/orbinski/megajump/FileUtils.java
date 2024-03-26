package com.orbinski.megajump;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

class FileUtils
{
  static List<String> getContents(final String path)
  {
    /* TODO: fix logging
    if (log.isLoggable(java.util.logging.Level.FINEST))
    {
      log.finest("Reading file from " + path);
    }
     */

    final List<String> contents = new ArrayList<>();
    final File file = new File(path);

    if (!file.exists())
    {
      /* TODO: fix logging
      if (log.isLoggable(java.util.logging.Level.WARNING))
      {
        log.warning("Couldn't parse contents because the target file doesn't exist! [target= + " + file.getPath() + "]");
      }
       */

      return contents;
    }
    else if (!file.isFile())
    {
      /* TODO: fix logging
      if (log.isLoggable(java.util.logging.Level.WARNING))
      {
        log.warning("Couldn't parse contents because the target file is not a file! [target= + " + file.getPath() + "]");
      }
       */

      return contents;
    }

    try (final BufferedReader reader = new BufferedReader(new FileReader(file)))
    {
      String text;

      while ((text = reader.readLine()) != null)
      {
        // Do not add comment lines
        if (text.startsWith("#"))
        {
          continue;
        }

        contents.add(text);
      }
    }
    catch (final Exception e)
    {
      /* TODO: fix logging
      if (log.isLoggable(java.util.logging.Level.WARNING))
      {
        log.log(Level.WARNING, "Could not parse file! [path=" + path + "]", e);
      }
       */
    }

    return contents;
  }
}
