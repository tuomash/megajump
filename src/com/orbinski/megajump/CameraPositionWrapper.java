package com.orbinski.megajump;

public class CameraPositionWrapper extends EntityWrapper
{
  private static final long serialVersionUID = 1L;

  public CameraPositionWrapper()
  {
    // Empty constructor is needed for deserialization
  }

  public CameraPositionWrapper(final CameraPosition position)
  {
    super(position);
  }

  public CameraPosition unwrap()
  {
    return new CameraPosition(this);
  }
}
