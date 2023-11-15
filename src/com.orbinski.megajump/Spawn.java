package com.orbinski.megajump;

public class Spawn extends Entity
{
  Spawn(final float width, final float height)
  {
    super(width, height);
  }

  Spawn(final SpawnWrapper wrapper)
  {
    super(wrapper);
  }

  @Override
  boolean isSpawn()
  {
    return true;
  }
}
