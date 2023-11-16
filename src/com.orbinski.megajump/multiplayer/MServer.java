package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.orbinski.megajump.*;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.List;

public class MServer extends Thread
{
  final Server server;
  final ServerListener listener;
  final Physics physics;
  final Levels levels;
  private final List<Player> players = new ArrayList<>();

  private final CircularFifoQueue<ClientPlayerAddRequest> clientPlayerAddRequestQueue = new CircularFifoQueue<>(10);
  private final CircularFifoQueue<ClientPlayerRemoveRequest> clientPlayerRemoveRequestQueue = new CircularFifoQueue<>(10);
  private final CircularFifoQueue<ClientPlayerInputRequest> clientPlayerInputRequestQueue = new CircularFifoQueue<>(200);
  private final ServerSnapshotResponse snapshotResponse = new ServerSnapshotResponse();

  Level level;
  boolean running;
  boolean shutdownRequest;

  public MServer()
  {
    server = new Server();
    listener = new ServerListener(this);
    server.addListener(listener);
    physics = new Physics();
    levels = new Levels();

    level = levels.get("platforms_2");
    physics.setLevel(level);
    snapshotResponse.setLevelTag(level.getTag());

    server.getKryo().register(int[].class);
    server.getKryo().register(ClientPlayerAddRequest.class);
    server.getKryo().register(ClientPlayerInputRequest.class);
    server.getKryo().register(ClientPlayerRemoveRequest.class);
    server.getKryo().register(ExampleRequest.class);
    server.getKryo().register(PlayerMultiplayerState.class);
    server.getKryo().register(PlayerMultiplayerState[].class);
    server.getKryo().register(Response.class);
    server.getKryo().register(Request.class);
    server.getKryo().register(ServerSnapshotResponse.class);
  }

  @Override
  public synchronized void start()
  {
    try
    {
      server.bind(54555, 54777);
      level.started = true;
      running = true;
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
        x var begin = current_time();
        x receive_from_clients(); // poll, accept, receive, decode, validate
        x update(); // AI, simulate
        x send_updates_clients();
        x var elapsed = current_time() - begin;
        if(elapsed < tick)
        {
          x sleep(tick - elapsed);
        }
         */

        // Get start time

        final long start = System.currentTimeMillis();

        // Process network updates

        server.update(0);

        // Process client player add requests

        for (int i = 0; i < clientPlayerAddRequestQueue.size(); i++)
        {
          final ClientPlayerAddRequest request = clientPlayerAddRequestQueue.get(i);
          addPlayer(request.connection, request.playerId);
          snapshotResponse.addPlayer(request.playerId);
        }

        clientPlayerAddRequestQueue.clear();

        // Process client player remove requests

        for (int i = 0; i < clientPlayerRemoveRequestQueue.size(); i++)
        {
          final ClientPlayerRemoveRequest request = clientPlayerRemoveRequestQueue.get(i);
          removePlayer(request.playerId);
          snapshotResponse.removePlayer(request.playerId);
        }

        clientPlayerRemoveRequestQueue.clear();

        // Process client player input requests

        for (int i = 0; i < clientPlayerInputRequestQueue.size(); i++)
        {
          final ClientPlayerInputRequest request = clientPlayerInputRequestQueue.get(i);
          final Player player = getPlayer(request.playerId);

          if (player != null)
          {
            if (request.getRequestId() > player.multiplayerState.requestId)
            {
              player.multiplayerState.requestId = request.getRequestId();
            }

            // TODO: check if player can actually jump
            if (request.isJump())
            {
              player.applyGravity = true;
              player.updateVelocityX(request.getJumpVelocityX());
              player.updateVelocityY(request.getJumpVelocityY());
            }
            else if (request.isReset())
            {
              player.reset();
              player.setPosition(level.spawn.getPosition());
            }
            else
            {
              if (request.isMoveUp())
              {
                player.moveUp();
              }

              if (request.isMoveLeft())
              {
                player.moveLeft();
              }

              if (request.isMoveRight())
              {
                player.moveRight();
              }

              if (request.isMoveDown())
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
          final PlayerMultiplayerState state = player.multiplayerState;
          state.playerId = player.id;
          state.name = player.name;
          state.x = player.getPosition().x;
          state.y = player.getPosition().y;
          snapshotResponse.addPlayerState(state);
        }

        // Send players the server snapshot

        for (int i = 0; i < players.size(); i++)
        {
          final Player player = players.get(i);
          final Connection connection = player.connection;
          sendResponse(connection, snapshotResponse);
        }

        // Clear the server snapshot

        snapshotResponse.reset();

        // Calculate elapsed time

        final long elapsed = System.currentTimeMillis() - start;
        // System.out.println("Server update took " + elapsed + "ms");

        // Sleep the amount that's needed to match the tick rate

        if (elapsed < Globals.SERVER_TICK_RATE_MILLISECONDS)
        {
          Thread.sleep(Globals.SERVER_TICK_RATE_MILLISECONDS - elapsed);
        }
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
    if (!clientPlayerAddRequestQueue.isFull())
    {
      clientPlayerAddRequestQueue.add(request);
    }
  }

  public void addClientPlayerRemoveRequest(final ClientPlayerRemoveRequest request)
  {
    if (!clientPlayerRemoveRequestQueue.isFull())
    {
      clientPlayerRemoveRequestQueue.add(request);
    }
  }

  public void addClientPlayerInputRequest(final ClientPlayerInputRequest request)
  {
    if (!clientPlayerInputRequestQueue.isFull())
    {
      clientPlayerInputRequestQueue.add(request);
    }
  }

  private void sendResponse(final Connection connection, final Response response)
  {
    if (connection != null && connection.isConnected() && response.dirty)
    {
      if (response.type == Response.Type.TCP)
      {
        connection.sendTCP(response);
      }
      else
      {
        connection.sendUDP(response);
      }
    }
  }

  @Override
  public void interrupt()
  {
    super.interrupt();
    shutdownRequest = true;
  }
}
