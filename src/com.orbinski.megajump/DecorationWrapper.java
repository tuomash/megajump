package com.orbinski.megajump;

class DecorationWrapper extends EntityWrapper
{
  int version = 1;

  DecorationWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  DecorationWrapper(final Decoration decoration)
  {
    super(decoration);
  }

  Decoration unwrap()
  {
    return new Decoration(this);
  }
}
