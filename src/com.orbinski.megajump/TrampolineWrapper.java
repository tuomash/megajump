package com.orbinski.megajump;

class TrampolineWrapper extends EntityWrapper
{
  int version = 1;

  TrampolineWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  TrampolineWrapper(final Trampoline trampoline)
  {
    super(trampoline);
  }

  Trampoline unwrap()
  {
    return new Trampoline(this);
  }
}
