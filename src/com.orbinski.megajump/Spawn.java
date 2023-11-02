package com.orbinski.megajump;

class Spawn extends Entity
{
  Spawn(final float width, final float height)
  {
    super(width, height);
  }

  @Override
  boolean isSpawn()
  {
    return true;
  }
}
