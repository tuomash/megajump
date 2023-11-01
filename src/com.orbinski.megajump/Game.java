package com.orbinski.megajump;

import com.badlogic.gdx.graphics.OrthographicCamera;

import static com.orbinski.megajump.Globals.*;

class Game
{
  final OrthographicCamera camera;
  final Player player;
  final Levels levels;
  final CameraState cameraState;
  final LevelEditor levelEditor;

  Level level;
  Save save;
  boolean help;
  boolean paused;

  Game()
  {
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

    level.updateUI();
    reset();

    Audio.playBackgroundMusic();
  }

  void update(final float delta)
  {
    if (help || paused)
    {
      return;
    }

    player.update(delta);
    level.update(delta, player, this);
    save.updateScore(level);

    if (player.canJump())
    {
      UserInterface.enableJumpBar();
    }
    else
    {
      UserInterface.disableJumpBar();
    }

    // TODO: define death point in the level
    if (player.getY() < -70.0f)
    {
      UserInterface.retryText.visible = true;
      player.stop();
      player.setState(Player.State.DEATH);
      level.finished = true;
    }

    if (player.isMoving())
    {
      if (level.moveCameraX)
      {
        camera.position.x = camera.position.x + delta * player.velocityX;
      }

      if (level.moveCameraY)
      {
        if (player.getY() > (15.0f + level.floor))
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

  private void updateCameraState(final float delta)
  {
    if (cameraState.moving && cameraState.active)
    {
      camera.position.x = camera.position.x + delta * cameraState.velocityX;
      camera.position.y = camera.position.y + delta * cameraState.velocityY;
      System.out.println("camera x:" + camera.position.x);

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

  void selectPreviousLevel()
  {
    levels.selectPreviousLevel();
    level = levels.getLevel();
    reset();
  }

  void selectNextLevel()
  {
    levels.selectNextLevel();
    level = levels.getLevel();
    reset();
  }

  void jump()
  {
    player.jump();
    level.started = true;
  }

  void setCameraToPlayer()
  {
    camera.position.x = player.getX() + 69.0f;
    camera.position.y = player.getY() + 30.0f;
  }

  void setCameraToPlayerTeleport()
  {
    camera.position.x = player.getX() + 69.0f;

    if (player.velocityY < 0.0f)
    {
      camera.position.y = player.getY() - 15.0f;
    }
    else if (player.velocityY > 0.0f)
    {
      camera.position.y = player.getY() + 15.0f;
    }
    else
    {
      camera.position.y = player.getY();
    }
  }

  void toggleLevelEditor()
  {
    levelEditor.level = level;
    levelEditor.active = !levelEditor.active;
    reset();
  }

  void reset()
  {
    player.reset();
    cameraState.reset();

    camera.position.x = 0.0f;
    camera.position.y = 0.0f;

    setCameraToPlayer();

    if (level != null)
    {
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

    UserInterface.retryText.visible = false;
  }
}
