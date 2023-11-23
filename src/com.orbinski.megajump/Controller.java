package com.orbinski.megajump;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.Gdx.*;

public class Controller
{
  private final Game gameObj;

  private GameInterface game;

  private final Vector2 mouse = new Vector2();

  final Vector2 originalSelection = new Vector2();
  private int originalSelectionX = 0;
  private int originalSelectionY = 0;
  final Vector2 changedSelection = new Vector2();

  Controller(final Game game)
  {
    this.gameObj = game;
  }

  void update()
  {
    game = gameObj.getGame();

    if (input.isKeyPressed(Input.Keys.ALT_LEFT) && input.isKeyJustPressed(Input.Keys.ENTER))
    {
      final Graphics.DisplayMode currentMode = graphics.getDisplayMode();

      if (graphics.isFullscreen())
      {
        // System.out.println("w: " + currentMode.width + ", h:" + currentMode.height);
        graphics.setWindowedMode(currentMode.width, currentMode.height);
      }
      else
      {
        // TODO: doesn't work properly when going from windowed (not maximized) to full screen
        graphics.setFullscreenMode(currentMode);
      }
    }

    if (game.isLevelEditor())
    {
      handleEditorControls();
    }
    else
    {
      handlePlayerControls();
    }
  }

  void handlePlayerControls()
  {
    if (input.isKeyJustPressed(Input.Keys.F1))
    {
      game.toggleHelp();
    }
    else if (input.isKeyJustPressed(Input.Keys.SPACE))
    {
      game.togglePaused();
    }

    if (game.isHelp() || game.isPaused())
    {
      return;
    }

    if (input.isKeyPressed(Input.Keys.CONTROL_LEFT) && input.isKeyJustPressed(Input.Keys.E))
    {
      game.toggleLevelEditor();
    }
    // TODO: remove these when a proper UI has been implemented
    else if (input.isKeyPressed(Input.Keys.CONTROL_LEFT) && input.isKeyJustPressed(Input.Keys.NUM_1))
    {
      game.connectToServer();
    }
    // TODO: remove these when a proper UI has been implemented
    else if (input.isKeyPressed(Input.Keys.CONTROL_LEFT) && input.isKeyJustPressed(Input.Keys.NUM_2))
    {
      game.disconnectFromServer();
    }

    if (input.isKeyJustPressed(Input.Keys.R))
    {
      game.resetPlayerToStart();
    }
    else if (input.isKeyJustPressed(Input.Keys.P))
    {
      game.selectPreviousLevel();
    }
    else if (input.isKeyJustPressed(Input.Keys.N))
    {
      game.selectNextLevel();
    }

    // Set screen coordinates
    mouse.x = input.getX();
    mouse.y = input.getY();

    // Transform to world coordinates
    Renderer.unproject(mouse);

    if (!game.getLevel().finished)
    {
      if (input.isTouched())
      {
        if (!game.isTargeting())
        {
          game.setTargeting(true);
        }

        game.updateAssistantPosition(mouse);
      }
      else if (game.isTargeting())
      {
        game.jump();
      }
    }

    // Player aerial controls
    if (input.isKeyPressed(Input.Keys.W) || input.isKeyPressed(Input.Keys.UP))
    {
      game.moveUp();
    }
    else if (input.isKeyPressed(Input.Keys.S) || input.isKeyPressed(Input.Keys.DOWN))
    {
      game.moveDown();
    }

    if (input.isKeyPressed(Input.Keys.A) || input.isKeyPressed(Input.Keys.LEFT))
    {
      game.moveLeft();
    }
    else if (input.isKeyPressed(Input.Keys.D) || input.isKeyPressed(Input.Keys.RIGHT))
    {
      game.moveRight();
    }
    // Camera controls
    /*
    else
    {
      if (input.isKeyPressed(Input.Keys.W) || input.isKeyPressed(Input.Keys.UP))
      {
        game.cameraState.moveUp();
      }
      else if (input.isKeyPressed(Input.Keys.S) || input.isKeyPressed(Input.Keys.DOWN))
      {
        game.cameraState.moveDown();
      }

      if (input.isKeyPressed(Input.Keys.A) || input.isKeyPressed(Input.Keys.LEFT))
      {
        game.cameraState.moveLeft();
      }
      else if (input.isKeyPressed(Input.Keys.D) || input.isKeyPressed(Input.Keys.RIGHT))
      {
        game.cameraState.moveRight();
      }
    }
     */
  }

