package com.orbinski.megajump.multiplayer;

import com.orbinski.megajump.Game;
import com.orbinski.megajump.Globals;
import com.orbinski.megajump.Player;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.List;

public class MultiplayerGame
{
  public final Game game;

  private MClient client;
  private ClientConnector connector;

  public boolean active;

  private final List<Player> players = new ArrayList<>();

  private final Object lock = new Object();
  private final ClientPlayerInputRequest clientInputRequest = new ClientPlayerInputRequest();
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

        // Load the level requested by the server

        if (response.getLevelTag() != null && !game.level.getTag().equalsIgnoreCase(response.getLevelTag()))
        {
          game.loadLevel(response.getLevelTag());

          for (int i = 0; i < players.size(); i++)
          {
            final Player player = players.get(i);
            game.physics.addPlayer(player);
          }
        }

        // Update player states

        for (int i = 0; i < response.getPlayerStateList().length; i++)
        {
          final PlayerMultiplayerState state = response.getPlayerStateList()[i];

          if (state == null)
          {
            continue;
          }

          // Update local player
          if (state.playerId == game.player.id)
          {
            final Player player = game.player;

            if (state.playerName != null)
            {
              player.setName(state.playerName);
            }
          }
          else
          {
            // TODO: drop non-existing players
            Player player = null;

            for (int z = 0; z < players.size(); z++)
            {
              final Player existing = players.get(z);

              if (state.playerId == existing.id)
              {
                player = existing;
                break;
              }
            }

            if (player == null)
            {
              player = new Player();
              player.id = state.playerId;
              players.add(player);
              game.physics.addPlayer(player);
            }

            if (state.playerName != null)
            {
              player.setName(state.playerName);
            }

            if (state.playerState != null)
            {
              player.setState(Player.State.valueOf(state.playerState));
            }

            player.setPosition(state.getX(), state.getY());
            player.updateAnimationState(false);
            player.update(delta);
            player.updatePlayerNameTextPosition();
          }
        }

        // TODO: interpolate the positions of other players
      }

      // TODO: this is a dumb solution, replace later
      game.player.updatePlayerNameTextPosition();
    }
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
    if (connector == null)
    {
      connector = new ClientConnector(this);
      connector.start();
    }
  }

  public void clearClientConnector()
  {
    if (connector != null)
    {
      connector.interrupt();
      connector = null;
    }
  }

  public void setClient(final MClient client)
  {
    this.client = client;
    active = true;

    // Store current singleplayer level so that we can go back to it after disconnecting
    spLevel = game.level.getTag();
  }

  public void disconnectFromServer()
  {
    if (client != null)
    {
      client.shutdown();
    }

    client = null;
    active = false;
    game.loadLevel(spLevel);
    spLevel = null;
  }

  public void sendRequests()
  {
    if (isActive())
    {
      clientInputRequest.setX(game.player.getPosition().x);
      clientInputRequest.setY(game.player.getPosition().y);
      clientInputRequest.setLevelTag(game.level.getTag());
      client.requests.add(clientInputRequest);
      client.sendRequests();
    }
  }

  public boolean isActive()
  {
    return active && client != null && client.isConnected();
  }

  public List<Player> getPlayers()
  {
    return players;
  }
}
