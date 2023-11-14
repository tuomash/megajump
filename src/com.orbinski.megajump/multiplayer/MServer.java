package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Server;
import com.orbinski.megajump.Globals;
import com.orbinski.megajump.Physics;

public class MServer extends Thread
{
  final Server server;
  final MListener listener;
  final Physics physics;

  boolean running;
  boolean shutdownRequest;

  public MServer()
  {
    server = new Server();
    listener = new MListener(this);
    server.addListener(listener);
    physics = new Physics();
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
      try
      {
        if (shutdownRequest)
        {
          running = false;
          break;
        }

        /*
        var begin = current_time();
        receive_from_clients(); // poll, accept, receive, decode, validate
        x update(); // AI, simulate
        send_updates_clients();
        var elapsed = current_time() - begin;
        if(elapsed < tick)
        {
          sleep(tick - elapsed);
        }
         */

        physics.update(Globals.TIME_STEP_SECONDS);

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
