package com.orbinski.megajump;

class Door extends Entity
{
  enum Type
  {
    STATIC,
    MOVING
  }

  Type type = Type.STATIC;
  boolean movingToEnd;
  Float startX;
  Float startY;
  Float endX;
  Float endY;

  Door(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
  }

  @Override
  void update(final float delta)
  {
    super.update(delta);

    if (type == Type.MOVING && !movingToTarget)
    {
      movingToEnd = !movingToEnd;

      if (movingToEnd)
      {
        if (endX != null)
        {
          targetX = endX;
        }

        if (endY != null)
        {
          targetY = endY;
        }
      }
      else
      {
        if (startX != null)
        {
          targetX = startX;
        }

        if (startY != null)
        {
          targetY = startY;
        }
      }

      targetY = getY();
      movingToTarget = true;
    }
  }

  void reset()
  {
    if (type == Type.MOVING)
    {
      movingToEnd = false;
      movingToTarget = false;

      if (startX != null)
      {
        setX(startX);
      }

      if (startY != null)
      {
        setY(startY);
      }
    }
  }
}
