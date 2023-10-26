package com.orbinski.megajump;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

class Save implements Serializable
{
  private static final String saveFilePath = System.getProperty("user.dir") + File.separator + "save.dat";

  Map<String, Score> scores = new HashMap<>();

  void updateScore(final Level level)
  {
    if (level.tag == null || level.tag.isEmpty() || !level.scoreUpdate || !level.finished)
    {
      return;
    }

    final Score score;

    if (scores.containsKey(level.tag.toUpperCase()))
    {
      score = scores.get(level.tag.toUpperCase());
    }
    else
    {
      score = new Score();
    }

    score.save(level);
    scores.put(score.levelTag, score);
    writeToDisk(this);
    level.scoreUpdate = false;
  }

  static void writeToDisk(final Save save)
  {
    final File file = new File(saveFilePath);

    try
    {
      write(file, save);
    }
    catch (final Exception e)
    {
      System.out.println("error: couldn't write save file: " + e.getMessage());
    }
  }

  static Save readFromDisk()
  {
    final File file = new File(saveFilePath);

    try
    {
      return read(file);
    }
    catch (final Exception e)
    {
      System.out.println("error: couldn't read save file: " + e.getMessage());
    }

    return null;
  }

  static boolean doesSaveFileExist()
  {
    return new File(saveFilePath).exists();
  }

  private static void write(final File path, final Save save)
  {
    try (final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path)))
    {
      out.writeObject(save);
    }
    catch (final Exception e)
    {
      throw new IllegalStateException("Couldn't write data!", e);
    }
  }

  private static Save read(final File path) throws FileNotFoundException
  {
    try (final ObjectInputStream in = new ObjectInputStream(new FileInputStream(path)))
    {
      return (Save) in.readObject();
    }
    catch (final FileNotFoundException e)
    {
      throw e;
    }
    catch (final Exception e)
    {
      throw new IllegalStateException("Couldn't read data!", e);
    }
  }
}
