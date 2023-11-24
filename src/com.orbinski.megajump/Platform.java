package com.orbinski.megajump;

class Platform extends Entity
{
  private final float widthStep = 2.0f;
  private final float heightStep = 2.0f;

  public Platform()
  {
  }

  public Platform(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
    // showBorder = true;
  }

  public Platform(final PlatformWrapper wrapper)
  {
    super(wrapper);
  }

  public Platform copy()
  {
    final Platform copy = new Platform(getPosition().x, getPosition().y, getWidth(), getHeight());
    super.copyValues(copy);
    return copy;
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
    setPosition(getPosition());
  }

  @Override
  void decreaseWidth()
  {
    if (getWidth() > widthStep)
    {
      setWidth(getWidth() - widthStep);
      setPosition(getPosition());
    }
  }

  @Override
  void increaseHeight()
  {
    setHeight(getHeight() + heightStep);
    setPosition(getPosition());
  }

  @Override
  void decreaseHeight()
  {
    if (getHeight() > heightStep)
    {
      setHeight(getHeight() - heightStep);
      setPosition(getPosition());
    }
  }
}
