package com.orbinski.megajump;

import com.badlogic.gdx.math.Vector3;

class Player extends Entity
{
  final float maxVelocityX = 70.0f;
  final float maxVelocityY = 70.0f;
  private final Vector3 mouseScreen = new Vector3();

  Vector3 mouseWorld = new Vector3();
  float cursorWidth = 0.5f;
  float cursorHeight = 0.5f;
  boolean targeting;

  Player()
  {
    super(-75.0f, -30.0f, 2.5f, 5.0f);
  }

  void update(final float delta)
  {
    super.update(delta);
  }

  void updateMouseLocation(final int x, final int y)
  {
    mouseScreen.x = x;
    mouseScreen.y = y;
    final Vector3 result = Renderer.unproject(mouseScreen);

    if (result != null)
    {
      mouseWorld = result;
    }
  }

  void jump()
  {
    if (!moving)
    {
      moving = true;

      final float maxDiffX = 40.0f;
      final float mouseWorldX = mouseWorld.x;
      final float playerWorldX = getX();
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

      if (mouseWorldX < getX())
      {
        velocityX = -velocityX;
      }

      final float maxDiffY = 40.0f;
      final float mouseWorldY = mouseWorld.y;
      final float playerWorldY = getY();
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

      if (mouseWorldY < getY())
      {
        velocityY = -velocityY;
      }
    }
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
