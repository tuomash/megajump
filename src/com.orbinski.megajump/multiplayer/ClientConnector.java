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

    // TODO: move under MClient.java?
    client.getKryo().register(int[].class);
    client.getKryo().register(ClientPlayerAddRequest.class);
    client.getKryo().register(ClientPlayerInputRequest.class);
    client.getKryo().register(ClientPlayerRemoveRequest.class);
    client.getKryo().register(ExampleRequest.class);
    client.getKryo().register(PlayerData.class);
    client.getKryo().register(PlayerData[].class);
    client.getKryo().register(Response.class);
    client.getKryo().register(Request.class);
    client.getKryo().register(ServerSnapshotResponse.class);

    this.game = game;
  }

  @Override
  public void run()
  {
    try
    {
      client.start();
      client.connect(5000, "127.0.0.1", 54555, 54777);
    }
    catch (final Exception e)
    {
      e.printStackTrace();
      return;
    }

    game.setClient(new MClient(client));
  }
}
