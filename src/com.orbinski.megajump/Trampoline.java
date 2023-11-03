package com.orbinski.megajump;

class Trampoline extends Entity
{
  private final float widthStep = 1.0f;
  private final float heightStep = 1.0f;

  Trampoline(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
  }

  Trampoline(final TrampolineWrapper wrapper)
  {
    super(wrapper);
  }

  boolean apply(final Player player)
  {
    if (EntityUtils.overlaps(player.bottomSide, this))
    {
      player.moveToPreviousLocation();
      player.velocityY = (Math.abs(player.velocityY) + 50.0f) * 0.60f;

      return true;
    }

    return false;
  }

  @Override
  boolean isTrampoline()
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
    // setHeight(getHeight() + heightStep);
  }

  @Override
  void decreaseHeight()
  {
    /*
    if (getHeight() > heightStep)
    {
      setHeight(getHeight() - heightStep);
    }
     */
  }
}
