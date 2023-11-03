package com.orbinski.megajump;

class PlatformWrapper extends EntityWrapper
{
  int version = 1;

  PlatformWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  PlatformWrapper(final Platform platform)
  {
    super(platform);
  }

  Platform unwrap()
  {
    return new Platform(this);
  }
}
