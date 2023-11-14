package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.orbinski.megajump.Globals;
import com.orbinski.megajump.Levels;
import com.orbinski.megajump.Physics;
import com.orbinski.megajump.Player;

import java.util.ArrayList;
import java.util.List;

public class MServer extends Thread
{
  final Server server;
  final ServerListener listener;
  final Physics physics;
  final Levels levels;
  private final List<Player> players = new ArrayList<>();

  private final List<ClientPlayerAddRequest> clientPlayerAddRequestQueue = new ArrayList<>();
  private final List<ClientPlayerRemoveRequest> clientPlayerRemoveRequestQueue = new ArrayList<>();
  private final List<ClientPlayerInputRequest> clientPlayerInputRequestQueue = new ArrayList<>();
  private final ServerSnapshotResponse snapshotResponse = new ServerSnapshotResponse();

  boolean running;
  boolean shutdownRequest;

  public MServer()
  {
    server = new Server();
    listener = new ServerListener(this);
    server.addListener(listener);
    physics = new Physics();
    levels = new Levels();
  }

  @Override
  public synchronized void start()
  {
    try
    {
      server.bind(54555, 54777);
      running = true;

      physics.setLevel(levels.get("platforms_2"));

      server.getKryo().register(ArrayList.class);
      server.getKryo().register(ClientPlayerAddRequest.class);
      server.getKryo().register(ClientPlayerInputRequest.class);
      server.getKryo().register(ClientPlayerRemoveRequest.class);
      server.getKryo().register(ExampleRequest.class);
      server.getKryo().register(PlayerData.class);
      server.getKryo().register(ServerSnapshotResponse.class);

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
        x receive_from_clients(); // poll, accept, receive, decode, validate
        x update(); // AI, simulate
        x send_updates_clients();
        var elapsed = current_time() - begin;
        if(elapsed < tick)
        {
          sleep(tick - elapsed);
        }
         */

        // Process network updates

        server.update(0);

        // Process client player add requests

        for (int i = 0; i < clientPlayerAddRequestQueue.size(); i++)
        {
          final ClientPlayerAddRequest request = clientPlayerAddRequestQueue.get(i);
          addPlayer(request.connection, request.playerId);
          // snapshotResponse.addedPlayers.add(request.playerId);
        }

        clientPlayerAddRequestQueue.clear();

        // Process client player remove requests

        for (int i = 0; i < clientPlayerRemoveRequestQueue.size(); i++)
        {
          final ClientPlayerRemoveRequest request = clientPlayerRemoveRequestQueue.get(i);
          removePlayer(request.playerId);
          // snapshotResponse.removedPlayers.add(request.playerId);
        }

        clientPlayerRemoveRequestQueue.clear();

        // Process client player input requests

        for (int i = 0; i < clientPlayerInputRequestQueue.size(); i++)
        {
          final ClientPlayerInputRequest request = clientPlayerInputRequestQueue.get(i);
          final Player player = getPlayer(request.playerId);

          if (player != null)
          {
            if (request.jump)
            {
              player.jump();
            }
            else
            {
              if (request.moveUp)
              {
                player.moveUp();
              }

              if (request.moveLeft)
              {
                player.moveLeft();
              }

              if (request.moveRight)
              {
                player.moveRight();
              }

              if (request.moveDown)
              {
                player.moveDown();
              }
            }
          }
        }

        clientPlayerInputRequestQueue.clear();

        // Update physics

        physics.update(Globals.TIME_STEP_SECONDS);

        // Update player positions in the server snapshot

        for (int i = 0; i < players.size(); i++)
        {
          final Player player = players.get(i);
          final PlayerData data = new PlayerData();
          data.playerId = player.id;
          data.name = player.name;
          data.x = player.getPosition().x;
          data.y = player.getPosition().y;
          // snapshotResponse.playerDataList.add(data);
        }

        // Send players the server snapshot

        for (int i = 0; i < players.size(); i++)
        {
          final Player player = players.get(i);
          final Connection connection = player.connection;

          if (connection != null && connection.isConnected())
          {
            connection.sendUDP(snapshotResponse);
          }
        }

        // Clear the server snapshot

        snapshotResponse.reset();

        // TODO: implement server tick rate
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

  public void addPlayer(final Connection connection, final int id)
  {
    if (canAdd(id))
    {
      final Player player = new Player();
      player.id = id;

      if (physics.canAdd(player))
      {
        player.connection = connection;
        players.add(player);
        physics.addPlayer(player);
      }
    }
  }

  public boolean canAdd(final int id)
  {
    for (int i = 0; i < players.size(); i++)
    {
      if (id == players.get(i).id)
      {
        return false;
      }
    }

    return true;
  }

  public Player getPlayer(final int id)
  {
    for (int i = 0; i < players.size(); i++)
    {
      final Player player = players.get(i);

      if (id == player.id)
      {
        return player;
      }
    }

    return null;
  }

  public void removePlayer(final int id)
  {
    int indexToRemove = -1;

    for (int i = 0; i < players.size(); i++)
    {
      if (id == players.get(i).id)
      {
        indexToRemove = i;
        break;
      }
    }

    if (indexToRemove != -1)
    {
      final Player player = players.remove(indexToRemove);
      physics.removePlayer(player);
    }
  }

  public void addClientPlayerAddRequest(final ClientPlayerAddRequest request)
  {
    clientPlayerAddRequestQueue.add(request);
  }

  public void addClientPlayerRemoveRequest(final ClientPlayerRemoveRequest request)
  {
    clientPlayerRemoveRequestQueue.add(request);
  }

  public void addClientPlayerInputRequest(final ClientPlayerInputRequest request)
  {
    clientPlayerInputRequestQueue.add(request);
  }

  @Override
  public void interrupt()
  {
    super.interrupt();
    shutdownRequest = true;
  }
}
