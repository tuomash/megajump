package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Client;
import com.orbinski.megajump.Game;

public class ClientConnector extends Thread
{
  final Client client;
  final Game game;

  public ClientConnector(final Game game)
  {
    client = new Client();
    this.game = game;
  }

  @Override
  public void run()
  {
    try
    {
      client.start();
      client.connect(5000, "127.0.0.1", 54555, 54777);

      client.getKryo().register(ClientInputRequest.class);
      client.getKryo().register(ExampleRequest.class);
    }
    catch (final Exception e)
    {
      e.printStackTrace();
      return;
    }

    game.setClient(new MClient(client));
  }
}
