package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;

class Game
{
  final Player player;
  final Door door;
  final CameraState cameraState;

  boolean movementPressedAfterReset;

  Game()
  {
    player = new Player();
    door = new Door(25.0f, 25.0f, 5.0f, 5.0f);
    cameraState = new CameraState();
  }

  void update(final float delta)
  {
    player.update(delta);

    if (Entity.overlaps(player, door))
    {
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
    final Camera camera = Renderer.staticViewport.getCamera();
    camera.position.x = 0.0f;
    camera.position.y = 0.0f;

    if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
    {
      movementPressedAfterReset = true;
    }
    else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
    {
      movementPressedAfterReset = true;
    }
  }
}
