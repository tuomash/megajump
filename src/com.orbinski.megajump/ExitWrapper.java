package com.orbinski.megajump;

class ExitWrapper extends EntityWrapper
{
  int version = 1;

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
