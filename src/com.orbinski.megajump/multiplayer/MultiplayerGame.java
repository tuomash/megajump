package com.orbinski.megajump.multiplayer;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.orbinski.megajump.*;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.util.ArrayList;
import java.util.List;

public class MultiplayerGame implements GameInterface
{
  private final Player clientPlayer = new Player();
  private final List<Player> players = new ArrayList<>();
  // TODO: feed multiplayer specific levels file
  private final Levels levels = new Levels();
  private final Physics physics = new Physics();
  private final CameraState cameraState = new CameraState();
  private final OrthographicCamera camera;

  private final Object lock = new Object();
  private final ClientPlayerInputRequest clientInputRequest = new ClientPlayerInputRequest();
  private final CircularFifoQueue<ServerSnapshotResponse> responses = new CircularFifoQueue<>(100);

  private MClient client;
  private ClientConnector connector;

  public boolean active;
  private Level level;

  public MultiplayerGame(final OrthographicCamera camera)
  {
    this.camera = camera;

    levels.goToBeginning();
    level = levels.getLevel();
    reset();
  }

  @Override
  public void updatePhysics(final float delta)
  {
    if (isActive())
    {
      physics.update(delta);
    }
  }

  @Override
  public void update(final float delta)
  {
    if (isActive())
    {
      final ServerSnapshotResponse response = getResponse();

      if (response != null)
      {
        // System.out.println("server: x " + response.getPlayerDataList()[0].x + ", y " + response.getPlayerDataList()[0].y);

        // Load the level requested by the server

        if (response.getLevelTag() != null && !level.getTag().equalsIgnoreCase(response.getLevelTag()))
        {
          loadLevel(response.getLevelTag());
        }

        // Set level as started when information comes from the server

        level.started = response.levelStarted;

        // Apply new state received from the server

        for (int i = 0; i < response.getPlayerStateList().length; i++)
        {
          final PlayerMultiplayerState state = response.getPlayerStateList()[i];

          if (state == null)
          {
            continue;
          }

          // Update local player
          if (state.playerId == clientPlayer.id)
          {
            if (state.playerName != null)
            {
              clientPlayer.setName(state.playerName);
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
            }

            if (state.playerName != null)
            {
              player.setName(state.playerName);
            }

            if (state.playerState != null)
            {
              player.setState(Player.State.valueOf(state.playerState));
            }

            if (state.updatePosition)
            {
              player.setPosition(state.getX(), state.getY());
            }

            player.velocityX = state.getVelocityX();
            player.velocityY = state.getVelocityY();
          }
        }

        // TODO: interpolate the positions of other players
      }

      for (int i = 0; i < players.size(); i++)
      {
        final Player player = players.get(i);
        player.updateAnimationState(false);
        player.update(delta);

        // TODO: this is a dumb solution, replace later
        player.updatePlayerNameTextPosition();
      }

      // TODO: implement proper camera following
      if (clientPlayer.isMoving())
      {
        if (level.moveCameraX)
        {
          camera.position.x = camera.position.x + delta * clientPlayer.velocityX;
        }

        if (level.moveCameraY)
        {
          if (clientPlayer.getPosition().y > level.cameraFloor.y)
          {
            camera.position.y = camera.position.y + delta * clientPlayer.velocityY + delta * 1.5f;
          }
        }
      }

      if (clientPlayer.canJump())
      {
        UserInterface.enableJumpBar();
      }
      else
      {
        UserInterface.disableJumpBar();
      }
    }
  }

  @Override
  public void handleMultiplayer()
  {
    if (isActive())
    {
      sendRequests();
    }
  }

