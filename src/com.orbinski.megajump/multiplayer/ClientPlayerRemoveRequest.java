package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Connection;

public class ClientPlayerRemoveRequest
{
  public transient Connection connection;
  public transient int playerId;
}
