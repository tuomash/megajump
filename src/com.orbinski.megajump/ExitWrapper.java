package com.orbinski.megajump;

class ExitWrapper extends EntityWrapper
{
  int version = 1;

  ExitWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  ExitWrapper(final Exit exit)
  {
    super(exit);
  }

  Exit unwrap()
  {
    return new Exit(this);
  }
}
