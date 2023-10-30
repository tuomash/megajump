package com.orbinski.megajump;

class JumpAssistant
{
  final Player player;

  final float cursorWidth = 0.5f;
  final float cursorHeight = 0.5f;
  float cursorX;
  float cursorY;
  boolean targeting;

  private float jumpElapsed;
  private final float jumpTarget = 1.25f;
  private boolean canJump = true;

  public JumpAssistant(final Player player)
  {
    this.player = player;
  }

  void update(final float delta)
  {
    if (!canJump)
    {
      jumpElapsed = jumpElapsed + delta;
      UserInterface.jumpBar.updateBar(jumpElapsed, jumpTarget);

      if (jumpElapsed > jumpTarget)
      {
        jumpElapsed = 0.0f;
        canJump = true;
      }
    }
  }

  void jump()
  {
    if (canJump())
    {
      player.applyGravity = true;
      UserInterface.retryText.visible = false;

      final float maxDiffX = 40.0f;
      final float playerWorldX = player.getX();
      float diffX = Math.abs(cursorX - playerWorldX);

      if (diffX > maxDiffX)
      {
        diffX = maxDiffX;
      }

      final float percentageX = diffX / maxDiffX;
      float jumpVelocityX = player.maxJumpVelocityX * percentageX;

      // Clamp max jump x velocity
      if (jumpVelocityX > player.maxJumpVelocityX)
      {
        jumpVelocityX = player.maxJumpVelocityX;
      }

      if (cursorX < player.getX())
      {
        jumpVelocityX = -jumpVelocityX;
      }

      // Add jump x velocity existing velocity
      // Only add if max x velocity has not been reached
      if (player.velocityX < player.maxVelocityX && player.velocityX > -player.maxVelocityX)
      {
        player.updateVelocityX(jumpVelocityX);
      }

      final float maxDiffY = 40.0f;
      final float playerWorldY = player.getY();
      float diffY = Math.abs(cursorY - playerWorldY);

      if (diffY > maxDiffY)
      {
        diffY = maxDiffY;
      }

      final float percentageY = diffY / maxDiffY;
      float jumpVelocityY = player.maxJumpVelocityY * percentageY;

      // Clamp max jump y velocity
      if (jumpVelocityY > player.maxJumpVelocityY)
      {
        jumpVelocityY = player.maxJumpVelocityY;
      }

      if (cursorY < player.getY())
      {
        jumpVelocityY = -jumpVelocityY;
      }

      // Add jump y velocity existing velocity
      // Only add if max y velocity has not been reached
      if (player.velocityY < player.maxVelocityY && player.velocityY > -player.maxVelocityY)
      {
        player.updateVelocityY(jumpVelocityY);
      }

      if (player.velocityX > 0.0f)
      {
        player.setDirection(Player.Direction.RIGHT);
      }
      else if (player.velocityX < 0.0f)
      {
        player.setDirection(Player.Direction.LEFT);
      }

      canJump = false;
      player.setState(Player.State.JUMPING);
    }
  }

  boolean canJump()
  {
    return canJump && (player.getPosition() == Player.Position.START || player.getPosition() == Player.Position.PLATFORM);
  }

  void reset()
  {
    targeting = false;
    canJump = true;
    jumpElapsed = 0.0f;
  }
}
