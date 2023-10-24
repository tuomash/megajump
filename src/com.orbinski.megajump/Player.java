package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Texture;

class Player extends Entity
{
  final float maxVelocityX = 70.0f;
  final float maxVelocityY = 80.0f;
  final float cursorWidth = 0.5f;
  final float cursorHeight = 0.5f;

  float cursorX;
  float cursorY;
  boolean targeting;

  Player()
  {
    super(-75.0f, -30.0f, 10.0f, 10.0f);

    movement = Movement.REGULAR;
    // drawBorder = true;
    // drawCollisions = true;

    rightSide.width = 0.5f;
    rightSide.height = getHeight() * 0.75f;

    bottomSide.width = getWidth() * 0.8f;
    bottomSide.height = 0.5f;

    animation = Animations.playerIdleRight;
  }

  void update(final float delta)
  {
    super.update(delta);

    if (animation != null)
    {
      animation.update(delta);
      texture = animation.getFrame().texture;
    }
  }

  void updateCursorLocation(final float x, final float y)
  {
    this.cursorX = x;
    this.cursorY = y;
  }

  void jump()
  {
    if (!moving)
    {
      moving = true;
      UserInterface.retryText.visible = false;

      final float maxDiffX = 40.0f;
      final float playerWorldX = getX();
      float diffX = Math.abs(cursorX - playerWorldX);

      if (diffX > maxDiffX)
      {
        diffX = maxDiffX;
      }

      final float percentageX = diffX / maxDiffX;
      velocityX = maxVelocityX * percentageX;

      if (velocityX > maxVelocityX)
      {
        velocityX = maxVelocityX;
      }

      if (cursorX < getX())
      {
        velocityX = -velocityX;
      }

      final float maxDiffY = 40.0f;
      final float playerWorldY = getY();
      float diffY = Math.abs(cursorY - playerWorldY);

      if (diffY > maxDiffY)
      {
        diffY = maxDiffY;
      }

      final float percentageY = diffY / maxDiffY;
      velocityY = maxVelocityY * percentageY;

      if (velocityY > maxVelocityY)
      {
        velocityY = maxVelocityY;
      }

      if (cursorY < getY())
      {
        velocityY = -velocityY;
      }
    }
  }

  void moveUp()
  {
    velocityY = velocityY + 0.5f;
  }

  void moveLeft()
  {
    velocityX = velocityX - 0.5f;
    texture = Resources.dwarfLeft;
  }

  void moveRight()
  {
    velocityX = velocityX + 0.5f;
    texture = Resources.dwarfRight;
  }

  void moveDown()
  {
    velocityY = velocityY - 0.5f;
  }

  @Override
  boolean overlaps(final Entity entity)
  {
    if (entity.isDoor())
    {
      return super.overlaps(entity);
    }
    else if (entity.isBlock())
    {
      moveToPreviousLocation();
      return super.overlaps(entity);

      /* TODO: implement proper collision detection
      boolean overlaps = false;

      if (!rightSideCollision && EntityUtils.overlaps(rightSide, entity))
      {
        setX(entity.getBottomLeftCornerX() - getWidthOffset());
        velocityX = 0.0f;
        overlaps = true;
        rightSideCollision = true;
      }
      else if (!bottomSideCollision && EntityUtils.overlaps(bottomSide, entity))
      {
        setY(entity.getY() + entity.getHeightOffset() + getHeightOffset());
        velocityX = 0.0f;
        velocityY = 0.0f;
        moving = false;
        overlaps = true;
        applyGravity = false;
        bottomSideCollision = true;
      }

      return overlaps;
       */
    }

    return false;
  }

  @Override
  void setX(final float x)
  {
    super.setX(x);
    rightSide.x = x + getWidthOffset() - rightSide.width;
    bottomSide.x = x - bottomSide.width * 0.5f;
  }

  @Override
  void setY(final float y)
  {
    super.setY(y);
    rightSide.y = y - rightSide.height * 0.5f;
    bottomSide.y = getBottomLeftCornerY();
  }

  @Override
  void setWidth(final float width)
  {
    super.setWidth(width);
    bottomSide.width = width * 0.5f;
  }

  @Override
  void setHeight(final float height)
  {
    super.setHeight(height);
    rightSide.height = height * 0.5f;
  }

  void reset()
  {
    moving = false;
    targeting = false;
    setX(-75.0f);
    setY(-30.0f);
    velocityX = 0.0f;
    velocityY = 0.0f;
    animation = Animations.playerIdleRight;
  }
}
