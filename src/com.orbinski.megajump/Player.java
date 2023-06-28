package com.orbinski.megajump;

class Player
{
  private float x = -75.0f;
  private float y = -30.0f;
  private float gravity = -0.01f;
  private float velocityX = 0.0f;
  private float velocityY = 0.0f;
  float rx;
  float ry = y;
  float width = 5.0f;
  float widthOffset = width / 2.0f;
  float height = 5.0f;
  boolean moving;

  void update()
  {
    if (moving)
    {
      velocityY = velocityY + gravity;

      x = x + velocityX;
      y = y + velocityY;
    }

    rx = x - widthOffset;
    ry = y;
  }

  void shoot()
  {
    if (!moving)
    {
      moving = true;
      velocityX = 0.0f;
      velocityY = 0.5f;
    }
  }

  void reset()
  {
    moving = false;
    x = -75.0f;
    y = -30.0f;
    velocityX = 0.0f;
    velocityY = 0.0f;
  }
}
