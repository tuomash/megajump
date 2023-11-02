package com.orbinski.megajump;

class LevelEditor
{
  Level level;
  boolean active;
  Entity entity;

  void addPlatform(final float x, final float y)
  {
    final Platform platform = new Platform(x, y, 40.0f, 1.5f);

    if (!level.overlapsEntity(platform))
    {
      level.platforms.add(platform);
    }
  }

  void addTrampoline(final float x, final float y)
  {
    final Trampoline trampoline = new Trampoline(x, y, 5.0f, 1.5f);

    if (!level.overlapsEntity(trampoline))
    {
      level.trampolines.add(trampoline);
    }
  }

  void addDecoration(final float x, final float y)
  {
    final Decoration decoration = new Decoration(x, y, 1.5f, 2.5f);

    if (!level.overlapsEntity(decoration))
    {
      level.decorations.add(decoration);
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
    }
  }

  void decreaseEntitySize()
  {
    if (entity != null)
    {
      entity.decreaseWidth();
      entity.decreaseHeight();
    }
  }

  void clearEntity()
  {
    if (entity != null)
    {
      entity.selected = false;
    }

    entity = null;
  }
}
