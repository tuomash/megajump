package com.orbinski.megajump;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class LevelEditor implements InputProcessor
{
  Level level;
  boolean active;
  Entity entity;
  Entity copySource;
  boolean input;
  StringBuilder inputBuilder = new StringBuilder();
  boolean help;
  boolean rename;
  boolean command;
  boolean edgeScrolling = false;

  LevelEditor()
  {
    Gdx.input.setInputProcessor(this);
  }

  void addPlatform(final float x, final float y)
  {
    final Platform platform = new Platform(x, y, 40.0f, 1.5f);
    addPlatform(platform);
  }

  void addPlatform(final Platform platform)
  {
    if (!level.overlapsEntity(platform))
    {
      level.platforms.add(platform);
      level.setSaved(false);
    }
  }

  void addTrampoline(final float x, final float y)
  {
    final Trampoline trampoline = new Trampoline(x, y, 5.0f, 1.5f);

    if (!level.overlapsEntity(trampoline))
    {
      level.trampolines.add(trampoline);
      level.setSaved(false);
    }
  }

  void addDecoration(final float x, final float y)
  {
    final Decoration decoration = new Decoration(x, y, 1.5f, 2.5f);

    if (!level.overlapsEntity(decoration))
    {
      level.decorations.add(decoration);
      level.setSaved(false);
    }
  }

  void selectEntity(final float x, final float y)
  {
    if (level != null)
    {
      entity = level.findEntity(x, y);

      if (entity != null)
      {
        entity.selected = true;
      }
    }
  }

  void moveEntity(final float x, final float y)
  {
    if (entity != null)
    {
      entity.setPosition(x, y);

      if (level.overlapsEntity(entity))
      {
        entity.moveToPreviousPosition();
      }
      else
      {
        level.setSaved(false);
      }
    }
  }

  void copyEntity()
  {
    if (entity != null)
    {
      copySource = entity;
    }
  }

  void pasteEntity(final Vector2 position)
  {
    if (copySource != null)
    {
      if (copySource instanceof Platform platform)
      {
        final Platform copy = platform.copy();
        copy.setPosition(position);
        addPlatform(copy);
      }
    }
  }

  void removeEntity()
  {
    if (entity != null)
    {
      level.removeEntity(entity);
    }
  }

  void increaseEntityWidth()
  {
    if (entity != null)
    {
      final float prevWidth = entity.getWidth();
      entity.increaseWidth();

      if (level.overlapsEntity(entity))
      {
        entity.setWidth(prevWidth);
      }
      else
      {
        level.setSaved(false);
      }
    }
  }

  void decreaseEntityWidth()
  {
    if (entity != null)
    {
      entity.decreaseWidth();

      level.setSaved(false);
    }
  }

  void increaseEntityHeight()
  {
    if (entity != null)
    {
      final float prevHeight = entity.getHeight();
      entity.increaseHeight();

      if (level.overlapsEntity(entity))
      {
        entity.setHeight(prevHeight);
      }
      else
      {
        level.setSaved(false);
      }
    }
  }

  void decreaseEntityHeight()
  {
    if (entity != null)
    {
      entity.decreaseHeight();

      level.setSaved(false);
    }
  }

  void raiseCameraFloor()
  {
    level.raiseCameraFloor(5.0f);
  }

  void lowerCameraFloor()
  {
    level.lowerCameraFloor(5.0f);
  }

  void raiseDeathPoint()
  {
    level.raiseDeathPoint(5.0f);
  }

  void lowerDeathPoint()
  {
    level.lowerDeathPoint(5.0f);
  }

  void toggleMoveCameraX()
  {
    level.moveCameraX = !level.moveCameraX;
    level.setSaved(false);
    UserInterface.updateMoveCameraXText(level.moveCameraX);
  }

  void toggleMoveCameraY()
  {
    level.moveCameraY = !level.moveCameraY;
    level.setSaved(false);
    UserInterface.updateMoveCameraYText(level.moveCameraY);
  }

  boolean saveLevel()
  {
    final LevelWrapper wrapper = new LevelWrapper(level);

    if (wrapper.writeToDisk())
    {
      level.setSaved(true);
      return true;
    }

    return false;
  }

  void clearSelectionStatus()
  {
    if (entity != null)
    {
      entity.selected = false;
    }

    entity = null;

    if (copySource != null)
    {
      copySource.selected = false;
    }

    copySource = null;
  }

  void enableInput()
  {
    input = true;
    inputBuilder.setLength(0);
    UserInterface.levelNameText.visible = false;
    UserInterface.moveCameraXText.visible = false;
    UserInterface.moveCameraYText.visible = false;
    UserInterface.caret.visible = true;
  }

  void disableInput()
  {
    input = false;
    UserInterface.levelNameText.visible = true;
    UserInterface.moveCameraXText.visible = true;
    UserInterface.moveCameraYText.visible = true;
    UserInterface.caret.visible = false;
  }

  void removeCharacterFromInput()
  {
    if (input && !inputBuilder.isEmpty())
    {
      inputBuilder.setLength(inputBuilder.length() - 1);

      if (UserInterface.newLevelNameText != null && UserInterface.newLevelNameText.visible)
      {
        UserInterface.updateNewLevelNameText(inputBuilder.toString());
      }
      else if (UserInterface.commandText != null && UserInterface.commandText.visible)
      {
        UserInterface.updateCommandText(inputBuilder.toString());
      }
    }
  }

  void enableRename()
  {
    enableInput();
    rename = true;
    inputBuilder.append(level.getName());
    UserInterface.newLevelNameText.visible = true;
    UserInterface.updateNewLevelNameText(level.getName());
  }

  void disableRename()
  {
    disableInput();
    rename = false;
    UserInterface.newLevelNameText.visible = false;
    UserInterface.updateLevelNameText(level.getName());
  }

  void renameLevel()
  {
    if (rename && !inputBuilder.isEmpty())
    {
      final String previousName = level.getName();
      final String previousTag = level.getTag();
      final String newLevelName = inputBuilder.toString();
      level.setName(newLevelName);

      if (!LevelWrapper.doesLevelExist(level.getTag()))
      {
        if (!saveLevel())
        {
          System.out.println("warn: couldn't save level file '" + level.getTag() + "'");
          level.setName(previousName);
          disableRename();
          return;
        }

        if (!LevelWrapper.deleteLevel(previousTag))
        {
          System.out.println("warn: couldn't delete previous level file '" + previousTag + "'");
        }
      }
      else
      {
        level.setName(previousName);
      }
    }

    disableRename();
  }

  void enableCommand()
  {
    enableInput();
    command = true;
    UserInterface.commandText.visible = true;
    UserInterface.updateCommandText("");
  }

  void disableCommand()
  {
    disableInput();
    command = false;
    UserInterface.commandText.visible = false;
  }

  void runCommand()
  {
    if (command && !inputBuilder.isEmpty())
    {
      final String commandStr = inputBuilder.toString();
      final String[] parts = commandStr.split(" ");

      // TODO: implement proper parsing when needed
      if (parts.length == 3)
      {
        final String trophyLevel = parts[1];
        final int milliseconds;

        try
        {
          milliseconds = Integer.parseInt(parts[2]);
        }
        catch (final Exception e)
        {
          // Expected, can happen
          disableCommand();
          return;
        }

        if (trophyLevel.equalsIgnoreCase("gold"))
        {
          level.goldTimeInMilliseconds = milliseconds;
          level.setSaved(false);
        }
        else if (trophyLevel.equalsIgnoreCase("silver"))
        {
          level.silverTimeInMilliseconds = milliseconds;
          level.setSaved(false);
        }
        else if (trophyLevel.equalsIgnoreCase("bronze"))
        {
          level.bronzeTimeInMilliseconds = milliseconds;
          level.setSaved(false);
        }
      }
    }

    disableCommand();
  }

  public void toggleEdgeScrolling()
  {
    edgeScrolling = !edgeScrolling;
    UserInterface.updateEdgeScrollingText(edgeScrolling);
  }

  @Override
  public boolean keyDown(final int i)
  {
    return false;
  }

  @Override
  public boolean keyUp(final int i)
  {
    return false;
  }

  @Override
  public boolean keyTyped(final char c)
  {
    if (input)
    {
      if (Character.isAlphabetic(c) || Character.isDigit(c) || Character.isSpaceChar(c))
      {
        inputBuilder.append(c);
      }

      if (rename)
      {
        UserInterface.updateNewLevelNameText(inputBuilder.toString());
      }
      else if (command)
      {
        UserInterface.updateCommandText(inputBuilder.toString());
      }
    }

    return false;
  }

  @Override
  public boolean touchDown(final int i, final int i1, final int i2, final int i3)
  {
    return false;
  }

  @Override
  public boolean touchUp(final int i, final int i1, final int i2, final int i3)
  {
    return false;
  }

  @Override
  public boolean touchDragged(final int i, final int i1, final int i2)
  {
    return false;
  }

  @Override
  public boolean mouseMoved(final int i, final int i1)
  {
    return false;
  }

  @Override
  public boolean scrolled(float v, float v1)
  {
    return false;
  }
}
