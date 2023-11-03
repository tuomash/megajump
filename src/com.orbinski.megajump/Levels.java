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

    final List<String> levelTags = new ArrayList<>();
    levelTags.add("small_jump");
    levelTags.add("medium_jump");
    levelTags.add("long_jump");
    levelTags.add("big_jump");
    levelTags.add("even_bigger_jump");
    levelTags.add("kite");
    levelTags.add("guiding_light");
    levelTags.add("it_moves");
    levelTags.add("elevator");
    levelTags.add("rectangle");
    levelTags.add("trampoline_1");
    levelTags.add("trampoline_2");
    levelTags.add("trampoline_3");
    levelTags.add("trampoline_4");
    levelTags.add("platforms_1");
    levelTags.add("on_a_pedestal");
    levelTags.add("platforms_2");
    levelTags.add("teleport_1");

    // TODO: remove manual level definions at some point
    /*
    {
      final Level level = new Level();
      level.name = "Small Jump";
      level.tag = "small_jump";
      level.goldTimeInMilliseconds = 233;
      level.silverTimeInMilliseconds = 500;
      level.bronzeTimeInMilliseconds = 1000;
      final Exit exit = new Exit(-55.0f, -30.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    /*
    {
      final Level level = new Level();
      level.name = "Medium Jump";
      level.tag = "medium_jump";
      level.goldTimeInMilliseconds = 390;
      level.silverTimeInMilliseconds = 500;
      level.bronzeTimeInMilliseconds = 1000;
      final Exit exit = new Exit(-45.0f, -20.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    /*
    {
      final Level level = new Level();
      level.name = "Long Jump";
      level.tag = "long_jump";
      level.goldTimeInMilliseconds = 600;
      level.silverTimeInMilliseconds = 700;
      level.bronzeTimeInMilliseconds = 1500;
      final Exit exit = new Exit(-25.0f, -10.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    /*
    {
      final Level level = new Level();
      level.name = "Big Jump";
      level.tag = "big_jump";
      level.goldTimeInMilliseconds = 1000;
      level.silverTimeInMilliseconds = 1200;
      level.bronzeTimeInMilliseconds = 1800;
      final Exit exit = new Exit(10.0f, 10.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    /*
    {
      final Level level = new Level();
      level.name = "Even Bigger Jump";
      level.tag = "even_bigger_jump";
      level.goldTimeInMilliseconds = 1200;
      level.silverTimeInMilliseconds = 1500;
      level.bronzeTimeInMilliseconds = 2000;
      final Exit exit = new Exit(25.0f, 25.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    /*
    {
      final Level level = new Level();
      level.name = "High as a Kite";
      level.tag = "kite";
      level.goldTimeInMilliseconds = 900;
      level.silverTimeInMilliseconds = 1200;
      level.bronzeTimeInMilliseconds = 1600;
      final Exit exit = new Exit(-75.0f, 33.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    {
      /*
      final Level level = new Level();
      level.name = "Guiding Light";
      level.tag = "guiding_light";
      level.goldTimeInMilliseconds = 3000;
      level.silverTimeInMilliseconds = 3500;
      level.bronzeTimeInMilliseconds = 4000;
      level.moveCameraX = true;

      final Exit exit = new Exit(190.0f, -30.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);

      final Decoration decoration1 = new Decoration(5.0f, 5.0f, 1.5f, 2.5f);
      level.decorations.add(decoration1);

      final Decoration decoration2 = new Decoration(55.0f, 5.0f, 1.5f, 2.5f);
      level.decorations.add(decoration2);

      final Decoration decoration3 = new Decoration(95.0f, 5.0f, 1.5f, 2.5f);
      level.decorations.add(decoration3);

      final Decoration decoration4 = new Decoration(145.0f, 5.0f, 1.5f, 2.5f);
      level.decorations.add(decoration4);
       */


    }

    /*
    {
      final Level level = new Level();
      level.name = "It Moves";
      level.tag = "it_moves";
      level.goldTimeInMilliseconds = 1000;
      level.silverTimeInMilliseconds = 1500;
      level.bronzeTimeInMilliseconds = 2000;

      final Exit exit = new Exit(-20.0f, -10.0f, 5.0f, 5.0f);
      exit.addWaypoint(new Point2D.Float(-25.0f, -10.0f));
      exit.addWaypoint(new Point2D.Float(30.0f, -10.0f));
      exit.setExitVelocityX(10.0f);
      exit.setExitVelocityY(10.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    /*
    {
      final Level level = new Level();
      level.name = "Elevator";
      level.tag = "elevator";
      level.goldTimeInMilliseconds = 750;
      level.silverTimeInMilliseconds = 1000;
      level.bronzeTimeInMilliseconds = 1500;

      final Exit exit = new Exit(-20.0f, -10.0f, 5.0f, 5.0f);
      exit.addWaypoint(new Point2D.Float(-20.0f, -10.0f));
      exit.addWaypoint(new Point2D.Float(-20.0f, 30.0f));
      exit.setExitVelocityX(10.0f);
      exit.setExitVelocityY(10.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    /*
    {
      final Level level = new Level();
      level.name = "Rectangle";
      level.tag = "rectangle";
      level.goldTimeInMilliseconds = 750;
      level.silverTimeInMilliseconds = 1000;
      level.bronzeTimeInMilliseconds = 1500;

      final Exit exit = new Exit(-20.0f, -10.0f, 5.0f, 5.0f);
      exit.addWaypoint(new Point2D.Float(-20.0f, -10.0f));
      exit.addWaypoint(new Point2D.Float(-20.0f, 20.0f));
      exit.addWaypoint(new Point2D.Float(40.0f, 20.0f));
      exit.addWaypoint(new Point2D.Float(40.0f, -10.0f));
      exit.setExitVelocityX(17.5f);
      exit.setExitVelocityY(17.5f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    /*
    {
      final Level level = new Level();
      level.name = "Trampoline 1";
      level.tag = "trampoline_1";
      level.goldTimeInMilliseconds = 2800;
      level.silverTimeInMilliseconds = 3500;
      level.bronzeTimeInMilliseconds = 5000;

      final Exit exit = new Exit(20.0f, 15.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);

      final Trampoline trampoline = new Trampoline(-15.0f, -25.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline);
    }
     */

    /*
    {
      final Level level = new Level();
      level.name = "Trampoline 2";
      level.tag = "trampoline_2";
      level.goldTimeInMilliseconds = 3200;
      level.silverTimeInMilliseconds = 3600;
      level.bronzeTimeInMilliseconds = 5000;

      final Exit exit = new Exit(40.0f, 45.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);

      final Trampoline trampoline = new Trampoline(-15.0f, -25.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline);
    }
     */

    /*
    {
      final Level level = new Level();
      level.moveCameraX = true;
      level.name = "Trampoline 3";
      level.tag = "trampoline_3";
      level.goldTimeInMilliseconds = 10000;
      level.silverTimeInMilliseconds = 12000;
      level.bronzeTimeInMilliseconds = 15000;

      final Exit exit = new Exit(145.0f, -25.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);

      final Trampoline trampoline1 = new Trampoline(-15.0f, -25.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline1);

      final Trampoline trampoline2 = new Trampoline(45.0f, -25.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline2);

      final Trampoline trampoline3 = new Trampoline(95.0f, -25.0f, 5.0f, 1.5f);
      level.trampolines.add(trampoline3);
    }
     */

    /*
    {
      final Level level = new Level();
      level.moveCameraX = true;
      level.moveCameraY = true;
      level.name = "Trampoline 4";
      level.tag = "trampoline_4";
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

      final Exit exit = new Exit(265.0f, 110.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    /*
    {
      final Level level = new Level();
      level.moveCameraY = true;
      level.name = "Platforms I";
      level.tag = "platforms_1";
      level.goldTimeInMilliseconds = 10000;
      level.silverTimeInMilliseconds = 12000;
      level.bronzeTimeInMilliseconds = 15000;

      final Platform platform1 = new Platform(40.0f, 0.0f, 40.0f, 1.5f);
      level.platforms.add(platform1);

      final Platform platform2 = new Platform(-80.0f, 20.0f, 40.0f, 1.5f);
      level.platforms.add(platform2);

      final Platform platform3 = new Platform(40.0f, 40.0f, 40.0f, 1.5f);
      level.platforms.add(platform3);

      final Platform platform4 = new Platform(-80.0f, 60.0f, 40.0f, 1.5f);
      level.platforms.add(platform4);

      final Exit exit = new Exit(-20.0f, 100.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    /*
    {
      final Level level = new Level();
      level.name = "On a Pedestal";
      level.tag = "on_a_pedestal";
      level.goldTimeInMilliseconds = 10000;
      level.silverTimeInMilliseconds = 12000;
      level.bronzeTimeInMilliseconds = 15000;

      final Platform platform1 = new Platform(-70.0f, 25.0f, 20.0f, 1.5f);
      level.platforms.add(platform1);

      final Exit exit = new Exit(-70.0f, 30.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    /*
    {
      final Level level = new Level();
      level.moveCameraX = true;
      level.moveCameraY = true;
      level.name = "Platforms II";
      level.tag = "platforms_2";
      level.goldTimeInMilliseconds = 10000;
      level.silverTimeInMilliseconds = 12000;
      level.bronzeTimeInMilliseconds = 15000;

      final Platform platform1 = new Platform(40.0f, 0.0f, 60.0f, 1.5f);
      level.platforms.add(platform1);

      final Platform platform2 = new Platform(240.0f, 20.0f, 60.0f, 1.5f);
      level.platforms.add(platform2);

      final Platform platform3 = new Platform(440.0f, 40.0f, 60.0f, 1.5f);
      level.platforms.add(platform3);

      final Platform platform4 = new Platform(640.0f, 60.0f, 60.0f, 1.5f);
      level.platforms.add(platform4);

      final Exit exit = new Exit(840.0f, 100.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    /*
    {
      final Level level = new Level();
      level.moveCameraX = true;
      level.moveCameraY = true;
      level.name = "Teleport I";
      level.tag = "teleport_1";
      level.goldTimeInMilliseconds = 10000;
      level.silverTimeInMilliseconds = 12000;
      level.bronzeTimeInMilliseconds = 15000;

      final Teleport teleport = new Teleport(0.0f, -40.0f, 5.0f, 5.0f);
      teleport.setTarget(0.0f, 200.0f);
      level.teleports.add(teleport);

      final Exit exit = new Exit(50.0f, 150.0f, 5.0f, 5.0f);
      level.setExit(exit);
      levels.add(level);
    }
     */

    for (int i = 0; i < levelTags.size(); i++)
    {
      final LevelWrapper wrapper = LevelWrapper.readFromDisk(levelTags.get(i));

      if (wrapper != null)
      {
        levels.add(wrapper.unwrap());
      }
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

  void addLevel(final Level level)
  {
    levels.add(level);
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

  void loadScores(final Save save)
  {
    for (int i = 0; i < levels.size(); i++)
    {
      final Level level = levels.get(i);

      if (level.tag != null && !level.tag.isEmpty())
      {
        if (save.scores.containsKey(level.tag))
        {
          final Score score = save.scores.get(level.tag);
          score.load(level);
        }
      }
    }
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

  Level get(final String tag)
  {
    for (int i = 0; i < levels.size(); i++)
    {
      final Level level = levels.get(i);

      if (level.tag != null && level.tag.equalsIgnoreCase(tag))
      {
        levelIndex = i;
        return level;
      }
    }

    return null;
  }
}
