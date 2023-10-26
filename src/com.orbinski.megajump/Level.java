package com.orbinski.megajump;

import java.util.ArrayList;
import java.util.List;

class Level
{
  enum Trophy
  {
    GOLD,
    SILVER,
    BRONZE,
    NONE
  }

  String name = "Level";
  String tag;
  Door door;
  List<Decoration> decorations = new ArrayList<>();
  List<Block> blocks = new ArrayList<>();
  List<Trampoline> trampolines = new ArrayList<>();
  List<Platform> platforms = new ArrayList<>();
  boolean started;
  boolean finished;
  boolean cleared;

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
  }

  void update(final float delta, final Player player)
  {
    if (finished)
    {
      return;
    }

    if (started)
    {
      elapsed = elapsed + delta;
      millisecondsElapsed = (int) (elapsed * 1000.0f);
      lastTimeMillisecondsElapsed = millisecondsElapsed;

      UserInterface.updateElapsedTimeText(millisecondsElapsed);
    }

    if (door != null)
    {
      door.update(delta);
    }

    if (door != null && player.overlaps(door))
    {
      player.moving = false;
      UserInterface.retryText.visible = true;
      UserInterface.nextLevelText.visible = true;
      finished = true;
      cleared = true;

      if (bestTimeMillisecondsElapsed < 0 || millisecondsElapsed < bestTimeMillisecondsElapsed)
      {
        bestTimeMillisecondsElapsed = millisecondsElapsed;
        UserInterface.updateBestTimeText(bestTimeMillisecondsElapsed);
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
      player.clearCollisionStatus();

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
    }
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
  }
}
