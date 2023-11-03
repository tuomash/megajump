package com.orbinski.megajump;

class Block extends Entity
{
  public Block(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
    texture = Resources.block;
    // showBorder = true;
  }

  Block(final BlockWrapper wrapper)
  {
    super(wrapper);
    texture = Resources.block;
  }

  @Override
  boolean isBlock()
  {
    return true;
  }
}
