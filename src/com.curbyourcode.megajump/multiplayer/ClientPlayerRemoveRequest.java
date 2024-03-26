package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Connection;

public class ClientPlayerRemoveRequest extends Request
{
  public transient Connection connection;
  public transient int playerId;
}
