package com.orbinski.megajump;

class PlatformWrapper extends EntityWrapper
{
  private static final long serialVersionUID = 1L;

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
