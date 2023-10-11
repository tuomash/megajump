package com.orbinski.megajump;

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
    super(-75.0f, -30.0f, 2.5f, 5.0f);
  }

  void update(final float delta)
  {
    super.update(delta);
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
  }

  void moveRight()
  {
    velocityX = velocityX + 0.5f;
  }

  void moveDown()
  {
    velocityY = velocityY - 0.5f;
  }

  void reset()
  {
    moving = false;
    targeting = false;
    setX(-75.0f);
    setY(-30.0f);
    velocityX = 0.0f;
    velocityY = 0.0f;
  }
}
