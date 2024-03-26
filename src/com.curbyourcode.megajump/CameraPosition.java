package com.orbinski.megajump;

public class CameraPosition extends Entity
{
  public CameraPosition(final float width, final float height)
  {
    super(width, height);
  }

  public CameraPosition(final CameraPositionWrapper wrapper)
  {
    super(wrapper);
  }

  @Override
  boolean isCameraStartPosition()
  {
    return true;
  }
}
