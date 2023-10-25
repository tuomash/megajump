package com.orbinski.megajump;

class Platform extends Entity
{
  public Platform(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
    // showBorder = true;
  }

  @Override
  boolean isPlatform()
  {
    return true;
  }
}
