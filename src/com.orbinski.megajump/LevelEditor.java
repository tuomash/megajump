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
      final Entity previous = entity;
      entity = level.findEntity(x, y);

      if (entity != null)
      {
        entity.selected = true;
      }

      if (previous != null)
      {
        previous.selected = false;
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
