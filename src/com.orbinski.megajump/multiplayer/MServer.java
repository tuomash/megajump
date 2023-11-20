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
  final Levels levels;
  final Physics physics;

  private final List<PlayerMultiplayerState> playerStates = new ArrayList<>();

  private final CircularFifoQueue<ClientPlayerAddRequest> clientPlayerAddRequestQueue = new CircularFifoQueue<>(10);
  private final CircularFifoQueue<ClientPlayerRemoveRequest> clientPlayerRemoveRequestQueue = new CircularFifoQueue<>(10);
  private final CircularFifoQueue<ClientPlayerInputRequest> clientPlayerInputRequestQueue = new CircularFifoQueue<>(200);
  private final ServerSnapshotResponse snapshotResponse = new ServerSnapshotResponse();

  Level level;
  boolean running;
  boolean shutdownRequest;
  boolean doLevelChange;

  public MServer()
  {
    server = new Server();
    listener = new ServerListener(this);
    server.addListener(listener);
    levels = new Levels();
    physics = new Physics();
    physics.local = false;

    snapshotResponse.countdownState = -1;

    levels.goToBeginning();
    level = levels.getLevel();
    physics.setLevel(level);
    snapshotResponse.setLevelTag(level.getTag());

    server.getKryo().register(ClientPlayerInputRequest.class);
    server.getKryo().register(Message.class);
    server.getKryo().register(Message[].class);
    server.getKryo().register(PlayerMultiplayerState.class);
    server.getKryo().register(PlayerMultiplayerState[].class);
    server.getKryo().register(Response.class);
    server.getKryo().register(Request.class);
    server.getKryo().register(ServerSnapshotResponse.class);

    final PlayerMultiplayerState state = addPlayer(null, -1);
    state.playerName = "AI";
    state.ai = true;
    physics.addPlayer(state.player);
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

        // Get start time

        final long start = System.currentTimeMillis();

        // Change to next level if all players are finished

        if (!playerStates.isEmpty())
        {
          boolean hasHumanPlayers = false;

          for (int i = 0; i < playerStates.size(); i++)
          {
            final PlayerMultiplayerState state = playerStates.get(i);

            if (!state.ai)
            {
              hasHumanPlayers = true;
              break;
            }
          }

          if (hasHumanPlayers)
          {
            boolean allFinishedLevel = true;

            for (int i = 0; i < playerStates.size(); i++)
            {
              final PlayerMultiplayerState state = playerStates.get(i);

              if (!state.ai && !state.isAtExit)
              {
                allFinishedLevel = false;
                break;
              }
            }

            if (allFinishedLevel && !doLevelChange)
            {
              levels.selectNextLevel();
              level = levels.getLevel();
              physics.setLevel(level);
              snapshotResponse.setLevelTag(level.getTag());

              doLevelChange = true;
              System.out.println("Starting level change to: " + level.getTag());
            }

            if (doLevelChange)
            {
              boolean allFinishedLevelChange = true;

              for (int i = 0; i < playerStates.size(); i++)
              {
                final PlayerMultiplayerState state = playerStates.get(i);

                // Skip AI players
                if (state.ai)
                {
                  state.isAtExit = false;
                  state.player.reset();
                  state.setPosition(level.spawn.getPosition().x, level.spawn.getPosition().y);
                  continue;
                }

                if (state.levelTag != null && state.levelTag.equalsIgnoreCase(level.getTag()))
                {
                  System.out.println("Player " + state.playerId + " has changed to level: " + level.getTag());
                  state.setPosition(level.spawn.getPosition().x, level.spawn.getPosition().y);
                  state.isAtExit = false;
                }
                else
                {
                  allFinishedLevelChange = false;
                }
              }

              if (allFinishedLevelChange)
              {
                snapshotResponse.setLevelTag(null);
                doLevelChange = false;
                level.started = true;
                System.out.println("Level change is done");
              }
            }
          }
        }

        // TODO: show winner before level change

        // TODO: do countdown

        if (!level.started)
        {
        }

        // Process network updates

        server.update(0);

        // Process client player add requests

        for (int i = 0; i < clientPlayerAddRequestQueue.size(); i++)
        {
          final ClientPlayerAddRequest request = clientPlayerAddRequestQueue.get(i);
          final PlayerMultiplayerState state = addPlayer(request.connection, request.playerId);

          if (state != null)
          {
            final Message message = new Message();
            message.text = "Player " + state.playerName + " joined the server";
            snapshotResponse.addMessage(message);
          }
        }

        clientPlayerAddRequestQueue.clear();

        // Process client player remove requests

        for (int i = 0; i < clientPlayerRemoveRequestQueue.size(); i++)
        {
          final ClientPlayerRemoveRequest request = clientPlayerRemoveRequestQueue.get(i);
          final PlayerMultiplayerState state = removePlayer(request.playerId);

          if (state != null)
          {
            final Message message = new Message();
            message.text = "Player " + state.playerName + " left the server";
            snapshotResponse.addMessage(message);
          }
        }

        clientPlayerRemoveRequestQueue.clear();

        // Process client player input requests

        for (int i = 0; i < clientPlayerInputRequestQueue.size(); i++)
        {
          final ClientPlayerInputRequest request = clientPlayerInputRequestQueue.get(i);
          final PlayerMultiplayerState state = getPlayer(request.playerId);

          if (state != null)
          {
            state.levelTag = request.getLevelTag();

            if (!doLevelChange && level.started && !state.isAtExit)
            {
              state.setPosition(request.getX(), request.getY());
            }
          }
        }

        clientPlayerInputRequestQueue.clear();

        // Simulate AI players

        for (int i = 0; i < playerStates.size(); i++)
        {
          final PlayerMultiplayerState state = playerStates.get(i);

          if (state.ai && !doLevelChange && level.started)
          {
            final Player player = state.player;

            if (player.state == Player.State.IDLE)
            {
              player.applyGravity = true;
              player.setState(Player.State.JUMPING);
              player.updateVelocityX(player.maxJumpVelocityX);
              player.updateVelocityY(player.maxJumpVelocityY);
            }
            else if (player.state == Player.State.DEATH)
            {
              player.reset();
              player.setPosition(level.spawn.getPosition());
              state.updatePosition = true;
            }

            physics.update(Globals.TIME_STEP_SECONDS);
            state.setPosition(player.getPosition().x, player.getPosition().y);
            state.setVelocity(player.velocityX, player.velocityY);
            state.playerState = player.state.toString();
          }
        }

        // Check whether players have hit the exit

        for (int i = 0; i < playerStates.size(); i++)
        {
          final PlayerMultiplayerState state = playerStates.get(i);

          if (level.exit != null && level.exit.overlaps(state.player))
          {
            state.isAtExit = true;
          }
        }

        // Update player positions in the server snapshot

        for (int i = 0; i < playerStates.size(); i++)
        {
          final PlayerMultiplayerState state = playerStates.get(i);
          snapshotResponse.addPlayerState(state);
        }

        snapshotResponse.levelStarted = level.started;

        // Send players the server snapshot

        for (int i = 0; i < playerStates.size(); i++)
        {
          final PlayerMultiplayerState state = playerStates.get(i);

          if (!state.ai)
          {
            sendResponse(state.connection, snapshotResponse);
          }
        }

        // Reset the server snapshot

        snapshotResponse.reset();

        for (int i = 0; i < playerStates.size(); i++)
        {
          final PlayerMultiplayerState state = playerStates.get(i);
          state.updatePosition = false;
        }

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

  public PlayerMultiplayerState addPlayer(final Connection connection, final int id)
  {
    if (canAdd(id))
    {
      final PlayerMultiplayerState state = new PlayerMultiplayerState();
      state.connection = connection;
      state.playerId = id;
      state.player = new Player();
      state.playerName = "Player " + state.playerId;
      state.levelTag = level.getTag();
      playerStates.add(state);
      return state;
    }

    return null;
  }

  public boolean canAdd(final int id)
  {
    for (int i = 0; i < playerStates.size(); i++)
    {
      if (id == playerStates.get(i).playerId)
      {
        return false;
      }
    }

    return true;
  }

  public PlayerMultiplayerState getPlayer(final int id)
  {
    for (int i = 0; i < playerStates.size(); i++)
    {
      final PlayerMultiplayerState player = playerStates.get(i);

      if (id == player.playerId)
      {
        return player;
      }
    }

    return null;
  }

  public PlayerMultiplayerState removePlayer(final int id)
  {
    int indexToRemove = -1;

    for (int i = 0; i < playerStates.size(); i++)
    {
      if (id == playerStates.get(i).playerId)
      {
        indexToRemove = i;
        break;
      }
    }

    if (indexToRemove != -1)
    {
      return playerStates.remove(indexToRemove);
    }

    return null;
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
    if (connection != null && connection.isConnected())
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
