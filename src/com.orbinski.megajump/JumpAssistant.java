package com.orbinski.megajump;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class JumpAssistant
{
  final Player player;

  private final Vector2 cursorPosition = new Vector2();
  public boolean targeting;

  private float jumpElapsed;
  private final float jumpTarget = 1.25f;
  private boolean canJump = true;

  final Rectangle[] jumpCurve = new Rectangle[250];

  public JumpAssistant(final Player player)
  {
    this.player = player;

    for (int i = 0; i < jumpCurve.length; i++)
    {
      jumpCurve[i] = new Rectangle();
    }
  }

  public void update(final float delta)
  {
    if (!canJump)
    {
      jumpElapsed = jumpElapsed + delta;

      if (UserInterface.jumpBar != null)
      {
        UserInterface.jumpBar.updateBar(jumpElapsed, jumpTarget);
      }

      if (jumpElapsed > jumpTarget)
      {
        jumpElapsed = 0.0f;
        canJump = true;
      }
    }
  }

  public void jump()
  {
    if (canJump())
    {
      if (player.touchedFor > 0.0f && player.touchedFor < Globals.FRICTION_PLATFORM_APPLY_AFTER_SECONDS)
      {
        player.chainedJumps++;
      }
      else
      {
        player.chainedJumps = 0;
      }

      player.applyGravity = true;

      if (UserInterface.retryText != null)
      {
        UserInterface.retryText.visible = false;
      }

      final float jumpVelocityX = calculateJumpVelocityX();
      final float playerVelocityX = Math.abs(player.velocityX);

      // Add jump x velocity to existing velocity
      // Only add if max x velocity has not been reached
      if (playerVelocityX < player.maxVelocityX)
      {
        player.updateVelocityX(jumpVelocityX);
      }

      final float jumpVelocityY = calculateJumpVelocityY();
      final float playerVelocityY = Math.abs(player.velocityY);

      // Add jump y velocity to existing velocity
      // Only add if max y velocity has not been reached
      if (playerVelocityY < player.maxVelocityY)
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
    float diffX = Math.abs(cursorPosition.x - playerWorldX);

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

    if (cursorPosition.x < player.getPosition().x)
    {
      jumpVelocityX = -jumpVelocityX;
    }

    return jumpVelocityX;
  }

  private float calculateJumpVelocityY()
  {
    final float maxDiffY = 40.0f;
    final float playerWorldY = player.getPosition().y;
    float diffY = Math.abs(cursorPosition.y - playerWorldY);

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

    if (cursorPosition.y < player.getPosition().y)
    {
      jumpVelocityY = -jumpVelocityY;
    }

    return jumpVelocityY;
  }

  public void updateCursorPosition(final Vector2 newPosition)
  {
    cursorPosition.x = newPosition.x;
    cursorPosition.y = newPosition.y;
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

  public void reset()
  {
    targeting = false;
    canJump = true;
    jumpElapsed = 0.0f;
  }
}
