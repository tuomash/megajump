package com.orbinski.megajump;

import com.badlogic.gdx.InputProcessor;

class LevelEditor implements InputProcessor
{
  Level level;
  boolean active;
  Entity entity;
  boolean input;
  StringBuilder inputBuilder = new StringBuilder();
  boolean help;

  void addPlatform(final float x, final float y)
  {
    final Platform platform = new Platform(x, y, 40.0f, 1.5f);

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
      entity.setX(x);
      entity.setY(y);

      level.setSaved(false);
    }
  }

  void removeEntity()
  {
    if (entity != null)
    {
      level.removeEntity(entity);
    }
  }

  void increaseEntitySize()
  {
    if (entity != null)
    {
      entity.increaseWidth();
      entity.increaseHeight();

      level.setSaved(false);
    }
  }

  void decreaseEntitySize()
  {
    if (entity != null)
    {
      entity.decreaseWidth();
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

  void clearEntity()
  {
    if (entity != null)
    {
      entity.selected = false;
    }

    entity = null;
  }

  void enableInput()
  {
    input = true;
    inputBuilder.setLength(0);
    inputBuilder.append(level.getName());
    UserInterface.levelNameText.visible = false;
    UserInterface.newLevelNameText.visible = true;
    UserInterface.updateNewLevelNameText(level.getName());
  }

  void disableInput()
  {
    input = false;
    UserInterface.levelNameText.visible = true;
    UserInterface.updateLevelNameText(level.getName());
    UserInterface.newLevelNameText.visible = false;
  }

  void renameLevel()
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
        disableInput();
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

    disableInput();
  }

  void removeCharacterFromInput()
  {
    if (input && inputBuilder.length() > 5)
    {
      inputBuilder.setLength(inputBuilder.length() - 1);
      UserInterface.updateNewLevelNameText(inputBuilder.toString());
    }
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
        UserInterface.updateNewLevelNameText(inputBuilder.toString());
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
