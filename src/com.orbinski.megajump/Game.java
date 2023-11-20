package com.orbinski.megajump;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.orbinski.megajump.multiplayer.MultiplayerGame;

import static com.orbinski.megajump.Globals.*;

public class Game
{
  public enum Mode
  {
    SINGLEPLAYER,
    MULTIPLAYER
  }

  public final Physics physics;
  final OrthographicCamera camera;
  public final Player player;
  final Levels levels;
  final CameraState cameraState;
  final LevelEditor levelEditor;
  final MultiplayerGame multiplayer;

  Mode mode = Mode.SINGLEPLAYER;
  public Level level;
  Save save;
  boolean help;
  boolean paused;

  Game()
  {
    physics = new Physics();
    camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
    camera.update();
    player = new Player();
    levels = new Levels();
    cameraState = new CameraState();
    levelEditor = new LevelEditor();
    multiplayer = new MultiplayerGame(this);

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

  void handleMultiplayer()
  {
    multiplayer.sendRequests();
  }

  void updatePhysics(final float delta)
  {
    if (isSinglePlayer() && (help || paused))
    {
      return;
    }

    physics.update(delta);

    for (int i = 0; i < physics.getPlayers().size(); i++)
    {
      final Player player1 = physics.getPlayers().get(i);
      // System.out.println(player1.getName() + " vel x " + player.velocityX);
      // System.out.println(player1.getName() + " vel y " + player.velocityY);
    }
  }

  void update(final float delta)
  {
    if (isSinglePlayer() && (help || paused))
    {
      return;
    }

    multiplayer.update(delta);

    // System.out.println("client: x " + player.getPosition().x + " y " + player.getPosition().y);

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

  public void selectPreviousLevel()
  {
    levels.selectPreviousLevel();
    level = levels.getLevel();
    levelEditor.level = level;
    reset();
  }

  public void selectNextLevel()
  {
    levels.selectNextLevel();
    level = levels.getLevel();
    levelEditor.level = level;
    reset();
  }

  void moveUp()
  {
    player.moveUp();
  }

  void moveLeft()
  {
    player.moveLeft();
  }

  void moveRight()
  {
    player.moveRight();
  }

  void moveDown()
  {
    player.moveDown();
  }

  void jump()
  {
    player.jump();
    level.started = true;
  }

  void resetToStart()
  {
    // TODO: only reset player and
    reset();
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

  public void connectToServer()
  {
    multiplayer.connectToServer();
  }

  public void disconnectFromServer()
  {
    multiplayer.disconnectFromServer();
  }

  public boolean isSinglePlayer()
  {
    return mode == Mode.SINGLEPLAYER;
  }

  public boolean isMultiplayer()
  {
    return multiplayer.isActive();
  }

  public void reset()
  {
    player.reset();
    cameraState.reset();

    camera.position.x = 0.0f;
    camera.position.y = 0.0f;

    if (level != null)
    {
      physics.getPlayers().clear();
      physics.addPlayer(player);
      physics.setLevel(level);
      level.game = this;
      level.player = player;
      level.reset();
      level.updateUI();
      player.updatePlayerNameTextPosition();

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
