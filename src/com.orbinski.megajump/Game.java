package com.orbinski.megajump;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.orbinski.megajump.multiplayer.ClientConnector;
import com.orbinski.megajump.multiplayer.ClientListener;
import com.orbinski.megajump.multiplayer.MClient;

import static com.orbinski.megajump.Globals.*;

public class Game
{
  public enum Mode
  {
    SINGLEPLAYER,
    MULTIPLAYER
  }

  final Physics physics;
  final OrthographicCamera camera;
  final Player player;
  final Levels levels;
  final CameraState cameraState;
  final LevelEditor levelEditor;

  Mode mode = Mode.SINGLEPLAYER;
  Level level;
  Save save;
  boolean help;
  boolean paused;

  public MClient client;
  public ClientListener listener;
  ClientConnector connector;

  Game()
  {
    physics = new Physics();
    camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    camera.update();
    player = new Player();
    levels = new Levels();
    cameraState = new CameraState();
    levelEditor = new LevelEditor();

    if (Save.doesSaveFileExist())
    {
      save = Save.readFromDisk();

      // Backup broken save file
      if (save == null)
      {
        System.out.println("error: save file seems to be broken");
        System.out.println("backing up broken save file");
        Save.backupSaveFile();
      }
    }

    if (save == null)
    {
      save = new Save();
      Save.writeToDisk(save);
    }

    levels.loadScores(save);

    if (levelTag != null && !levelTag.isEmpty())
    {
      level = levels.get(levelTag);

      if (level == null)
      {
        System.out.println("warn: no such level '" + levelTag + "'");
      }
    }

    if (level == null)
    {
      levels.goToBeginning();
      level = levels.getLevel();
    }

    if (editor)
    {
      toggleLevelEditor();
    }

    level.updateUI();
    reset();

    Audio.playBackgroundMusic();
  }

  void updateServer()
  {
    if (isMultiplayer())
    {
      client.sendRequests();
    }
  }

  void updatePhysics(final float delta)
  {
    if (help || paused)
    {
      return;
    }

    // TODO: do server reconciliation
    physics.update(delta);

    // TODO: implement proper camera following
    if (player.isMoving())
    {
      if (level.moveCameraX)
      {
        camera.position.x = camera.position.x + delta * player.velocityX;
      }

      if (level.moveCameraY)
      {
        if (player.getPosition().y > level.cameraFloor.y)
        {
          camera.position.y = camera.position.y + delta * player.velocityY + delta * 1.5f;
        }
      }
    }
    else if (cameraState.moving)
    {
      updateCameraState(delta);
    }
  }

  void update(final float delta)
  {
    if (help || paused)
    {
      return;
    }

    player.update(delta);
    level.update(delta);
    save.updateScore(level);

    if (player.canJump())
    {
      UserInterface.enableJumpBar();
    }
    else
    {
      UserInterface.disableJumpBar();
    }

    if (player.state == Player.State.DEATH)
    {
      UserInterface.retryText.visible = true;
      level.finished = true;
    }
  }

  private void updateCameraState(final float delta)
  {
    if (cameraState.moving && cameraState.active)
    {
      camera.position.x = camera.position.x + delta * cameraState.velocityX;
      camera.position.y = camera.position.y + delta * cameraState.velocityY;
      // System.out.println("camera x:" + camera.position.x);
      // System.out.println("camera y:" + camera.position.y);

      if (camera.position.x > Level.MAX_DIMENSION_OFFSET)
      {
        camera.position.x = Level.MAX_DIMENSION_OFFSET;
      }
      else if (camera.position.x < -Level.MAX_DIMENSION_OFFSET)
      {
        camera.position.x = -Level.MAX_DIMENSION_OFFSET;
      }

      if (camera.position.y > Level.MAX_DIMENSION_OFFSET)
      {
        camera.position.y = Level.MAX_DIMENSION_OFFSET;
      }
      else if (camera.position.y < -Level.MAX_DIMENSION_OFFSET)
      {
        camera.position.y = -Level.MAX_DIMENSION_OFFSET;
      }

      cameraState.reset();
    }
    /*
    else if (player.moving)
    {
      // camera.position.x = player.getX() + 10.0f;
      // camera.position.y = player.getY() + 10.0f;
    }
     */
  }

