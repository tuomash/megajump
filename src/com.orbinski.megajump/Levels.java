package com.orbinski.megajump;

import java.awt.geom.Point2D;
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
      level.moveCameraX = true;

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
      door.addWaypoint(new Point2D.Float(-25.0f, -10.0f));
      door.addWaypoint(new Point2D.Float(30.0f, -10.0f));
      door.velocityX = 10.0f;
      door.velocityY = 10.0f;
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
      door.addWaypoint(new Point2D.Float(-20.0f, -10.0f));
      door.addWaypoint(new Point2D.Float(-20.0f, 30.0f));
      door.velocityX = 10.0f;
      door.velocityY = 10.0f;
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Rectangle";
      level.goldTimeInMilliseconds = 750;
      level.silverTimeInMilliseconds = 1000;
      level.bronzeTimeInMilliseconds = 1500;

      final Door door = new Door(-20.0f, -10.0f, 5.0f, 5.0f);
      door.addWaypoint(new Point2D.Float(-20.0f, -10.0f));
      door.addWaypoint(new Point2D.Float(-20.0f, 20.0f));
      door.addWaypoint(new Point2D.Float(40.0f, 20.0f));
      door.addWaypoint(new Point2D.Float(40.0f, -10.0f));
      door.velocityX = 17.5f;
      door.velocityY = 17.5f;
      level.setDoor(door);
      levels.add(level);
    }

    {
      final Level level = new Level();
      level.name = "Blocked 2";
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

    {
      final Level level = new Level();
      level.name = "Trampoline 1";
      level.goldTimeInMilliseconds = 2800;
      level.silverTimeInMilliseconds = 3500;
      level.bronzeTimeInMilliseconds = 5000;

      final Door door = new Door(20.0f, 15.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);

      final Trampoline trampoline = new Trampoline(-15.0f, -25.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline);
    }

    {
      final Level level = new Level();
      level.name = "Trampoline 2";
      level.moveCameraX = true;
      level.moveCameraY = true;
      level.goldTimeInMilliseconds = 3200;
      level.silverTimeInMilliseconds = 3600;
      level.bronzeTimeInMilliseconds = 5000;

      final Door door = new Door(40.0f, 45.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);

      final Trampoline trampoline = new Trampoline(-15.0f, -25.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline);
    }

    {
      final Level level = new Level();
      level.moveCameraX = true;
      // level.moveCameraY = true;
      level.name = "Trampoline 3";
      level.goldTimeInMilliseconds = 10000;
      level.silverTimeInMilliseconds = 12000;
      level.bronzeTimeInMilliseconds = 15000;

      final Door door = new Door(145.0f, -25.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);

      final Trampoline trampoline1 = new Trampoline(-15.0f, -25.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline1);

      final Trampoline trampoline2 = new Trampoline(45.0f, -25.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline2);

      final Trampoline trampoline3 = new Trampoline(95.0f, -25.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline3);
    }

    {
      final Level level = new Level();
      level.moveCameraX = true;
      level.moveCameraY = true;
      level.name = "Trampoline 4";
      level.goldTimeInMilliseconds = 10000;
      level.silverTimeInMilliseconds = 12000;
      level.bronzeTimeInMilliseconds = 15000;

      final Trampoline trampoline1 = new Trampoline(-15.0f, -25.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline1);

      final Trampoline trampoline2 = new Trampoline(45.0f, 0.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline2);

      final Trampoline trampoline3 = new Trampoline(95.0f, 25.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline3);

      final Trampoline trampoline4 = new Trampoline(145.0f, 50.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline4);

      final Trampoline trampoline5 = new Trampoline(195.0f, 75.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline5);

      final Door door = new Door(265.0f, 110.0f, 5.0f, 5.0f);
      level.setDoor(door);
      levels.add(level);
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
