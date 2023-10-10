package com.orbinski.megajump;

class Level
{
  String name = "Level";
  Door door;
  boolean finished;
  boolean cleared;
  float elapsed;
  int millisecondsElapsed;
  int lastTimeMillisecondsElapsed = -1;
  int bestTimeMillisecondsElapsed = -1;

  Level()
  {
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

    if (door != null && Entity.overlaps(player, door))
    {
      player.moving = false;
      finished = true;
      cleared = true;

      if (bestTimeMillisecondsElapsed < 0 || millisecondsElapsed < bestTimeMillisecondsElapsed)
      {
        bestTimeMillisecondsElapsed = millisecondsElapsed;
        UserInterface.updateBestTimeText(bestTimeMillisecondsElapsed / 1000, bestTimeMillisecondsElapsed % 1000);
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
  }

  void setDoor(final Door door)
  {
    this.door = door;
  }

  void reset()
  {
    finished = false;
    elapsed = 0.0f;
    millisecondsElapsed = 0;
  }
}
