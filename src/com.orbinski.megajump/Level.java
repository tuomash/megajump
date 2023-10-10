package com.orbinski.megajump;

class Level
{
  String name = "Level";
  Door door;
  boolean finished;
  boolean cleared;
  float elapsed;
  int millisecondsElapsed;
  int seconds;
  int milliseconds;
  int lastTimeMilliseconds = -1;
  int bestTimeMilliseconds = -1;

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
      seconds = millisecondsElapsed / 1000;
      milliseconds = millisecondsElapsed % 1000;

      UserInterface.updateElapsedTimeText(seconds, milliseconds);
    }

    if (door != null && Entity.overlaps(player, door))
    {
      finished = true;
      cleared = true;
      lastTimeMilliseconds = milliseconds;

      if (bestTimeMilliseconds < 0 || milliseconds < bestTimeMilliseconds)
      {
        bestTimeMilliseconds = milliseconds;
        UserInterface.updateBestTimeText(bestTimeMilliseconds / 1000, bestTimeMilliseconds % 1000);
      }
    }
  }

  void updateUI()
  {
    if (bestTimeMilliseconds > 0)
    {
      UserInterface.updateBestTimeText(bestTimeMilliseconds / 1000, bestTimeMilliseconds % 1000);
    }
    else
    {
      UserInterface.clearBestTimeText();
    }

    if (lastTimeMilliseconds > 0)
    {
      UserInterface.updateBestTimeText(lastTimeMilliseconds / 1000, lastTimeMilliseconds % 1000);
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
    seconds = 0;
    millisecondsElapsed = 0;
  }
}
