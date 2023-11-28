package com.orbinski.megajump;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.orbinski.megajump.multiplayer.MultiplayerGame;

import java.util.ArrayList;
import java.util.List;

import static com.orbinski.megajump.Globals.*;

public class Game implements GameInterface
{
  public final Physics physics;
  final OrthographicCamera camera;
  public final Player player;
  public final List<Player> players;
  final Levels levels;
  final CameraState cameraState;
  final LevelEditor levelEditor;
  final MultiplayerGame multiplayer;

  private GameInterface game;

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
    players = new ArrayList<>();
    players.add(player);
    levels = new Levels();
    cameraState = new CameraState();
    levelEditor = new LevelEditor();
    multiplayer = new MultiplayerGame(this, camera);

    game = this;

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

  @Override
  public void updatePhysics(final float delta)
  {
    if (help || paused)
    {
      return;
    }

    physics.update(delta);
  }

  @Override
  public void update(final float delta)
  {
    if (help || paused)
    {
      return;
    }

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

    UserInterface.update(delta);
  }

  @Override
  public void handleMultiplayer()
  {
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

  @Override
  public void createNewLevel()
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

  @Override
  public void selectPreviousLevel()
  {
    levels.selectPreviousLevel();
    level = levels.getLevel();
    levelEditor.level = level;
    reset();
  }

  @Override
  public void selectNextLevel()
  {
    levels.selectNextLevel();
    level = levels.getLevel();
    levelEditor.level = level;
    reset();
  }

  @Override
  public void moveUp()
  {
    player.moveUp();
  }

  @Override
  public void moveLeft()
  {
    player.moveLeft();
  }

  @Override
  public void moveRight()
  {
    player.moveRight();
  }

  @Override
  public void moveDown()
  {
    player.moveDown();
  }

  @Override
  public void jump()
  {
    player.jump();
    level.started = true;
  }

  @Override
  public void resetPlayerToStart()
  {
    reset();
  }

  @Override
  public boolean isTargeting()
  {
    return player.assistant.targeting;
  }

  public void setTargeting(final boolean targeting)
  {
    player.assistant.targeting = targeting;

    if (targeting)
    {
      cameraState.active = false;
    }
  }

  @Override
  public void updateAssistantPosition(final Vector2 newPosition)
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

  @Override
  public void toggleLevelEditor()
  {
    levelEditor.level = level;
    levelEditor.active = !levelEditor.active;
    UserInterface.updateEdgeScrollingText(levelEditor.edgeScrolling);
    UserInterface.updateMoveCameraXText(level.moveCameraX);
    UserInterface.updateMoveCameraYText(level.moveCameraY);
    reset();
  }

  public void setToSingleplayer()
  {
    game = this;
  }

  public void setToMultiplayer()
  {
    game = multiplayer;
  }

  @Override
  public boolean isMultiplayer()
  {
    return false;
  }

  @Override
  public void connectToServer()
  {
    multiplayer.connectToServer();
  }

  @Override
  public void disconnectFromServer()
  {
    multiplayer.disconnectFromServer();
  }

  public GameInterface getGame()
  {
    return game;
  }

  @Override
  public boolean isHelp()
  {
    return help;
  }

  @Override
  public void toggleHelp()
  {
    help = !help;
  }

  @Override
  public boolean isPaused()
  {
    return paused;
  }

  @Override
  public void togglePaused()
  {
    paused = !paused;
  }

  @Override
  public CameraState getCameraState()
  {
    return cameraState;
  }

  @Override
  public Player getPlayer()
  {
    return player;
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
    return levelEditor.active;
  }

  @Override
  public LevelEditor getLevelEditor()
  {
    return levelEditor;
  }

  public void reset()
  {
    player.reset();
    cameraState.reset();

    camera.position.x = 0.0f;
    camera.position.y = 0.0f;

    if (level != null)
    {
      physics.players = players;
      physics.level = level;
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
