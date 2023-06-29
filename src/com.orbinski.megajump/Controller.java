package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;

class Controller
{
  final Game game;

  Controller(Game game)
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
    if (Gdx.input.isTouched())
    {
      if (!game.player.targeting)
      {
        game.player.targeting = true;
      }

      game.player.mouseScreen.x = Gdx.input.getX();
      game.player.mouseScreen.y = Gdx.input.getY();
    }
    else if (game.player.targeting)
    {
      game.player.targeting = false;
      game.player.shoot();
    }

    if (Gdx.input.isKeyJustPressed(Input.Keys.R))
    {
      game.player.reset();
    }
  }
}
