package com.orbinski.megajump;

class ExitWrapper extends EntityWrapper
{
  private static final long serialVersionUID = 1L;

  float exitVelocityX;
  float exitVelocityY;

  ExitWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  ExitWrapper(final Exit exit)
  {
    super(exit);

    exitVelocityX = exit.getExitVelocityX();
    exitVelocityY = exit.getExitVelocityY();
  }

  Exit unwrap()
  {
    return new Exit(this);
  }
}
