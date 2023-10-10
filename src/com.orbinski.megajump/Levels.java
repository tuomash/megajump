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
      level.name = "Tutorial 1";
      final Door door = new Door(-55.0f, -30.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Tutorial 2";
      final Door door = new Door(-45.0f, -20.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Tutorial 3";
      final Door door = new Door(-25.0f, -10.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Tutorial 4";
      final Door door = new Door(10.0f, 10.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Tutorial 5";
      final Door door = new Door(25.0f, 25.0f, 5.0f, 5.0f);
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