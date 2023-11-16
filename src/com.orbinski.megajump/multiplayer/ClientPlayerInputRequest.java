package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Connection;

public class ClientPlayerInputRequest extends Request
{
  public transient Connection connection;
  public transient int playerId;

  public int requestId;

  private boolean moveUp;
  private boolean moveLeft;
  private boolean moveRight;
  private boolean moveDown;
  private boolean jump;
  private boolean reset;
  private float jumpVelocityX;
  private float jumpVelocityY;

  private transient float x;
  private transient float y;
  private transient float velocityX;
  private transient float velocityY;
  private transient float frameTime;

  public int getRequestId()
  {
    return requestId;
  }

  public boolean isMoveUp()
  {
    return moveUp;
  }

  public void enableMoveUp()
  {
    moveUp = true;
    dirty = true;
  }

  public boolean isMoveLeft()
  {
    return moveLeft;
  }

  public void enableMoveLeft()
  {
    moveLeft = true;
    dirty = true;
  }

  public boolean isMoveRight()
  {
    return moveRight;
  }

  public void enableMoveRight()
  {
    moveRight = true;
    dirty = true;
  }

  public boolean isMoveDown()
  {
    return moveDown;
  }

  public void enableMoveDown()
  {
    moveDown = true;
    dirty = true;
  }

  public boolean isJump()
  {
    return jump;
  }

  public void enableJump(final float jumpVelocityX, final float jumpVelocityY)
  {
    jump = true;
    this.jumpVelocityX = jumpVelocityX;
    this.jumpVelocityY = jumpVelocityY;
    dirty = true;
  }

  public boolean isReset()
  {
    return reset;
  }

  public void enableReset()
  {
    reset = true;
    dirty = true;
  }

  public float getJumpVelocityX()
  {
    return jumpVelocityX;
  }

  public float getJumpVelocityY()
  {
    return jumpVelocityY;
  }

  public float getX()
  {
    return x;
  }

  public void setX(final float x)
  {
    this.x = x;
  }

  public float getY()
  {
    return y;
  }

  public void setY(final float y)
  {
    this.y = y;
  }

  public float getVelocityX()
  {
    return velocityX;
  }

  public void setVelocityX(final float velocityX)
  {
    this.velocityX = velocityX;
  }

  public float getVelocityY()
  {
    return velocityY;
  }

  public void setVelocityY(final float velocityY)
  {
    this.velocityY = velocityY;
  }

  public float getFrameTime()
  {
    return frameTime;
  }

  public void setFrameTime(final float frameTime)
  {
    this.frameTime = frameTime;
  }

  @Override
  public void reset()
  {
    super.reset();

    moveUp = false;
    moveLeft = false;
    moveRight = false;
    moveDown = false;
    jump = false;
    reset = false;
    x = 0.0f;
    y = 0.0f;
    velocityX = 0.0f;
    velocityY = 0.0f;
    jumpVelocityX = 0.0f;
    jumpVelocityY = 0.0f;
    frameTime = 0.0f;
  }
}
