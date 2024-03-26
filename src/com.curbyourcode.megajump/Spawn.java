package com.orbinski.megajump;

public class Spawn extends Entity
{
  public Spawn(final float width, final float height)
  {
    super(width, height);
  }

  public Spawn(final SpawnWrapper wrapper)
  {
    super(wrapper);
  }

  @Override
  public boolean isSpawn()
  {
    return true;
  }
}
