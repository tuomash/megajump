package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

class MListener extends Listener
{
  private final MServer server;

  MListener(final MServer server)
  {
    this.server = server;
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

    if (object instanceof ExampleRequest)
    {
      final ExampleRequest request = (ExampleRequest) object;
      System.out.println(request.text);
    }
  }

  @Override
  public void idle(final Connection connection)
  {
    super.idle(connection);
  }
}
