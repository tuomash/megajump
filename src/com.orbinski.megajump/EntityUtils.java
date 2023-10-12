package com.orbinski.megajump;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

class EntityUtils
{
  private static final Rectangle entityA = new Rectangle();
  private static final Rectangle entityB = new Rectangle();

  static boolean overlaps(final Entity a, final Entity b)
  {
    if (a == null || b == null)
    {
      return false;
    }

    entityA.x = a.getBottomLeftCornerX();
    entityA.y = a.getBottomLeftCornerY();
    entityA.width = a.getWidth();
    entityA.height = a.getHeight();

    entityB.x = b.getBottomLeftCornerX();
    entityB.y = b.getBottomLeftCornerY();
    entityB.width = b.getWidth();
    entityB.height = b.getHeight();

    return entityA.overlaps(entityB);
  }

  static boolean contains(final Entity a, final float x, final float y)
  {
    if (a == null)
    {
      return false;
    }

    entityA.x = a.getBottomLeftCornerX();
    entityA.y = a.getBottomLeftCornerY();
    entityA.width = a.getWidth();
    entityA.height = a.getHeight();

    return entityA.contains(x, y);
  }

  static boolean overlaps(final Circle circle, final Entity a)
  {
    if (circle == null || a == null)
    {
      return false;
    }

    entityA.x = a.getBottomLeftCornerX();
    entityA.y = a.getBottomLeftCornerY();
    entityA.width = a.getWidth();
    entityA.height = a.getHeight();

    return Intersector.overlaps(circle, entityA);
  }

  static boolean overlaps(final Rectangle rectangle, final Entity a)
  {
    if (rectangle == null || a == null)
    {
      return false;
    }

    entityA.x = a.getBottomLeftCornerX();
    entityA.y = a.getBottomLeftCornerY();
    entityA.width = a.getWidth();
    entityA.height = a.getHeight();

    return Intersector.overlaps(rectangle, entityA);
  }
}
