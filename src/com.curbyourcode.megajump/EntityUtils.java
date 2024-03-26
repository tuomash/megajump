package com.orbinski.megajump;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

class EntityUtils
{
  private static final Rectangle rect1 = new Rectangle();
  private static final Rectangle rect2 = new Rectangle();

  static boolean overlaps(final Entity a, final Entity b)
  {
    if (a == null || b == null)
    {
      return false;
    }

    if (a.collisionBox != null && b.collisionBox != null)
    {
      return overlaps(a.collisionBox, b.collisionBox);
    }
    else if (a.collisionBox != null)
    {
      return overlaps(a.collisionBox, b);
    }
    else if (b.collisionBox != null)
    {
      return overlaps(b.collisionBox, a);
    }

    rect1.x = a.getBottomLeftCornerPosition().x;
    rect1.y = a.getBottomLeftCornerPosition().y;
    rect1.width = a.getWidth();
    rect1.height = a.getHeight();

    rect2.x = b.getBottomLeftCornerPosition().x;
    rect2.y = b.getBottomLeftCornerPosition().y;
    rect2.width = b.getWidth();
    rect2.height = b.getHeight();

    return rect1.overlaps(rect2);
  }

  static boolean contains(final Entity a, final float x, final float y)
  {
    if (a == null)
    {
      return false;
    }

    rect1.x = a.getBottomLeftCornerPosition().x;
    rect1.y = a.getBottomLeftCornerPosition().y;
    rect1.width = a.getWidth();
    rect1.height = a.getHeight();

    return rect1.contains(x, y);
  }

  static boolean overlaps(final Circle circle, final Entity a)
  {
    if (circle == null || a == null)
    {
      return false;
    }

    rect1.x = a.getBottomLeftCornerPosition().x;
    rect1.y = a.getBottomLeftCornerPosition().y;
    rect1.width = a.getWidth();
    rect1.height = a.getHeight();

    return Intersector.overlaps(circle, rect1);
  }

  static boolean overlaps(final Rectangle rectangle, final Entity a)
  {
    if (rectangle == null || a == null)
    {
      return false;
    }

    rect1.x = a.getBottomLeftCornerPosition().x;
    rect1.y = a.getBottomLeftCornerPosition().y;
    rect1.width = a.getWidth();
    rect1.height = a.getHeight();

    return overlaps(rectangle, rect1);
  }

  static boolean overlaps(final Rectangle rectangle1, final Rectangle rectangle2)
  {
    if (rectangle1 == null || rectangle2 == null)
    {
      return false;
    }

    return Intersector.overlaps(rectangle1, rectangle2);
  }
}
