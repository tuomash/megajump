package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientListener extends Listener
{
  private final MClient client;

  public ClientListener(final MClient client)
  {
    this.client = client;
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
      System.out.println("server: x " + response.getPlayerDataList()[0].x + ", y " + response.getPlayerDataList()[0].y);
    }
  }

  @Override
  public void idle(final Connection connection)
  {
    super.idle(connection);
  }
}
