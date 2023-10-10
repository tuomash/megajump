package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Camera;

class Game
{
  final Player player;
  final Levels levels;
  final CameraState cameraState;

  Level level;

  Game()
  {
    player = new Player();
    levels = new Levels();
    cameraState = new CameraState();

    level = levels.getNext();
    reset();
  }

  void update(final float delta)
  {
    player.update(delta);
    level.update(delta, player);

    updateCameraState(delta);
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

  void nextLevel()
  {
    if (level.cleared)
    {
      if (levels.isAtEnd())
      {
        levels.reset();
      }

      level = levels.getNext();
      reset();
    }
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
    }
  }
}
