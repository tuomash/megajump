package com.orbinski.megajump;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

class Save implements Serializable
{
  private static final String saveFilePath = System.getProperty("user.dir") + File.separator + "save.dat";
  private static final long serialVersionUID = 1L;

  Map<String, Score> scores = new HashMap<>();

  void updateScore(final Level level)
  {
    if (level.getTag() == null || level.getTag().isEmpty() || !level.scoreUpdate || !level.cleared)
    {
      return;
    }

    final Score score;

    if (scores.containsKey(level.getTag()))
    {
      score = scores.get(level.getTag());
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

  static void backupSaveFile()
  {
    final File src = new File(saveFilePath);
    final String backupFilePath = System.getProperty("user.dir") + File.separator + "save_backup_"
        + System.currentTimeMillis() + ".dat";
    final File dst = new File(backupFilePath);

    try
    {
      Files.copy(src.toPath(), dst.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }
    catch (final Exception e)
    {
      System.out.println("error: couldn't backup save file: " + e.getMessage());
    }
  }

  private static void write(final File path, final Save save) throws IOException
  {
    try (final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path)))
    {
      out.writeObject(save);
    }
  }

  private static Save read(final File path) throws IOException, ClassNotFoundException
  {
    try (final ObjectInputStream in = new ObjectInputStream(new FileInputStream(path)))
    {
      return (Save) in.readObject();
    }
  }
}
