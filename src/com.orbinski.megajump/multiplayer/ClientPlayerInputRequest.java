package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Connection;

public class ClientPlayerInputRequest extends Request
{
  public transient Connection connection;
  public transient int playerId;

  private float x;
  private float y;

  public float getX()
  {
    return x;
  }

  public void setX(final float x)
  {
    this.x = x;
  }

  public float getY()
  {
    return y;
  }

  public void setY(final float y)
  {
    this.y = y;
  }

  @Override
  public void reset()
  {
    super.reset();

    x = 0.0f;
    y = 0.0f;
  }
}