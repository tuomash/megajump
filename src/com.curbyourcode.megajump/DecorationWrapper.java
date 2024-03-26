package com.orbinski.megajump;

class DecorationWrapper extends EntityWrapper
{
  private static final long serialVersionUID = 1L;

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
