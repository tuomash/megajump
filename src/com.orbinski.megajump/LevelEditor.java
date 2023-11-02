package com.orbinski.megajump;

class LevelEditor
{
  Level level;
  boolean active;
  Entity entity;

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
