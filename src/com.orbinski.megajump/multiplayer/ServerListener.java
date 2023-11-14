package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

class ServerListener extends Listener
{
  private final MServer server;

  public ServerListener(final MServer server)
  {
    this.server = server;
  }

  @Override
  public void connected(final Connection connection)
  {
    super.connected(connection);

    final ClientPlayerAddRequest request = new ClientPlayerAddRequest();
    request.connection = connection;
    request.playerId = connection.getID();
    server.addClientPlayerAddRequest(request);
  }

  @Override
  public void disconnected(final Connection connection)
  {
    super.disconnected(connection);

    final ClientPlayerRemoveRequest request = new ClientPlayerRemoveRequest();
    request.connection = connection;
    request.playerId = connection.getID();
    server.addClientPlayerRemoveRequest(request);
  }

  @Override
  public void received(final Connection connection, final Object object)
  {
    super.received(connection, object);

    if (object instanceof ExampleRequest)
    {
      final ExampleRequest request = (ExampleRequest) object;
      // System.out.println(request.text);
    }
    else if (object instanceof ClientPlayerInputRequest)
    {
      final ClientPlayerInputRequest request = (ClientPlayerInputRequest) object;
      request.connection = connection;
      request.playerId = connection.getID();
      server.addClientPlayerInputRequest(request);
    }
  }

  @Override
  public void idle(final Connection connection)
  {
    super.idle(connection);
    // TODO: disconnect if player idles for 5 minutes
  }
}
