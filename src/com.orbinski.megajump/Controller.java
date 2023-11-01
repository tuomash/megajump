package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

class Controller
{
  final Game game;

  private final Vector3 mouse = new Vector3();

  final Vector3 originalSelection = new Vector3();
  private int originalSelectionX = 0;
  private int originalSelectionY = 0;
  final Vector3 changedSelection = new Vector3();

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

    // Set screen coordinates
    mouse.x = Gdx.input.getX();
    mouse.y = Gdx.input.getY();

    // Transform to world coordinates
    Renderer.unproject(mouse);

    if (!game.level.finished)
    {
      if (Gdx.input.isTouched())
      {
        if (!game.player.assistant.targeting)
        {
          game.player.assistant.targeting = true;
          game.cameraState.active = false;
        }

        game.player.assistant.updateCursorLocation(mouse.x, mouse.y);
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
    else if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyJustPressed(Input.Keys.D))
    {
      game.levelEditor.removeEntity();
    }

    // Set screen coordinates
    mouse.x = Gdx.input.getX();
    mouse.y = Gdx.input.getY();

    // Transform to world coordinates
    Renderer.unproject(mouse);

    final LevelEditor editor = game.levelEditor;

    // Entity dragging controls

    if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT))
    {
      if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
      {
        if (editor.entity != null && (originalSelectionX != Gdx.input.getX() || originalSelectionY != Gdx.input.getY()))
        {
          originalSelection.x = originalSelectionX;
          originalSelection.y = originalSelectionY;
          Renderer.unproject(originalSelection);

          changedSelection.x = Gdx.input.getX();
          changedSelection.y = Gdx.input.getY();
          Renderer.unproject(changedSelection);

          final float diffX = changedSelection.x - originalSelection.x;
          final float diffY = changedSelection.y - originalSelection.y;
          editor.moveEntity(editor.entity.getX() + diffX, editor.entity.getY() + diffY);

          originalSelectionX = Gdx.input.getX();
          originalSelectionY = Gdx.input.getY();
        }
        else
        {
          editor.selectEntity(mouse.x, mouse.y);
          originalSelectionX = Gdx.input.getX();
          originalSelectionY = Gdx.input.getY();
        }
      }
      else
      {
        editor.clearEntity();
      }
    }

    // Entity selection controls

    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))
    {
      editor.selectEntity(mouse.x, mouse.y);
    }

    // Camera controls

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

    if (Gdx.input.isKeyPressed(Input.Keys.C))
    {
      game.reset();
    }
  }
}
