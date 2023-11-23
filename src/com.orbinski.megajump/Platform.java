package com.orbinski.megajump;

class Platform extends Entity
{
  private final float widthStep = 2.0f;
  private final float heightStep = 2.0f;

  public Platform(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
    // showBorder = true;
  }

  Platform(final PlatformWrapper wrapper)
  {
    super(wrapper);
  }

  @Override
  boolean isPlatform()
  {
    return true;
  }

  @Override
  void increaseWidth()
  {
    setWidth(getWidth() + widthStep);
  }

  @Override
  void decreaseWidth()
  {
    if (getWidth() > widthStep)
    {
      setWidth(getWidth() - widthStep);
    }
  }

  @Override
  void increaseHeight()
  {
    setHeight(getHeight() + heightStep);
  }

  @Override
  void decreaseHeight()
  {
    if (getHeight() > heightStep)
    {
      setHeight(getHeight() - heightStep);
    }
  }
}
