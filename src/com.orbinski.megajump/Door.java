package com.orbinski.megajump;

class Door extends Entity
{
  Door(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
    texture = Resources.door;
  }

  @Override
  boolean isDoor()
  {
    return true;
  }

  boolean overlaps(final Player player)
  {
    if (EntityUtils.overlaps(player.bottomSide, this))
    {
      return true;
    }
    else if (EntityUtils.overlaps(player.topSide, this))
    {
      return true;
    }
    else if (EntityUtils.overlaps(player.leftSide, this))
    {
      return true;
    }
    else if (EntityUtils.overlaps(player.rightSide, this))
    {
      return true;
    }

    return false;
  }
}
