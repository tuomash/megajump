package com.orbinski.megajump;

class BlockWrapper extends EntityWrapper
{
  private static final long serialVersionUID = 1L;

  BlockWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  BlockWrapper(final Block block)
  {
    super(block);
  }

  Block unwrap()
  {
    return new Block(this);
  }
}
