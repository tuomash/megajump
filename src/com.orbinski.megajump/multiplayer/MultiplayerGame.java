package com.orbinski.megajump.multiplayer;

import com.orbinski.megajump.Game;
import com.orbinski.megajump.Player;
import org.apache.commons.collections4.queue.CircularFifoQueue;

public class MultiplayerGame
{
  public final Game game;

  private MClient client;
  private ClientConnector connector;

  public boolean active;

  private final Object lock = new Object();
  private final ClientPlayerInputRequest clientInputRequest = new ClientPlayerInputRequest();
  private final CircularFifoQueue<ServerSnapshotResponse> responses = new CircularFifoQueue<>(100);

  private String spLevel;

  public MultiplayerGame(final Game game)
  {
    this.game = game;
  }

  public void update()
  {
    if (isActive())
    {
      final ServerSnapshotResponse response = getResponse();

      if (response != null)
      {
        // System.out.println("server: x " + response.getPlayerDataList()[0].x + ", y " + response.getPlayerDataList()[0].y);

        if (!game.level.getTag().equalsIgnoreCase(response.getLevelTag()))
        {
          spLevel = game.level.getTag();
          game.loadLevel(response.getLevelTag());
        }

        final Player player = game.player;

        // TODO: use player id to fetch
        final PlayerMultiplayerState state = response.getPlayerStateList()[0];

        // TODO: interpolate the positions of other players
      }
    }
  }

  private ClientPlayerInputRequest getClientInputRequest()
  {
    return clientInputRequest;
  }

  public void addResponse(final ServerSnapshotResponse response)
  {
    synchronized (lock)
    {
      if (!responses.isFull())
      {
        responses.add(response);
      }
    }
  }

  private ServerSnapshotResponse getResponse()
  {
    synchronized (lock)
    {
      if (!responses.isEmpty())
      {
        return responses.remove();
      }
    }

    return null;
  }

  public void connectToServer()
  {
    connector = new ClientConnector(this);
    connector.start();
  }

  public void setClient(final MClient client)
  {
    this.client = client;
    active = true;
  }

  public void disconnectFromServer()
  {
    if (client != null)
    {
      client.shutdown();
      client = null;
    }

    active = false;
    game.loadLevel(spLevel);
  }

  public void sendRequests()
  {
    if (isActive())
    {
      clientInputRequest.setX(game.player.getPosition().x);
      clientInputRequest.setY(game.player.getPosition().y);
      client.requests.add(clientInputRequest);
      client.sendRequests();
      clientInputRequest.reset();
    }
  }

  public boolean isActive()
  {
    return active && client != null && client.isConnected();
  }
}
