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
      level.name = "Small Jump";
      level.goldTimeInMilliseconds = 233;
      level.silverTimeInMilliseconds = 500;
      level.bronzeTimeInMilliseconds = 1000;
      final Door door = new Door(-55.0f, -30.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Medium Jump";
      level.goldTimeInMilliseconds = 390;
      level.silverTimeInMilliseconds = 500;
      level.bronzeTimeInMilliseconds = 1000;
      final Door door = new Door(-45.0f, -20.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Long Jump";
      level.goldTimeInMilliseconds = 600;
      level.silverTimeInMilliseconds = 700;
      level.bronzeTimeInMilliseconds = 1500;
      final Door door = new Door(-25.0f, -10.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Big Jump";
      level.goldTimeInMilliseconds = 1000;
      level.silverTimeInMilliseconds = 1200;
      level.bronzeTimeInMilliseconds = 1800;
      final Door door = new Door(10.0f, 10.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Even Bigger Jump";
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
      level.name = "It Moves";
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
      level.goldTimeInMilliseconds = 750;
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

    {
      final Level level = new Level();
      level.name = "Blocked";
      level.goldTimeInMilliseconds = 1500;
      level.silverTimeInMilliseconds = 2000;
      level.bronzeTimeInMilliseconds = 2500;

      final Door door = new Door(-20.0f, -10.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);

      final Block block1 = new Block(-25.0f, -10.0f, 2.5f, 2.5f);
      level.blocks.add(block1);

      final Block block2 = new Block(-25.0f, -12.5f, 2.5f, 2.5f);
      level.blocks.add(block2);

      final Block block3 = new Block(-25.0f, -7.5f, 2.5f, 2.5f);
      level.blocks.add(block3);

      final Block block4 = new Block(-25.0f, -15.0f, 2.5f, 2.5f);
      level.blocks.add(block4);

      final Block block5 = new Block(-27.5f, -15.0f, 2.5f, 2.5f);
      level.blocks.add(block5);

      final Block block6 = new Block(-30.0f, -15.0f, 2.5f, 2.5f);
      level.blocks.add(block6);

      final Block block7 = new Block(-25.0f, -5.0f, 2.5f, 2.5f);
      level.blocks.add(block7);

      final Block block8 = new Block(-25.0f, -2.5f, 2.5f, 2.5f);
      level.blocks.add(block8);
    }
  }

  void selectPreviousLevel()
  {
    if (isAtBeginning())
    {
      goToEnd();
    }
    else
    {
      levelIndex--;
    }
  }

  void selectNextLevel()
  {
    if (isAtEnd())
    {
      goToBeginning();
    }
    else
    {
      levelIndex++;
    }
  }

  Level getLevel()
  {
    if (levelIndex >= 0 && levelIndex < levels.size())
    {
      return levels.get(levelIndex);
    }

    return null;
  }

  boolean isAtBeginning()
  {
    return levelIndex == 0;
  }

  boolean isAtEnd()
  {
    return levelIndex == levels.size() - 1;
  }

  int getLevelIndex()
  {
    return levelIndex;
  }

  void goToBeginning()
  {
    levelIndex = 0;
  }

  void goToEnd()
  {
    levelIndex = levels.size() - 1;
  }
}
