package com.orbinski.megajump;

class Level
{
  String name = "Level";
  Door door;
  boolean finished;
  boolean cleared;
  float elapsed;
  float lastElapsed = -1.0f;
  float bestTime = -1.0f;

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
      UserInterface.updateElapsedTimeText(elapsed);
    }

    if (door != null && Entity.overlaps(player, door))
    {
      finished = true;
      cleared = true;
      lastElapsed = elapsed;

      if (bestTime < 0.0f || elapsed < bestTime)
      {
        bestTime = elapsed;
        UserInterface.updateBestTimeText(bestTime);
      }
    }
  }

  void setDoor(final Door door)
  {
    this.door = door;
  }

  void reset()
  {
    finished = false;
    elapsed = 0.0f;
  }
}
