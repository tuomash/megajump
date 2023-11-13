package com.orbinski.megajump;

import com.badlogic.gdx.math.Rectangle;

class JumpAssistant
{
  final Player player;

  private float cursorX;
  private float cursorY;
  boolean targeting;

  private float jumpElapsed;
  private final float jumpTarget = 1.25f;
  private boolean canJump = true;

  final Rectangle[] jumpCurve = new Rectangle[250];

  JumpAssistant(final Player player)
  {
    this.player = player;

    for (int i = 0; i < jumpCurve.length; i++)
    {
      jumpCurve[i] = new Rectangle();
    }
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

      final float jumpVelocityX = calculateJumpVelocityX();

      // Add jump x velocity existing velocity
      // Only add if max x velocity has not been reached
      if (player.velocityX < player.maxVelocityX && player.velocityX > -player.maxVelocityX)
      {
        player.updateVelocityX(jumpVelocityX);
      }

      final float jumpVelocityY = calculateJumpVelocityY();

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

  private float calculateJumpVelocityX()
  {
    final float maxDiffX = 40.0f;
    final float playerWorldX = player.getPosition().x;
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

    if (cursorX < player.getPosition().x)
    {
      jumpVelocityX = -jumpVelocityX;
    }

    return jumpVelocityX;
  }

  private float calculateJumpVelocityY()
  {
    final float maxDiffY = 40.0f;
    final float playerWorldY = player.getPosition().y;
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

    if (cursorY < player.getPosition().y)
    {
      jumpVelocityY = -jumpVelocityY;
    }

    return jumpVelocityY;
  }

  void updateCursorLocation(final float x, final float y)
  {
    cursorX = x;
    cursorY = y;
    calculateJumpCurve();
  }

  private void calculateJumpCurve()
  {
    final float velocityX = player.clampVelocityX(calculateJumpVelocityX());
    float velocityY = player.clampVelocityY(calculateJumpVelocityY());

    float x = player.getPosition().x;
    float y = player.getPosition().y;
    final Rectangle first = jumpCurve[0];
    first.x = x;
    first.y = y;

    for (int i = 1; i < jumpCurve.length; i++)
    {
      velocityY = velocityY + Globals.GRAVITY * Globals.TIME_STEP_SECONDS;
      final float distanceX = velocityX * Globals.TIME_STEP_SECONDS; // approximation
      final float distanceY = velocityY * Globals.TIME_STEP_SECONDS; // approximation
      x = x + distanceX;
      y = y + distanceY;

      final Rectangle rectangle = jumpCurve[i];
      rectangle.x = x;
      rectangle.y = y;
    }
  }

  boolean canJump()
  {
    return canJump && (player.getLocation() == Player.Location.START || player.getLocation() == Player.Location.PLATFORM);
  }

  void reset()
  {
    targeting = false;
    canJump = true;
    jumpElapsed = 0.0f;
  }
}
