package com.orbinski.megajump;

import com.badlogic.gdx.math.Vector3;

class Player
{
  float x = -75.0f;
  float y = -30.0f;
  float gravity = -0.01f;
  float maxVelocityX = 2.0f;
  float maxVelocityY = 2.0f;
  float velocityX = 0.0f;
  float velocityY = 0.0f;
  float topLeftCornerX;
  float topLeftCornerY;
  float width = 5.0f;
  float widthOffset = width / 2.0f;
  float height = 5.0f;
  float heightOffset = height / 2.0f;
  float centerX;
  float centerY;
  float centerWidth = 1.0f;
  float centerHeight = 1.0f;
  float centerWidthOffset = centerWidth / 2.0f;
  float centerHeightOffset = centerHeight / 2.0f;
  Vector3 mouseScreen = new Vector3();
  Vector3 mouseWorld = new Vector3();
  float cursorWidth = 0.5f;
  float cursorHeight = 0.5f;
  boolean moving;
  boolean targeting;

  void update()
  {
    if (moving)
    {
      velocityY = velocityY + gravity;

      x = x + velocityX;
      y = y + velocityY;
    }

    topLeftCornerX = x - widthOffset;
    topLeftCornerY = y - heightOffset;
    centerX = x - centerWidthOffset;
    centerY = y - centerHeightOffset;

    final Vector3 result = Renderer.unproject(mouseScreen);

    if (result != null)
    {
      mouseWorld = result;
    }
  }

  void shoot()
  {
    if (!moving)
    {
      moving = true;

      final float maxDiffX = 50.0f;
      final float mouseWorldX = mouseWorld.x;
      final float playerWorldX = x;
      float diffX = Math.abs(mouseWorldX - playerWorldX);

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

      if (mouseWorldX < x)
      {
        velocityX = -velocityX;
      }

      final float maxDiffY = 50.0f;
      final float mouseWorldY = mouseWorld.y;
      final float playerWorldY = y;
      float diffY = Math.abs(mouseWorldY - playerWorldY);

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

      if (mouseWorldY < y)
      {
        velocityY = -velocityY;
      }
    }
  }

  void reset()
  {
    moving = false;
    targeting = false;
    x = -75.0f;
    y = -30.0f;
    velocityX = 0.0f;
    velocityY = 0.0f;
  }
}
