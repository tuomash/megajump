package com.orbinski.megajump;

public class Exit extends Entity
{
  private float exitVelocityX;
  private float exitVelocityY;

  Exit(final float x, final float y, final float width, final float height)
  {
    super(x, y, width, height);
    texture = Resources.door;
  }

  Exit(final ExitWrapper wrapper)
  {
    super(wrapper);
    texture = Resources.door;
    setExitVelocityX(wrapper.exitVelocityX);
    setExitVelocityY(wrapper.exitVelocityY);
  }

  @Override
  boolean isExit()
  {
    return true;
  }

  public boolean overlaps(final Player player)
  {
    if (EntityUtils.overlaps(player.bottomSide, this))
    {
      return true;
    }
    else if (EntityUtils.overlaps(player.topSide, this))
    {
      return true;
    }
    else if (EntityUtils.overlaps(player.leftSide, this))
    {
      return true;
    }
    else if (EntityUtils.overlaps(player.rightSide, this))
    {
      return true;
    }

    return false;
  }

  float getExitVelocityX()
  {
    return exitVelocityX;
  }

  void setExitVelocityX(final float exitVelocityX)
  {
    this.exitVelocityX = exitVelocityX;
    super.velocityX = exitVelocityX;
  }

  float getExitVelocityY()
  {
    return exitVelocityY;
  }

  void setExitVelocityY(final float exitVelocityY)
  {
    this.exitVelocityY = exitVelocityY;
    super.velocityY = exitVelocityY;
  }
}
