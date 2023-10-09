package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Camera;

class Game
{
  final Player player;
  final CameraState cameraState;

  Game()
  {
    player = new Player();
    cameraState = new CameraState();
  }

  void update(final float delta)
  {
    player.update(delta);
    updateCameraState(delta);
  }

  private void updateCameraState(final float delta)
  {
    if (!player.moving && cameraState.moving)
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
  }
}
