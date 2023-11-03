package com.orbinski.megajump;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

class LevelWrapper implements Serializable
{
  private static final String levelsDirectory = System.getProperty("user.dir") + File.separator + "levels" + File.separator;

  int version = 1;

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
  Point2D.Float cameraFloor;
  Point2D.Float deathPoint;

  LevelWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  LevelWrapper(final Level level)
  {
    name = level.name;
    tag = level.tag;
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

    cameraFloor.setLocation(level.cameraFloor.x, level.cameraFloor.y);
    deathPoint.setLocation(level.deathPoint.x, level.deathPoint.y);
  }

  Level unwrap()
  {
    final Level level = new Level();
    level.tag = tag;
    level.name = name;
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

    if (cameraFloor != null)
    {
      level.cameraFloor.setLocation(cameraFloor.x, cameraFloor.y);
    }

    if (deathPoint != null)
    {
      level.deathPoint.setLocation(deathPoint.x, deathPoint.y);
    }

    return level;
  }

  void writeToDisk()
  {
    final File file = new File(levelsDirectory + tag + ".dat");

    try
    {
      write(file, this);
    }
    catch (final Exception e)
    {
      System.out.println("error: couldn't write level file: " + e.getMessage());
    }
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
      System.out.println("error: couldn't read level file: " + e.getMessage());
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
