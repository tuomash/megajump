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

  private int requestId = 1;
  private final Object lock = new Object();
  private final CircularFifoQueue<ClientPlayerInputRequest> previousRequests = new CircularFifoQueue<>(100);
  private ClientPlayerInputRequest clientInputRequest;
  private final CircularFifoQueue<ServerSnapshotResponse> responses = new CircularFifoQueue<>(100);

  private String spLevel;

  public MultiplayerGame(final Game game)
  {
    this.game = game;
  }

  public void update(final float delta)
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

        // TODO: do server reconciliation
        player.setPosition(state.x, state.y);

        // TODO: interpolate the positions of other players
      }
    }
  }

  public void moveUp()
  {
    if (isActive())
    {
      getClientInputRequest().enableMoveUp();
    }
  }

  public void moveLeft()
  {
    if (isActive())
    {
      getClientInputRequest().enableMoveLeft();
    }
  }

  public void moveRight()
  {
    if (isActive())
    {
      getClientInputRequest().enableMoveRight();
    }
  }

  public void moveDown()
  {
    if (isActive())
    {
      getClientInputRequest().enableMoveDown();
    }
  }

  public void jump(final float velocityX, final float velocityY)
  {
    if (isActive())
    {
      getClientInputRequest().enableJump(velocityX, velocityY);
    }
  }

  public void resetToStart()
  {
    if (isActive())
    {
      getClientInputRequest().enableReset();
    }
  }

  private ClientPlayerInputRequest getClientInputRequest()
  {
    if (clientInputRequest == null)
    {
      clientInputRequest = new ClientPlayerInputRequest();
      clientInputRequest.requestId = requestId;
      requestId++;
      previousRequests.add(clientInputRequest);
    }

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
    requestId = 1;
    game.loadLevel(spLevel);
  }

  public void sendRequests()
  {
    if (isActive())
    {
      if (clientInputRequest != null)
      {
        client.requests.add(clientInputRequest);
        client.sendRequests();
        clientInputRequest = null;
      }
    }
  }

  public boolean isActive()
  {
    return active && client != null && client.isConnected();
  }
}
