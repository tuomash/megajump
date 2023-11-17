package com.orbinski.megajump.multiplayer;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.orbinski.megajump.Player;

public class PlayerMultiplayerState
{
  public transient Connection connection;
  public transient Player player;
  public transient boolean isAtExit;
  public transient String levelTag;
  public transient boolean ai = false;

  public int playerId;
  public String playerName;
  private float x;
  private float y;
  public String playerState;

  public float getX()
  {
    return x;
  }

  public float getY()
  {
    return y;
  }

  public void setPosition(final Vector2 position)
  {
    setPosition(position.x, position.y);
  }

  public void setPosition(final float x, final float y)
  {
    this.x = x;
    this.y = y;
    player.setPosition(x, y, false);
  }
}
