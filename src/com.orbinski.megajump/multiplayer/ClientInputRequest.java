package com.orbinski.megajump.multiplayer;

public class ClientInputRequest
{
  public boolean moveUp;
  public boolean moveLeft;
  public boolean moveRight;
  public boolean moveDown;
  public boolean jump;
  public float jumpVelocityX;
  public float jumpVelocityY;

  public void reset()
  {
    moveUp = false;
    moveLeft = false;
    moveRight = false;
    moveDown = false;
    jump = false;
    jumpVelocityX = 0.0f;
    jumpVelocityY = 0.0f;
  }
}
