package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Client;

public class ClientConnector extends Thread
{
  final Client client;
  final MultiplayerGame game;

  public ClientConnector(final MultiplayerGame game)
  {
    client = new Client();
    this.game = game;

    client.getKryo().register(ClientPlayerInputRequest.class);
    client.getKryo().register(Message.class);
    client.getKryo().register(Message[].class);
    client.getKryo().register(PlayerMultiplayerState.class);
    client.getKryo().register(PlayerMultiplayerState[].class);
    client.getKryo().register(Response.class);
    client.getKryo().register(Request.class);
    client.getKryo().register(ServerSnapshotResponse.class);
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
      // TODO: add error message to MultiplayerGame.java
      e.printStackTrace();
      return;
    }

    game.setClient(new MClient(client, game));
  }
}
