package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Server;

public class MServer extends Thread
{
  final Server server;
  final MListener listener;

  boolean running;
  boolean shutdownRequest;

  public MServer()
  {
    server = new Server();
    listener = new MListener(this);
    server.addListener(listener);
  }

  @Override
  public synchronized void start()
  {
    try
    {
      server.start();
      server.bind(54555, 54777);
      running = true;

      server.getKryo().register(ExampleRequest.class);

      super.start();
    }
    catch (final Exception e)
    {
      running = false;
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
        interrupt();
      }
    }

    server.stop();
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