  void handleEditorControls()
  {
    final LevelEditor editor = game.getLevelEditor();

    // Set screen coordinates
    mouse.x = input.getX();
    mouse.y = input.getY();

    // Transform to world coordinates
    Renderer.unproject(mouse);

    // Editor text input
    if (editor.input)
    {
      if (input.isKeyJustPressed(Input.Keys.ESCAPE))
      {
        if (editor.rename)
        {
          editor.disableRename();
        }
        else if (editor.command)
        {
          editor.disableCommand();
        }
      }
      else if (input.isKeyJustPressed(Input.Keys.ENTER))
      {
        if (editor.rename)
        {
          editor.renameLevel();
        }
        else if (editor.command)
        {
          editor.runCommand();
        }
      }
      else if (input.isKeyJustPressed(Input.Keys.BACKSPACE))
      {
        editor.removeCharacterFromInput();
      }
    }
    // General editor controls
    else if (input.isKeyPressed(Input.Keys.CONTROL_LEFT))
    {
      if (input.isKeyJustPressed(Input.Keys.E))
      {
        game.toggleLevelEditor();
      }
      else if (input.isKeyJustPressed(Input.Keys.NUM_1))
      {
        editor.addPlatform(mouse.x, mouse.y);
      }
      else if (input.isKeyJustPressed(Input.Keys.NUM_2))
      {
        editor.addTrampoline(mouse.x, mouse.y);
      }
      else if (input.isKeyJustPressed(Input.Keys.NUM_3))
      {
        editor.addDecoration(mouse.x, mouse.y);
      }
      else if (input.isKeyJustPressed(Input.Keys.UP))
      {
        editor.raiseCameraFloor();
      }
      else if (input.isKeyJustPressed(Input.Keys.DOWN))
      {
        editor.lowerCameraFloor();
      }
      else if (input.isKeyJustPressed(Input.Keys.C))
      {
        editor.copyEntity();
      }
      else if (input.isKeyJustPressed(Input.Keys.X))
      {
        editor.removeEntity();
      }
      else if (input.isKeyJustPressed(Input.Keys.I))
      {
        editor.increaseEntityHeight();
      }
      else if (input.isKeyJustPressed(Input.Keys.K))
      {
        editor.decreaseEntityHeight();
      }
      else if (input.isKeyJustPressed(Input.Keys.U))
      {
        editor.increaseEntityWidth();
      }
      else if (input.isKeyJustPressed(Input.Keys.J))
      {
        editor.decreaseEntityWidth();
      }
      else if (input.isKeyJustPressed(Input.Keys.Y))
      {
        editor.raiseDeathPoint();
      }
      else if (input.isKeyJustPressed(Input.Keys.H))
      {
        editor.lowerDeathPoint();
      }
      else if (input.isKeyJustPressed(Input.Keys.T))
      {
        editor.toggleMoveCameraX();
      }
      else if (input.isKeyJustPressed(Input.Keys.G))
      {
        editor.toggleMoveCameraY();
      }
      else if (input.isKeyJustPressed(Input.Keys.S))
      {
        editor.saveLevel();
      }
      else if (input.isKeyJustPressed(Input.Keys.N))
      {
        game.createNewLevel();
      }
      else if (input.isKeyJustPressed(Input.Keys.R))
      {
        if (editor.rename)
        {
          editor.disableRename();
        }
        else
        {
          editor.enableRename();
        }
      }
      else if (input.isKeyJustPressed(Input.Keys.O))
      {
        if (editor.command)
        {
          editor.disableCommand();
        }
        else
        {
          editor.enableCommand();
        }
      }
    }
    // Entity dragging controls
    else if (input.isKeyPressed(Input.Keys.SHIFT_LEFT))
    {
      if (input.isButtonPressed(Input.Buttons.LEFT))
      {
        if (editor.entity != null && (originalSelectionX != input.getX() || originalSelectionY != input.getY()))
        {
          originalSelection.x = originalSelectionX;
          originalSelection.y = originalSelectionY;
          Renderer.unproject(originalSelection);

          changedSelection.x = input.getX();
          changedSelection.y = input.getY();
          Renderer.unproject(changedSelection);

          final float diffX = changedSelection.x - originalSelection.x;
          final float diffY = changedSelection.y - originalSelection.y;
          editor.moveEntity(editor.entity.getPosition().x + diffX, editor.entity.getPosition().y + diffY);

          originalSelectionX = input.getX();
          originalSelectionY = input.getY();
        }
        else
        {
          editor.selectEntity(mouse.x, mouse.y);
          originalSelectionX = input.getX();
          originalSelectionY = input.getY();
        }
      }
      else
      {
        editor.clearEntity();
      }
    }
    else
    {
      // Entity selection controls

      if (input.isButtonJustPressed(Input.Buttons.LEFT))
      {
        editor.clearEntity();
        editor.selectEntity(mouse.x, mouse.y);
      }

      // Level controls

      if (input.isKeyJustPressed(Input.Keys.R))
      {
        game.resetPlayerToStart();
      }
      else if (input.isKeyJustPressed(Input.Keys.N))
      {
        game.selectNextLevel();
      }
      else if (input.isKeyJustPressed(Input.Keys.P))
      {
        game.selectPreviousLevel();
      }
      else if (input.isKeyJustPressed(Input.Keys.F1))
      {
        editor.help = !editor.help;
      }

      // Camera controls

      if (input.isKeyPressed(Input.Keys.W) || input.isKeyPressed(Input.Keys.UP))
      {
        game.getCameraState().moveUp();
      }
      else if (input.isKeyPressed(Input.Keys.S) || input.isKeyPressed(Input.Keys.DOWN))
      {
        game.getCameraState().moveDown();
      }

      if (input.isKeyPressed(Input.Keys.A) || input.isKeyPressed(Input.Keys.LEFT))
      {
        game.getCameraState().moveLeft();
      }
      else if (input.isKeyPressed(Input.Keys.D) || input.isKeyPressed(Input.Keys.RIGHT))
      {
        game.getCameraState().moveRight();
      }
    }
  }
}
