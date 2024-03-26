package com.orbinski.megajump;

class TrampolineWrapper extends EntityWrapper
{
  private static final long serialVersionUID = 1L;

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
