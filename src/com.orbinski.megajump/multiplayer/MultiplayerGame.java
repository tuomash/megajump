package com.orbinski.megajump.multiplayer;

import com.orbinski.megajump.Game;
import com.orbinski.megajump.Player;

import java.util.ArrayList;
import java.util.List;

public class MultiplayerGame
{
  public final Game game;

  private MClient client;
  private ClientConnector connector;

  public boolean active;

  private int requestId = 1;
  // TODO: replace with a fixed size queue or list
  private List<ClientPlayerInputRequest> previousRequests = new ArrayList<>();
  private ClientPlayerInputRequest clientInputRequest;

  // TODO: replace with a fixed size queue or list
  public List<ServerSnapshotResponse> responses = new ArrayList<>();

  private String spLevel;

  public MultiplayerGame(final Game game)
  {
    this.game = game;
  }

  public void update(final float delta)
  {
    if (isActive())
    {
      if (!responses.isEmpty())
      {
        final ServerSnapshotResponse response = responses.remove(0);
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
