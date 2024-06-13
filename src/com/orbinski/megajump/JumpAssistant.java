package com.orbinski.megajump;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class JumpAssistant
{
  final Player player;

  private final Vector2 cursorPosition = new Vector2();
  public boolean targeting;

  private float jumpElapsed;
  private final float jumpTarget = 0.4f;
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

      final float playerVelocityX = Math.abs(player.velocityX);

      // Check if the player can "gain" x velocity from jumping
      if (playerVelocityX < Globals.MAX_JUMP_VELOCITY_X)
      {
        final float velocityCeilingX = Globals.MAX_JUMP_VELOCITY_X - playerVelocityX;
        float newVelocityX = calculateJumpVelocityX();

        if (newVelocityX > velocityCeilingX)
        {
          newVelocityX = velocityCeilingX;
        }

        player.updateVelocityX(newVelocityX);
      }

      final float playerVelocityY = Math.abs(player.velocityY);

      // Check if the player can "gain" y velocity from jumping
      if (playerVelocityY < Globals.MAX_JUMP_VELOCITY_Y)
      {
        final float velocityCeilingY = Globals.MAX_JUMP_VELOCITY_Y - playerVelocityY;
        float newVelocityY = calculateJumpVelocityY();

        if (newVelocityY > velocityCeilingY)
        {
          newVelocityY = velocityCeilingY;
        }

        player.updateVelocityY(newVelocityY);
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
    float jumpVelocityX = Globals.MAX_ONE_JUMP_VELOCITY_X * percentageX;

    // Clamp max jump x velocity
    if (jumpVelocityX > Globals.MAX_ONE_JUMP_VELOCITY_X)
    {
      jumpVelocityX = Globals.MAX_ONE_JUMP_VELOCITY_X;
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
    float jumpVelocityY = Globals.MAX_ONE_JUMP_VELOCITY_Y * percentageY;

    // Clamp max jump y velocity
    if (jumpVelocityY > Globals.MAX_ONE_JUMP_VELOCITY_Y)
    {
      jumpVelocityY = Globals.MAX_ONE_JUMP_VELOCITY_Y;
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