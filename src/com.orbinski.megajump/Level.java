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
  Door door;
  List<Decoration> decorations = new ArrayList<>();
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

  boolean moveCamera;

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

    if (player.moving)
    {
      elapsed = elapsed + delta;
      millisecondsElapsed = (int) (elapsed * 1000.0f);
      lastTimeMillisecondsElapsed = millisecondsElapsed;

      UserInterface.updateElapsedTimeText(millisecondsElapsed / 1000, millisecondsElapsed % 1000);
    }

    if (door != null && door.type == Door.Type.MOVING)
    {
      door.update(delta);
    }

    if (door != null && Entity.overlaps(player, door))
    {
      player.moving = false;
      UserInterface.retryText.visible = true;
      UserInterface.nextLevelText.visible = true;
      finished = true;
      cleared = true;

      if (bestTimeMillisecondsElapsed < 0 || millisecondsElapsed < bestTimeMillisecondsElapsed)
      {
        bestTimeMillisecondsElapsed = millisecondsElapsed;
        UserInterface.updateBestTimeText(bestTimeMillisecondsElapsed / 1000, bestTimeMillisecondsElapsed % 1000);
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
  }

  void updateUI()
  {
    if (bestTimeMillisecondsElapsed > 0)
    {
      UserInterface.updateBestTimeText(bestTimeMillisecondsElapsed / 1000, bestTimeMillisecondsElapsed % 1000);
    }
    else
    {
      UserInterface.clearBestTimeText();
    }

    if (lastTimeMillisecondsElapsed > 0)
    {
      UserInterface.updateElapsedTimeText(lastTimeMillisecondsElapsed / 1000, lastTimeMillisecondsElapsed % 1000);
    }
    else
    {
      UserInterface.clearElapsedTimeText();
    }

    UserInterface.updateLevelNameText(name);
    UserInterface.updateTrophyLevelText(trophy);
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
    finished = false;
    elapsed = 0.0f;
    millisecondsElapsed = 0;

    if (door != null)
    {
      door.reset();
    }
  }
}
