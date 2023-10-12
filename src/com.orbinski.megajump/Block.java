package com.orbinski.megajump;

class Block extends Entity
{
  public Block(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
    // showBorder = true;
  }

  @Override
  boolean isBlock()
  {
    return true;
  }
}
