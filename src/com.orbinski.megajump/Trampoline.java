package com.orbinski.megajump;

class Trampoline extends Entity
{
  Trampoline(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
  }

  boolean apply(final Player player)
  {
    if (EntityUtils.overlaps(player.bottomSide, this))
    {
      player.moveToPreviousLocation();
      player.velocityY = (Math.abs(player.velocityY) + 50.0f) * 0.60f;

      return true;
    }

    return false;
  }
}
