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
    clientInputRequest.moveUp = true;
  }

  public void moveLeft()
  {
    clientInputRequest.moveLeft = true;
  }

  public void moveRight()
  {
    clientInputRequest.moveRight = true;
  }

  public void moveDown()
  {
    clientInputRequest.moveDown = true;
  }

  public void jump()
  {
    // TODO: add velocities
    clientInputRequest.jump = true;
  }

  public void sendRequests()
  {
    sendClientInputRequest();
  }

  private void sendClientInputRequest()
  {
    sendUDP(clientInputRequest);
    clientInputRequest.reset();
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
