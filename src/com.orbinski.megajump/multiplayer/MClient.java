package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Client;

public class MClient
{
  final Client client;
  final ClientListener listener;

  private final ClientPlayerInputRequest clientInputRequest = new ClientPlayerInputRequest();

  public MClient(final Client client)
  {
    this.client = client;
    listener = new ClientListener(this);
    client.addListener(listener);
  }

  public void moveUp()
  {
    clientInputRequest.enableMoveUp();
  }

  public void moveLeft()
  {
    clientInputRequest.enableMoveLeft();
  }

  public void moveRight()
  {
    clientInputRequest.enableMoveRight();
  }

  public void moveDown()
  {
    clientInputRequest.enableMoveDown();
  }

  public void jump()
  {
    // TODO: add velocities
    clientInputRequest.enableJump(0.0f, 0.0f);
  }

  public void sendRequests()
  {
    sendClientInputRequest();
  }

  private void sendClientInputRequest()
  {
    if (clientInputRequest.dirty)
    {
      send(clientInputRequest);
    }
  }

  private void send(final Request request)
  {
    if (request.type ==  Request.Type.TCP)
    {
      sendTCP(request);
    }
    else
    {
      sendUDP(request);
    }

    request.reset();
  }

  private void sendTCP(final Object request)
  {
    if (client.isConnected())
    {
      client.sendTCP(request);
    }
  }

  private void sendUDP(final Object request)
  {
    if (client.isConnected())
    {
      client.sendUDP(request);
    }
  }

  public void shutdown()
  {
    if (client.isConnected())
    {
      client.stop();
    }

    client.close();
  }
}