  void createNewLevel()
  {
    level = new Level();
    level.setName("New Level " + System.currentTimeMillis());
    level.setSaved(false);
    levels.addLevel(level);
    levels.goToEnd();
    levelEditor.level = level;
    reset();
  }

  void selectPreviousLevel()
  {
    levels.selectPreviousLevel();
    level = levels.getLevel();
    levelEditor.level = level;
    reset();
  }

  void selectNextLevel()
  {
    levels.selectNextLevel();
    level = levels.getLevel();
    levelEditor.level = level;
    reset();
  }

  void moveUp()
  {
    player.moveUp();

    if (isMultiplayer())
    {
      client.moveUp();
    }
  }

  void moveLeft()
  {
    player.moveLeft();

    if (isMultiplayer())
    {
      client.moveLeft();
    }
  }

  void moveRight()
  {
    player.moveRight();

    if (isMultiplayer())
    {
      client.moveRight();
    }
  }

  void moveDown()
  {
    player.moveDown();

    if (isMultiplayer())
    {
      client.moveDown();
    }
  }

  void jump()
  {
    player.jump();
    level.started = true;

    if (isMultiplayer())
    {
      client.jump();
    }
  }

  boolean isTargeting()
  {
    return player.assistant.targeting;
  }

  void setTargeting(final boolean targeting)
  {
    player.assistant.targeting = targeting;

    if (targeting)
    {
      cameraState.active = false;
    }
  }

  void updateAssistantPosition(final Vector2 newPosition)
  {
    player.assistant.updateCursorPosition(newPosition);
  }

  void setCameraToPlayer()
  {
    camera.position.x = player.getPosition().x + 69.0f;
    camera.position.y = player.getPosition().y + 30.0f;
  }

  void setCameraToPlayerTeleport()
  {
    camera.position.x = player.getPosition().x + 69.0f;

    if (player.velocityY < 0.0f)
    {
      camera.position.y = player.getPosition().y - 15.0f;
    }
    else if (player.velocityY > 0.0f)
    {
      camera.position.y = player.getPosition().y + 15.0f;
    }
    else
    {
      camera.position.y = player.getPosition().y;
    }
  }

  void toggleLevelEditor()
  {
    levelEditor.level = level;
    levelEditor.active = !levelEditor.active;
    UserInterface.updateMoveCameraXText(level.moveCameraX);
    UserInterface.updateMoveCameraYText(level.moveCameraY);
    reset();
  }

  public void setClient(final MClient client)
  {
    this.client = client;
    listener = new ClientListener(client);
    mode = Mode.MULTIPLAYER;
  }

  public void connectToServer()
  {
    connector = new ClientConnector(this);
    connector.start();
  }

  public void disconnectFromServer()
  {
    if (client != null)
    {
      client.shutdown();
      client = null;
      listener = null;
      mode = Mode.SINGLEPLAYER;
    }
  }

  public boolean isMultiplayer()
  {
    return client != null && mode == Mode.MULTIPLAYER;
  }

  void reset()
  {
    player.reset();
    cameraState.reset();

    camera.position.x = 0.0f;
    camera.position.y = 0.0f;

    if (level != null)
    {
      physics.players.clear();
      physics.players.add(player);
      physics.setLevel(level);
      level.game = this;
      level.player = player;
      level.reset();
      level.updateUI();

      if (level.cleared)
      {
        UserInterface.nextLevelText.visible = true;
      }
      else
      {
        UserInterface.nextLevelText.visible = false;
      }
    }

    setCameraToPlayer();

    UserInterface.retryText.visible = false;
  }
}
