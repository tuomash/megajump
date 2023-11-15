package com.orbinski.megajump.multiplayer;

import com.orbinski.megajump.Game;

public class MultiplayerGame
{
  public final Game game;

  public MClient client;
  ClientConnector connector;

  public boolean active;

  public MultiplayerGame(final Game game)
  {
    this.game = game;
  }
}