  public void addResponse(final ServerSnapshotResponse response)
  {
    synchronized (lock)
    {
      responses.add(response);
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
    players.add(clientPlayer);
  }

  public void disconnectFromServer()
  {
    if (client != null)
    {
      client.shutdown();
    }

    client = null;
    active = false;
    players.clear();
    physics.clear();
  }

  public void sendRequests()
  {
    if (isActive())
    {
      clientInputRequest.setX(clientPlayer.getPosition().x);
      clientInputRequest.setY(clientPlayer.getPosition().y);
      clientInputRequest.setLevelTag(level.getTag());
      client.requests.add(clientInputRequest);
      client.sendRequests();
    }
  }

  public void loadLevel(final String tag)
  {
    // Level is already loaded
    if (level != null && level.getTag().equalsIgnoreCase(tag))
    {
      return;
    }

    final Level newLevel = levels.get(tag);

    if (newLevel != null)
    {
      level = newLevel;
      reset();
    }
  }

  public void setCameraToPlayer()
  {
    camera.position.x = clientPlayer.getPosition().x + 69.0f;
    camera.position.y = clientPlayer.getPosition().y + 30.0f;
  }

  public void reset()
  {
    clientPlayer.reset();
    cameraState.reset();

    camera.position.x = 0.0f;
    camera.position.y = 0.0f;

    if (level != null)
    {
      physics.players = players;
      physics.level = level;
      level.player = clientPlayer;
      level.started = false;

      for (int i = 0; i < players.size(); i++)
      {
        final Player player = players.get(i);
        player.setPosition(level.spawn.getPosition());
        player.updatePlayerNameTextPosition();
      }

      UserInterface.updateLevelNameText(level.getName());
    }

    setCameraToPlayer();
  }

  public boolean isActive()
  {
    return active && client != null && client.isConnected();
  }

  @Override
  public boolean isHelp()
  {
    return false;
  }

  @Override
  public void toggleHelp()
  {
    // TODO: implement multiplayer help
  }

  @Override
  public boolean isPaused()
  {
    return false;
  }

  @Override
  public void togglePaused()
  {
    // No implementation in multiplayer
  }

  @Override
  public CameraState getCameraState()
  {
    return cameraState;
  }

  @Override
  public Player getPlayer()
  {
    return clientPlayer;
  }

  @Override
  public List<Player> getPlayers()
  {
    return players;
  }

  @Override
  public Level getLevel()
  {
    return level;
  }

  @Override
  public boolean isLevelEditor()
  {
    return false;
  }

  @Override
  public void toggleLevelEditor()
  {
    // No implementation in multiplayer
  }

  @Override
  public LevelEditor getLevelEditor()
  {
    return null;
  }

  @Override
  public boolean isMultiplayer()
  {
    return true;
  }

  @Override
  public void resetPlayerToStart()
  {
    clientPlayer.reset();
    cameraState.reset();

    camera.position.x = 0.0f;
    camera.position.y = 0.0f;

    clientPlayer.setPosition(level.spawn.getPosition());
    clientPlayer.updatePlayerNameTextPosition();

    setCameraToPlayer();
  }

  @Override
  public void selectPreviousLevel()
  {
    // No implementation in multiplayer
  }

  @Override
  public void selectNextLevel()
  {
    // No implementation in multiplayer
  }

  @Override
  public boolean isTargeting()
  {
    return clientPlayer.assistant.targeting;
  }

  @Override
  public void setTargeting(final boolean targeting)
  {
    clientPlayer.assistant.targeting = targeting;

    if (targeting)
    {
      cameraState.active = false;
    }
  }

  @Override
  public void updateAssistantPosition(final Vector2 newPosition)
  {
    clientPlayer.assistant.updateCursorPosition(newPosition);
  }

  @Override
  public void jump()
  {
    clientPlayer.jump();
  }

  @Override
  public void moveUp()
  {
    clientPlayer.moveUp();
  }

  @Override
  public void moveLeft()
  {
    clientPlayer.moveLeft();
  }

  @Override
  public void moveRight()
  {
    clientPlayer.moveRight();
  }

  @Override
  public void moveDown()
  {
    clientPlayer.moveDown();
  }

  @Override
  public void createNewLevel()
  {
    // No implementation in multiplayer
  }
}
