package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Camera;

class Game
{
  final Player player;
  final Levels levels;
  final CameraState cameraState;

  Level level;
  boolean help;
  boolean paused;

  Game()
  {
    player = new Player();
    levels = new Levels();
    cameraState = new CameraState();

    levels.goToBeginning();
    level = levels.getLevel();
    level.updateUI();
    reset();
  }

  void update(final float delta)
  {
    if (help || paused)
    {
      return;
    }

    player.update(delta);
    level.update(delta, player);

    if (player.getY() < -70.0f)
    {
      UserInterface.retryText.visible = true;
      player.moving = false;
      level.finished = true;
    }

    if (player.moving)
    {
      final Camera camera = Renderer.staticViewport.getCamera();

      if (level.moveCameraX)
      {
        camera.position.x = camera.position.x + delta * player.velocityX;
      }

      if (level.moveCameraY)
      {
        camera.position.y = camera.position.y + delta * player.velocityY;
      }
    }
    else
    {
      updateCameraState(delta);
    }
  }

  private void updateCameraState(final float delta)
  {
    if (!player.moving && cameraState.moving && cameraState.active)
    {
      final Camera camera = Renderer.staticViewport.getCamera();
      final float max = 200.0f;
      camera.position.x = camera.position.x + delta * cameraState.velocityX;
      camera.position.y = camera.position.y + delta * cameraState.velocityY;

      if (camera.position.x > max)
      {
        camera.position.x = max;
      }
      else if (camera.position.x < -max)
      {
        camera.position.x = -max;
      }

      if (camera.position.y > max)
      {
        camera.position.y = max;
      }
      else if (camera.position.y < -max)
      {
        camera.position.y = -max;
      }

      cameraState.reset();
    }
    else if (player.moving)
    {
      final Camera camera = Renderer.staticViewport.getCamera();
      // camera.position.x = player.getX() + 10.0f;
      // camera.position.y = player.getY() + 10.0f;
    }
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

  void loadTextureReferences()
  {
    player.texture = Renderer.dwarfRight;
  }

  void jump()
  {
    player.targeting = false;
    player.jump();
    level.started = true;
  }

  void reset()
  {
    player.reset();
    cameraState.reset();

    if (Renderer.staticViewport != null)
    {
      final Camera camera = Renderer.staticViewport.getCamera();
      camera.position.x = 0.0f;
      camera.position.y = 0.0f;
    }

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
