package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

class Controller
{
  final Game game;
  private final Vector3 mouseScreen = new Vector3();

  Controller(final Game game)
  {
    this.game = game;
  }

  void update()
  {
    if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.ENTER))
    {
      final Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();

      if (Gdx.graphics.isFullscreen())
      {
        // System.out.println("w: " + currentMode.width + ", h:" + currentMode.height);
        Gdx.graphics.setWindowedMode(currentMode.width, currentMode.height);
      }
      else
      {
        // TODO: doesn't work properly when going from windowed (not maximized) to full screen
        Gdx.graphics.setFullscreenMode(currentMode);
      }
    }

    handlePlayerControls();
  }

  void handlePlayerControls()
  {
    mouseScreen.x = Gdx.input.getX();
    mouseScreen.y = Gdx.input.getY();

    final Vector3 result = Renderer.unproject(mouseScreen);

    if (result == null)
    {
      return;
    }

    if (Gdx.input.isTouched() && !game.player.moving)
    {
      if (!game.player.targeting)
      {
        game.player.targeting = true;
      }

      game.player.updateCursorLocation(result.x, result.y);
    }
    else if (game.player.targeting)
    {
      game.player.targeting = false;
      game.player.jump();
    }

    if (Gdx.input.isKeyJustPressed(Input.Keys.R))
    {
      game.player.reset();
    }
  }
}
