package com.orbinski.megajump;

class CameraState
{
  private static final float SPEED = 75.0f;

  boolean moving;
  boolean active = true;
  float velocityX;
  float velocityY;

  void moveUp()
  {
    moving = true;
    velocityY = SPEED;
  }

  void moveLeft()
  {
    moving = true;
    velocityX = -SPEED;
  }

  void moveRight()
  {
    moving = true;
    velocityX = SPEED;
  }

  void moveDown()
  {
    moving = true;
    velocityY = -SPEED;
  }

  void reset()
  {
    moving = false;
    active = true;
    velocityX = 0.0f;
    velocityY = 0.0f;
  }
}
