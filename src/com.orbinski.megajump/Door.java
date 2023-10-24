package com.orbinski.megajump;

class Door extends Entity
{
  Door(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
    setTexture(Resources.door);
  }

  @Override
  boolean isDoor()
  {
    return true;
  }
}
