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

    if (wrapper.target != null)
    {
      setTarget(wrapper.target.x, wrapper.target.y);
    }
  }

  boolean overlaps(final Player player)
  {
    if (EntityUtils.overlaps(player, this) && target != null)
    {
      player.setPosition(target.x, target.y);
      return true;
    }

    return false;
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
