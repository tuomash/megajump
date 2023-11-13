package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class MServer extends Thread
{
  MListener listener;
  Server server;
  boolean running;
  boolean shutdownRequest;

  @Override
  public synchronized void start()
  {
    server = new Server();
    server.start();

    try
    {
      server.bind(54555, 54777);
      running = true;
      listener = new MListener(this);
      server.addListener(listener);

      server.getKryo().register(ExampleRequest.class);
      super.start();
    }
    catch (final IOException e)
    {
      server = null;
      running = false;
      listener = null;
      throw new RuntimeException(e);
    }
  }

  @Override
  public void run()
  {
    while (running)
    {
      if (shutdownRequest)
      {
        running = false;
        break;
      }

      try
      {
        Thread.sleep(50L);
      }
      catch (final Exception e)
      {
        e.printStackTrace();
        shutdownRequest = true;
      }
    }

    if (server != null)
    {
      server.stop();
      server = null;
      listener = null;
    }

    running = false;
    shutdownRequest = false;
  }

  @Override
  public void interrupt()
  {
    super.interrupt();
    shutdownRequest = true;
  }
}
