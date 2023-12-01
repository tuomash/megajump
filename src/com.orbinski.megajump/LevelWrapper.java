package com.orbinski.megajump;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class LevelWrapper implements Serializable
{
  private static final String levelsDirectory = System.getProperty("user.dir") + File.separator + "levels" + File.separator;
  private static final long serialVersionUID = 1L;

  String name;
  String tag;
  int goldTimeInMilliseconds;
  int silverTimeInMilliseconds;
  int bronzeTimeInMilliseconds;
  ExitWrapper exit;
  SpawnWrapper spawn;
  List<DecorationWrapper> decorations;
  List<BlockWrapper> blocks;
  List<TrampolineWrapper> trampolines;
  List<PlatformWrapper> platforms;
  List<TeleportWrapper> teleports;
  boolean moveCameraX;
  boolean moveCameraY;
  Point2D.Float cameraCeiling;
  Point2D.Float cameraFloor;
  Point2D.Float deathPoint;

  LevelWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  LevelWrapper(final Level level)
  {
    name = level.getName();
    tag = level.getTag();
    goldTimeInMilliseconds = level.goldTimeInMilliseconds;
    silverTimeInMilliseconds = level.silverTimeInMilliseconds;
    bronzeTimeInMilliseconds = level.bronzeTimeInMilliseconds;
    exit = new ExitWrapper(level.exit);
    spawn = new SpawnWrapper(level.spawn);
    decorations = new ArrayList<>();
    blocks = new ArrayList<>();
    trampolines = new ArrayList<>();
    platforms = new ArrayList<>();
    teleports = new ArrayList<>();
    moveCameraX = level.moveCameraX;
    moveCameraY = level.moveCameraY;
    cameraCeiling = new Point2D.Float();
    cameraFloor = new Point2D.Float();
    deathPoint = new Point2D.Float();

    for (int i = 0; i < level.decorations.size(); i++)
    {
      decorations.add(new DecorationWrapper(level.decorations.get(i)));
    }

    for (int i = 0; i < level.blocks.size(); i++)
    {
      blocks.add(new BlockWrapper(level.blocks.get(i)));
    }

    for (int i = 0; i < level.trampolines.size(); i++)
    {
      trampolines.add(new TrampolineWrapper(level.trampolines.get(i)));
    }

    for (int i = 0; i < level.platforms.size(); i++)
    {
      platforms.add(new PlatformWrapper(level.platforms.get(i)));
    }

    for (int i = 0; i < level.teleports.size(); i++)
    {
      teleports.add(new TeleportWrapper(level.teleports.get(i)));
    }

    cameraCeiling.setLocation(level.cameraCeiling);
    cameraFloor.setLocation(level.cameraFloor);
    deathPoint.setLocation(level.deathPoint);
  }

  Level unwrap()
  {
    final Level level = new Level();
    level.setName(name);
    level.setTag(tag);
    level.goldTimeInMilliseconds = goldTimeInMilliseconds;
    level.silverTimeInMilliseconds = silverTimeInMilliseconds;
    level.bronzeTimeInMilliseconds = bronzeTimeInMilliseconds;

    if (exit != null)
    {
      level.exit = exit.unwrap();
    }

    if (spawn != null)
    {
      level.spawn = spawn.unwrap();
    }

    if (decorations != null)
    {
      for (int i = 0; i < decorations.size(); i++)
      {
        level.decorations.add(decorations.get(i).unwrap());
      }
    }

    if (blocks != null)
    {
      for (int i = 0; i < blocks.size(); i++)
      {
        level.blocks.add(blocks.get(i).unwrap());
      }
    }

    if (trampolines != null)
    {
      for (int i = 0; i < trampolines.size(); i++)
      {
        level.trampolines.add(trampolines.get(i).unwrap());
      }
    }

    if (platforms != null)
    {
      for (int i = 0; i < platforms.size(); i++)
      {
        level.platforms.add(platforms.get(i).unwrap());
      }
    }

    if (teleports != null)
    {
      for (int i = 0; i < teleports.size(); i++)
      {
        level.teleports.add(teleports.get(i).unwrap());
      }
    }

    level.moveCameraX = moveCameraX;
    level.moveCameraY = moveCameraY;

    if (cameraCeiling != null)
    {
      level.cameraCeiling.setLocation(cameraCeiling);
    }

    if (cameraFloor != null)
    {
      level.cameraFloor.setLocation(cameraFloor);
    }

    if (deathPoint != null)
    {
      level.deathPoint.setLocation(deathPoint);
    }

    return level;
  }

  boolean writeToDisk()
  {
    final File file = new File(levelsDirectory + tag + ".dat");

    try
    {
      write(file, this);
    }
    catch (final Exception e)
    {
      System.out.println("error: couldn't write level file: " + e.getMessage());
      return false;
    }

    return true;
  }

  static boolean doesLevelExist(final String levelTag)
  {
    return new File(levelsDirectory + levelTag + ".dat").exists();
  }

  static boolean deleteLevel(final String levelTag)
  {
    if (doesLevelExist(levelTag))
    {
      return new File(levelsDirectory + levelTag + ".dat").delete();
    }

    return false;
  }

  static LevelWrapper readFromDisk(final String levelTag)
  {
    final File file = new File(levelsDirectory + levelTag + ".dat");

    try
    {
      return read(file);
    }
    catch (final Exception e)
    {
      System.out.println("error: couldn't read level file '" + file.getName() + "': " + e.getMessage());
      e.printStackTrace();
    }

    return null;
  }

  private static void write(final File path, final LevelWrapper wrapper) throws IOException
  {
    try (final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path)))
    {
      out.writeObject(wrapper);
    }
  }

  private static LevelWrapper read(final File path) throws IOException, ClassNotFoundException
  {
    try (final ObjectInputStream in = new ObjectInputStream(new FileInputStream(path)))
    {
      return (LevelWrapper) in.readObject();
    }
  }
}
