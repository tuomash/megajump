package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener extends Listener
{
  private final MultiplayerGame game;

  public ClientListener(final MultiplayerGame game)
  {
    this.game = game;
  }

  @Override
  public void connected(final Connection connection)
  {
    super.connected(connection);
  }

  @Override
  public void disconnected(final Connection connection)
  {
    super.disconnected(connection);
  }

  @Override
  public void received(final Connection connection, final Object object)
  {
    super.received(connection, object);

    if (object instanceof ServerSnapshotResponse)
    {
      final ServerSnapshotResponse response = (ServerSnapshotResponse) object;
      game.responses.add(response);
    }
  }

  @Override
  public void idle(final Connection connection)
  {
    super.idle(connection);
  }
}
