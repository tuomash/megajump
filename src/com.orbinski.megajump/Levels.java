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

    {
      final Level level = new Level();
      level.name = "Orndorf's Jump";
      level.goldTimeInMilliseconds = 3000;
      level.silverTimeInMilliseconds = 3500;
      level.bronzeTimeInMilliseconds = 4000;
      level.moveCamera = true;

      final Door door = new Door(190.0f, -30.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);

      final Decoration decoration1 = new Decoration(5.0f, 5.0f, 1.5f, 2.5f);
      level.decorations.add(decoration1);

      final Decoration decoration2 = new Decoration(55.0f, 5.0f, 1.5f, 2.5f);
      level.decorations.add(decoration2);

      final Decoration decoration3 = new Decoration(95.0f, 5.0f, 1.5f, 2.5f);
      level.decorations.add(decoration3);

      final Decoration decoration4 = new Decoration(145.0f, 5.0f, 1.5f, 2.5f);
      level.decorations.add(decoration4);
    }

    {
      final Level level = new Level();
      level.name = "Da Moving PF";
      level.goldTimeInMilliseconds = 1000;
      level.silverTimeInMilliseconds = 1500;
      level.bronzeTimeInMilliseconds = 2000;

      final Door door = new Door(-20.0f, -10.0f, 5.0f, 5.0f);
      door.type = Door.Type.MOVING;
      door.movingToTarget = false;
      door.velocityX = 10.0f;
      door.velocityY = 10.0f;
      door.startX = door.getX();
      door.endX = 25.0f;
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Elevator";
      level.goldTimeInMilliseconds = 700;
      level.silverTimeInMilliseconds = 1000;
      level.bronzeTimeInMilliseconds = 1500;

      final Door door = new Door(-20.0f, -10.0f, 5.0f, 5.0f);
      door.type = Door.Type.MOVING;
      door.movingToTarget = false;
      door.velocityX = 10.0f;
      door.velocityY = 10.0f;
      door.startY = door.getY();
      door.endY = 40.0f;
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
