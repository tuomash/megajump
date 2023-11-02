package com.orbinski.megajump;

class Decoration extends Entity
{
  Decoration(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
    texture = Resources.candle;
  }

  @Override
  boolean isDecoration()
  {
    return true;
  }
}
