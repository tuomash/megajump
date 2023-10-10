package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;

class Game
{
  final Player player;
  final Levels levels;
  final CameraState cameraState;

  Level level;
  boolean movementPressedAfterReset;

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

    if (level.finished)
    {
      if (levels.isAtEnd())
      {
        levels.reset();
      }

      level = levels.getNext();
      reset();
    }

    updateCameraState(delta);
  }

  private void updateCameraState(final float delta)
  {
    if (!player.moving && cameraState.moving && !movementPressedAfterReset)
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

    if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
    {
      movementPressedAfterReset = true;
    }
    else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
    {
      movementPressedAfterReset = true;
    }

    if (level != null)
    {
      level.reset();
      UserInterface.updateLevelName(level.name);
    }
  }
}
