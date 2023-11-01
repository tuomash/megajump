package com.orbinski.megajump;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

class Level
{
  static float MAX_DIMENSION = 500.0f;
  static float MAX_DIMENSION_OFFSET = MAX_DIMENSION / 2.0f;

  enum Trophy
  {
    GOLD,
    SILVER,
    BRONZE,
    NONE
  }

  Game game;
  Player player;
  String name = "Level";
  String tag;
  Door door;
  Spawn spawn = new Spawn(5.0f, 5.0f);
  List<Decoration> decorations = new ArrayList<>();
  List<Block> blocks = new ArrayList<>();
  List<Trampoline> trampolines = new ArrayList<>();
  List<Platform> platforms = new ArrayList<>();
  List<Teleport> teleports = new ArrayList<>();

  boolean started;
  boolean finished;
  boolean cleared;
  boolean scoreUpdate;

  float elapsed;
  int millisecondsElapsed;
  int lastTimeMillisecondsElapsed = -1;
  int bestTimeMillisecondsElapsed = -1;

  Trophy trophy;
  int goldTimeInMilliseconds;
  int silverTimeInMilliseconds;
  int bronzeTimeInMilliseconds;

  boolean moveCameraX;
  boolean moveCameraY;

  float floor;

  Level()
  {
    setTrophy(Trophy.NONE);
    setSpawn(-75.0f, -30.0f);
  }

  void update(final float delta)
  {
    if (finished)
    {
      return;
    }
    else if (!started)
    {
      // Update moving doors even if the player hasn't started yet
      if (door != null)
      {
        door.update(delta);
      }

      return;
    }

    elapsed = elapsed + delta;
    millisecondsElapsed = (int) (elapsed * 1000.0f);
    lastTimeMillisecondsElapsed = millisecondsElapsed;

    UserInterface.updateElapsedTimeText(millisecondsElapsed);

    if (door != null)
    {
      door.update(delta);
    }

    if (door != null && door.overlaps(player))
    {
      player.stop();
      player.setState(Player.State.EXIT);
      UserInterface.retryText.visible = true;
      UserInterface.nextLevelText.visible = true;
      finished = true;
      cleared = true;

      if (bestTimeMillisecondsElapsed < 0 || millisecondsElapsed < bestTimeMillisecondsElapsed)
      {
        bestTimeMillisecondsElapsed = millisecondsElapsed;
        UserInterface.updateBestTimeText(bestTimeMillisecondsElapsed);
        scoreUpdate = true;
      }

      switch (trophy)
      {
        case GOLD:
        {
          // Do nothing
          break;
        }

        case SILVER:
        {
          if (millisecondsElapsed <= goldTimeInMilliseconds)
          {
            setTrophy(Trophy.GOLD);
          }

          break;
        }

        case BRONZE:
        {
          if (millisecondsElapsed <= goldTimeInMilliseconds)
          {
            setTrophy(Trophy.GOLD);
          }
          else if (millisecondsElapsed <= silverTimeInMilliseconds)
          {
            setTrophy(Trophy.SILVER);
          }

          break;
        }

        default:
        {
          if (millisecondsElapsed <= goldTimeInMilliseconds)
          {
            setTrophy(Trophy.GOLD);
          }
          else if (millisecondsElapsed <= silverTimeInMilliseconds)
          {
            setTrophy(Trophy.SILVER);
          }
          else if (millisecondsElapsed <= bronzeTimeInMilliseconds)
          {
            setTrophy(Trophy.BRONZE);
          }

          break;
        }
      }
    }
    else
    {
      player.setPosition(Player.Position.NONE);

      for (int i = 0; i < blocks.size(); i++)
      {
        final Block block = blocks.get(i);

        if (player.overlaps(block))
        {
          break;
        }
      }

      for (int i = 0; i < trampolines.size(); i++)
      {
        final Trampoline trampoline = trampolines.get(i);

        if (trampoline.apply(player))
        {
          break;
        }
      }

      for (int i = 0; i < platforms.size(); i++)
      {
        final Platform platform = platforms.get(i);

        if (player.overlaps(platform))
        {
          break;
        }
      }

      for (int i = 0; i < teleports.size(); i++)
      {
        final Teleport teleport = teleports.get(i);

        if (teleport.overlaps(player))
        {
          game.setCameraToPlayerTeleport();
          break;
        }
      }
    }
  }

  void updateUI()
  {
    if (bestTimeMillisecondsElapsed > 0)
    {
      UserInterface.updateBestTimeText(bestTimeMillisecondsElapsed);
    }
    else
    {
      UserInterface.clearBestTimeText();
    }

    if (lastTimeMillisecondsElapsed > 0)
    {
      UserInterface.updateElapsedTimeText(lastTimeMillisecondsElapsed);
    }
    else
    {
      UserInterface.clearElapsedTimeText();
    }

    UserInterface.updateLevelNameText(name);
    UserInterface.updateTrophyLevelText(trophy);
    UserInterface.updateGoldRequirementText(goldTimeInMilliseconds);
    UserInterface.updateSilverRequirementText(silverTimeInMilliseconds);
    UserInterface.updateBronzeRequirementText(bronzeTimeInMilliseconds);
  }

  void setDoor(final Door door)
  {
    this.door = door;
  }

  public void setTrophy(final Trophy trophy)
  {
    if (trophy != null)
    {
      this.trophy = trophy;
      UserInterface.updateTrophyLevelText(trophy);

      if (trophy == Trophy.GOLD || trophy == Trophy.SILVER || trophy == Trophy.BRONZE)
      {
        scoreUpdate = true;
      }
    }
  }

  Entity findEntity(final float x, final float y)
  {
    // TODO: add other entities
    // TODO: should player be in Level.java as well?

    if (door != null && door.contains(x, y))
    {
      return door;
    }

    if (spawn.contains(x, y))
    {
      return spawn;
    }

    for (int i = 0; i < decorations.size(); i++)
    {
      final Decoration decoration = decorations.get(i);

      if (decoration.contains(x, y))
      {
        return decoration;
      }
    }

    for (int i = 0; i < blocks.size(); i++)
    {
      final Block block = blocks.get(i);

      if (block.contains(x, y))
      {
        return block;
      }
    }

    for (int i = 0; i < trampolines.size(); i++)
    {
      final Trampoline trampoline = trampolines.get(i);

      if (trampoline.contains(x, y))
      {
        return trampoline;
      }
    }

    for (int i = 0; i < platforms.size(); i++)
    {
      final Platform platform = platforms.get(i);

      if (platform.contains(x, y))
      {
        return platform;
      }
    }

    for (int i = 0; i < teleports.size(); i++)
    {
      final Teleport teleport = teleports.get(i);

      if (teleport.contains(x, y))
      {
        return teleport;
      }
    }

    return null;
  }

  void setSpawn(final float x, final float y)
  {
    spawn.setX(x);
    spawn.setY(y);
  }

  void reset()
  {
    started = false;
    finished = false;
    elapsed = 0.0f;
    millisecondsElapsed = 0;

    if (door != null)
    {
      door.reset();
    }

    if (player != null)
    {
      player.setX(spawn.getX());
      player.setY(spawn.getY());
    }
  }
}
