package com.orbinski.megajump;

import java.util.ArrayList;
import java.util.List;

class Levels
{
  private final List<Level> levels;
  private int levelIndex;

  Levels()
  {
    levels = new ArrayList<>();
    levelIndex = -1;

    {
      final Level level = new Level();
      level.name = "So Close";
      level.goldTimeInMilliseconds = 233;
      level.silverTimeInMilliseconds = 500;
      level.bronzeTimeInMilliseconds = 1000;
      final Door door = new Door(-55.0f, -30.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Tutorial 2";
      level.goldTimeInMilliseconds = 390;
      level.silverTimeInMilliseconds = 500;
      level.bronzeTimeInMilliseconds = 1000;
      final Door door = new Door(-45.0f, -20.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Tutorial 3";
      level.goldTimeInMilliseconds = 600;
      level.silverTimeInMilliseconds = 700;
      level.bronzeTimeInMilliseconds = 1500;
      final Door door = new Door(-25.0f, -10.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Tutorial 4";
      level.goldTimeInMilliseconds = 1000;
      level.silverTimeInMilliseconds = 1200;
      level.bronzeTimeInMilliseconds = 1800;
      final Door door = new Door(10.0f, 10.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Tutorial 5";
      level.goldTimeInMilliseconds = 1200;
      level.silverTimeInMilliseconds = 1500;
      level.bronzeTimeInMilliseconds = 2000;
      final Door door = new Door(25.0f, 25.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Dwarves Can't Jump";
      level.goldTimeInMilliseconds = 900;
      level.silverTimeInMilliseconds = 1200;
      level.bronzeTimeInMilliseconds = 1600;
      final Door door = new Door(-75.0f, 33.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }
  }

  Level getNext()
  {
    if (!isAtEnd())
    {
      levelIndex++;
    }

    return levels.get(levelIndex);
  }

  boolean isAtEnd()
  {
    return levelIndex == levels.size() - 1;
  }

  int getLevelIndex()
  {
    return levelIndex;
  }

  void reset()
  {
    levelIndex = -1;
  }
}
