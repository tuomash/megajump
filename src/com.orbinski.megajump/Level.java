package com.orbinski.megajump;

class Level
{
  Door door;
  boolean finished;

  Level()
  {
  }

  void update(final float delta, final Player player)
  {
    if (finished)
    {
      return;
    }

    if (door != null && Entity.overlaps(player, door))
    {
      finished = true;
    }
  }

  void setDoor(final Door door)
  {
    this.door = door;
  }

  void reset()
  {
    finished = false;
  }
}
