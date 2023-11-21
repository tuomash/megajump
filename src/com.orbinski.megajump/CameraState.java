package com.orbinski.megajump;

public class CameraState
{
  private static final float SPEED = 75.0f;

  boolean moving;
  public boolean active = true;
  float velocityX;
  float velocityY;

  public void moveUp()
  {
    moving = true;
    velocityY = SPEED;
  }

  public void moveLeft()
  {
    moving = true;
    velocityX = -SPEED;
  }

  public void moveRight()
  {
    moving = true;
    velocityX = SPEED;
  }

  public void moveDown()
  {
    moving = true;
    velocityY = -SPEED;
  }

  public void reset()
  {
    moving = false;
    active = true;
    velocityX = 0.0f;
    velocityY = 0.0f;
  }
}
