package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Client;

public class MClient
{
  final Client client;

  public MClient(final Client client)
  {
    this.client = client;
  }

  public void shutdown()
  {
    if (client.isConnected())
    {
      client.stop();
    }
  }

  public void sendExample()
  {
    if (client.isConnected())
    {
      final ExampleRequest request = new ExampleRequest();
      request.text = "Hello world!";
      client.sendUDP(request);
    }
  }
}
