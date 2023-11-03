package com.orbinski.megajump;

import java.awt.geom.Point2D;

class Teleport extends Entity
{
  Point2D.Float target;
  Point2D.Float targetBottomLeftCorner;

  Teleport(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
  }

  Teleport(final TeleportWrapper wrapper)
  {
    super(wrapper);
  }

  boolean overlaps(final Player player)
  {
    boolean overlaps = false;

    if (EntityUtils.overlaps(player.bottomSide, this))
    {
      overlaps = true;
    }
    else if (EntityUtils.overlaps(player.topSide, this))
    {
      overlaps = true;
    }
    else if (EntityUtils.overlaps(player.leftSide, this))
    {
      overlaps = true;
    }
    else if (EntityUtils.overlaps(player.rightSide, this))
    {
      overlaps = true;
    }

    if (overlaps && target != null)
    {
      player.setX(target.x);
      player.setY(target.y);
    }

    return overlaps;
  }

  void setTarget(final float x, final float y)
  {
    if (target == null)
    {
      target = new Point2D.Float();
      targetBottomLeftCorner = new Point2D.Float();
    }

    target.x = x;
    target.y = y;
    targetBottomLeftCorner.x = x - getWidthOffset();
    targetBottomLeftCorner.y = y - getHeightOffset();
  }

  @Override
  boolean isTeleport()
  {
    return true;
  }
}
