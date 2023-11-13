package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class MClient
{
  Client client;
  boolean running;

  public void start()
  {
    client = new Client();
    client.start();

    try
    {
      client.connect(5000, "127.0.0.1", 54555, 54777);
      running = true;

      client.getKryo().register(ExampleRequest.class);
    }
    catch (final IOException e)
    {
      client = null;
      running = false;
      throw new RuntimeException(e);
    }
  }

  public void stop()
  {
    if (client != null)
    {
      client.stop();
    }

    running = false;
  }

  public void sendExample()
  {
    if (running && client != null)
    {
      final ExampleRequest request = new ExampleRequest();
      request.text = "Hello world!";
      client.sendUDP(request);
    }
  }
}
