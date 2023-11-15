package com.orbinski.megajump.multiplayer;

import com.esotericsoftware.kryonet.Connection;

public class ClientPlayerInputRequest extends Request
{
  public transient Connection connection;
  public transient int playerId;

  private boolean moveUp;
  private boolean moveLeft;
  private boolean moveRight;
  private boolean moveDown;
  private boolean jump;
  private float jumpVelocityX;
  private float jumpVelocityY;

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

  public float getJumpVelocityX()
  {
    return jumpVelocityX;
  }

  public float getJumpVelocityY()
  {
    return jumpVelocityY;
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
    jumpVelocityX = 0.0f;
    jumpVelocityY = 0.0f;
  }
}
