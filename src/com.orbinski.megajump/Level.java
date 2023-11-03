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

  private String name = "Level";
  private String tag;
  Exit exit;
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

  Point2D.Float cameraFloor = new Point2D.Float();
  Point2D.Float deathPoint = new Point2D.Float();

  private boolean saved = true;

  Level()
  {
    setTrophy(Trophy.NONE);
    setExit(new Exit(-55.0f, -30.0f, 5.0f, 5.0f));
    setSpawn(-75.0f, -30.0f);
    cameraFloor.setLocation(0.0f, 15.0f);
    deathPoint.setLocation(0.0f, -70.0f);
  }

  void update(final float delta)
  {
    if (finished)
    {
      return;
    }
    else if (!started)
    {
      // Update moving exits even if the player hasn't started yet
      if (exit != null)
      {
        exit.update(delta);
      }

      return;
    }

    elapsed = elapsed + delta;
    millisecondsElapsed = (int) (elapsed * 1000.0f);
    lastTimeMillisecondsElapsed = millisecondsElapsed;

    UserInterface.updateElapsedTimeText(millisecondsElapsed);

    if (exit != null)
    {
      exit.update(delta);
    }

    if (exit != null && exit.overlaps(player))
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

  void setExit(final Exit exit)
  {
    this.exit = exit;
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
    if (exit != null && exit.contains(x, y))
    {
      return exit;
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

  boolean overlapsEntity(final Entity entity)
  {
    if (exit != null && exit.overlaps(entity))
    {
      return true;
    }

    if (spawn.overlaps(entity))
    {
      return true;
    }

    for (int i = 0; i < blocks.size(); i++)
    {
      final Block block = blocks.get(i);

      if (block.overlaps(entity))
      {
        return true;
      }
    }

    for (int i = 0; i < trampolines.size(); i++)
    {
      final Trampoline trampoline = trampolines.get(i);

      if (trampoline.overlaps(entity))
      {
        return true;
      }
    }

    for (int i = 0; i < platforms.size(); i++)
    {
      final Platform platform = platforms.get(i);

      if (platform.overlaps(entity))
      {
        return true;
      }
    }

    for (int i = 0; i < teleports.size(); i++)
    {
      final Teleport teleport = teleports.get(i);

      if (teleport.overlaps(entity))
      {
        return true;
      }
    }

    return false;
  }

  boolean removeEntity(final Entity entity)
  {
    // Exit or spawn cannot be removed
    if (entity.isExit() || entity.isSpawn())
    {
      return false;
    }

    boolean removed = false;

    if (entity.isDecoration())
    {
      removed = decorations.remove(entity);
    }
    else if (entity.isBlock())
    {
      removed = blocks.remove(entity);
    }
    else if (entity.isTrampoline())
    {
      removed = trampolines.remove(entity);
    }
    else if (entity.isPlatform())
    {
      removed = platforms.remove(entity);
    }
    else if (entity.isTeleport())
    {
      removed = teleports.remove(entity);
    }

    if (removed)
    {
      setSaved(false);
    }

    return removed;
  }

  void raiseCameraFloor(final float amount)
  {
    cameraFloor.y = cameraFloor.y  + amount;
    setSaved(false);
  }

  void lowerCameraFloor(final float amount)
  {
    cameraFloor.y = cameraFloor.y - amount;
    setSaved(false);
  }

  void raiseDeathPoint(final float amount)
  {
    deathPoint.y = deathPoint.y  + amount;
    setSaved(false);
  }

  void lowerDeathPoint(final float amount)
  {
    deathPoint.y = deathPoint.y - amount;
    setSaved(false);
  }

  void setSpawn(final float x, final float y)
  {
    spawn.setX(x);
    spawn.setY(y);
  }

  String getName()
  {
    return name;
  }

  void setName(String name)
  {
    if (name == null || name.isEmpty())
    {
      name = "New Level " + System.currentTimeMillis();
    }

    if (name.length() > 32)
    {
      name = name.substring(0, 32);
    }

    final StringBuilder nameBuilder = new StringBuilder();
    final StringBuilder tagBuilder = new StringBuilder();

    for (int i = 0; i < name.length(); i++)
    {
      final char c = name.charAt(i);

      if (Character.isAlphabetic(c) || Character.isDigit(c))
      {
        nameBuilder.append(c);
        tagBuilder.append(c);
      }
      else if (Character.isSpaceChar(c))
      {
        nameBuilder.append(c);
        tagBuilder.append('_');
      }
    }

    this.name = nameBuilder.toString();
    setTag(tagBuilder.toString());
  }

  String getTag()
  {
    return tag;
  }

  void setTag(final String tag)
  {
    if (tag != null && !tag.isEmpty())
    {
      this.tag = tag.toLowerCase();
    }
  }

  boolean isSaved()
  {
    return saved;
  }

  void setSaved(final boolean saved)
  {
    this.saved = saved;

    if (saved)
    {
      UserInterface.unsavedChangesText.visible = false;
    }
    else
    {
      UserInterface.unsavedChangesText.visible = true;
    }
  }

  void reset()
  {
    started = false;
    finished = false;
    elapsed = 0.0f;
    millisecondsElapsed = 0;

    if (exit != null)
    {
      exit.reset();
    }

    if (player != null)
    {
      player.setX(spawn.getX());
      player.setY(spawn.getY());
    }
  }
}
