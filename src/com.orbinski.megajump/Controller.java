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

    if (!game.levelEditor.active)
    {
      handlePlayerControls();
    }
    else
    {
      handleEditorControls();
    }
  }

  void handlePlayerControls()
  {
    if (Gdx.input.isKeyJustPressed(Input.Keys.F1))
    {
      game.help = !game.help;
    }
    else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
    {
      game.paused = !game.paused;
    }

    if (game.help || game.paused)
    {
      return;
    }

    if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.E))
    {
      game.toggleLevelEditor();
    }

    if (Gdx.input.isKeyJustPressed(Input.Keys.R))
    {
      game.reset();
    }
    else if (Gdx.input.isKeyJustPressed(Input.Keys.P))
    {
      game.selectPreviousLevel();
    }
    else if (Gdx.input.isKeyJustPressed(Input.Keys.N))
    {
      game.selectNextLevel();
    }
    else if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
    {
      game.paused = !game.paused;
    }

    mouseScreen.x = Gdx.input.getX();
    mouseScreen.y = Gdx.input.getY();

    final Vector3 result = Renderer.unproject(mouseScreen);

    if (result == null)
    {
      return;
    }

    if (!game.level.finished)
    {
      if (Gdx.input.isTouched())
      {
        if (!game.player.assistant.targeting)
        {
          game.player.assistant.targeting = true;
          game.cameraState.active = false;
        }

        game.player.assistant.updateCursorLocation(result.x, result.y);
      }
      else if (game.player.assistant.targeting)
      {
        game.jump();
      }
    }

    // Player aerial controls
    if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))
    {
      game.player.moveUp();
    }
    else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))
    {
      game.player.moveDown();
    }

    if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
    {
      game.player.moveLeft();
    }
    else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
    {
      game.player.moveRight();
    }
    // Camera controls
    /*
    else
    {
      if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP))
      {
        game.cameraState.moveUp();
      }
      else if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN))
      {
        game.cameraState.moveDown();
      }

      if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT))
      {
        game.cameraState.moveLeft();
      }
      else if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT))
      {
        game.cameraState.moveRight();
      }
    }
     */
  }

  void handleEditorControls()
  {
    if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.E))
    {
      game.toggleLevelEditor();
    }
  }
}
