package com.orbinski.megajump;

class BlockWrapper extends EntityWrapper
{
  int version = 1;

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
