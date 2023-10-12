package com.orbinski.megajump;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

abstract class Entity
{
  private float x;
  private float y;
  private float prevX;
  private float prevY;
  private float bottomLeftCornerX;
  private float bottomLeftCornerY;
  private float width;
  private float widthOffset;
  private float height;
  private float heightOffset;

  float gravity = -0.6f;
  boolean applyWidthOffset = true;
  boolean applyHeightOffset = true;
  float targetX;
  float targetY;
  float velocityX;
  float velocityY;
  boolean showBorder = false;
  boolean visible = true;
  boolean moving;
  boolean movingToTarget;
  boolean dead;
  Sound moveSound;
  Sound deathSound;
  Texture texture;

  Entity()
  {
    setWidth(2.5f);
    setHeight(2.5f);
  }

  Entity(final float width, final float height)
  {
    setWidth(width);
    setHeight(height);
  }

  Entity(final float x, final float y, final float width, final float height)
  {
    setWidth(width);
    setHeight(height);
    setX(x);
    setY(y);
  }

  void update(final float delta)
  {
    if (dead)
    {
      return;
    }

    if (moving)
    {
      velocityY = velocityY + gravity;

      final float distanceX = velocityX * delta;
      final float distanceY = velocityY * delta;
      setX(getX() + distanceX);
      setY(getY() + distanceY);
    }
    else if (movingToTarget)
    {
      final float distanceToTarget = MathUtils.distance(getX(), getY(), targetX, targetY);

      if (distanceToTarget < 0.5)
      {
        movingToTarget = false;
      }

      final float toTargetX = (targetX - getX()) / distanceToTarget;
      final float toTargetY = (targetY - getY()) / distanceToTarget;
      final float distanceX = this.velocityX * delta;
      final float distanceY = this.velocityY * delta;

      setX(getX() + (toTargetX * distanceX));
      setY(getY() + (toTargetY * distanceY));
    }
  }

  void moveToPreviousLocation()
  {
    setX(prevX);
    setY(prevY);
  }

  boolean overlaps(final Entity another)
  {
    return EntityUtils.overlaps(this, another);
  }

  float getX()
  {
    return x;
  }

  void setX(final float x)
  {
    prevX = this.x;
    this.x = x;

    if (applyWidthOffset)
    {
      bottomLeftCornerX = x - widthOffset;
    }
    else
    {
      bottomLeftCornerX = x;
    }
  }

  float getY()
  {
    return y;
  }

  void setY(final float y)
  {
    prevY = this.y;
    this.y = y;

    if (applyHeightOffset)
    {
      bottomLeftCornerY = y - heightOffset;
    }
    else
    {
      bottomLeftCornerY = y;
    }
  }

  float getPrevX()
  {
    return prevX;
  }

  float getPrevY()
  {
    return prevY;
  }

  float getBottomLeftCornerX()
  {
    return bottomLeftCornerX;
  }

  float getBottomLeftCornerY()
  {
    return bottomLeftCornerY;
  }

  float getWidth()
  {
    return width;
  }

  void setWidth(final float width)
  {
    this.width = width;
    widthOffset = width / 2.0f;
  }

  float getWidthOffset()
  {
    return widthOffset;
  }

  float getHeight()
  {
    return height;
  }

  void setHeight(final float height)
  {
    this.height = height;
    heightOffset = height / 2.0f;
  }

  float getHeightOffset()
  {
    return heightOffset;
  }
}
