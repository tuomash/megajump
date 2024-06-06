package com.orbinski.megajump;

import com.badlogic.gdx.math.Rectangle;

import java.util.List;

import static com.orbinski.megajump.Globals.FRICTION_PLATFORM;

public class Physics
{
  private final Rectangle rectangle1 = new Rectangle();
  private final Rectangle rectangle2 = new Rectangle();

  public List<Player> players;
  public Level level;
  private float delta;

  public void update(final float delta)
  {
    this.delta = delta;

    if (level == null || level.finished)
    {
      return;
    }
    else if (!level.started)
    {
      // Update moving exits even if the player hasn't started yet
      applyMovement(level.exit);
      return;
    }

    // Do exit movement first
    applyMovement(level.exit);

    // Then do player movement and collision detection
    applyPlayerMovementAndDetectCollisions();
  }

  public void applyPlayerMovementAndDetectCollisions()
  {
    if (players == null || players.isEmpty())
    {
      return;
    }

    for (int i = 0; i < players.size(); i++)
    {
      final Player player = players.get(i);

      if (player.state != Player.State.EXIT && player.state != Player.State.DEATH)
      {
        player.setLocation(Player.Location.NONE);

        if (player.verticalMovement == Player.VerticalMovement.UP)
        {
          if (player.state == Player.State.JUMPING)
          {
            player.updateVelocityY(Globals.PLAYER_VELOCITY_JUMPING * delta);
          }
        }
        else if (player.verticalMovement == Player.VerticalMovement.DOWN)
        {
          if (player.state == Player.State.JUMPING)
          {
            player.updateVelocityY(-Globals.PLAYER_VELOCITY_JUMPING * delta);
          }
        }

        if (player.horizontalMovement == Player.HorizontalMovement.LEFT)
        {
          if (player.state == Player.State.JUMPING || player.state == Player.State.WALL_LANDING)
          {
            player.updateVelocityX(-Globals.PLAYER_VELOCITY_JUMPING * delta);
          }
          else if (player.state == Player.State.LANDING)
          {
            player.updateVelocityX(-Globals.PLAYER_VELOCITY_LANDING * delta);
          }
        }
        else if (player.horizontalMovement == Player.HorizontalMovement.RIGHT)
        {
          if (player.state == Player.State.JUMPING || player.state == Player.State.WALL_LANDING)
          {
            player.updateVelocityX(Globals.PLAYER_VELOCITY_JUMPING * delta);
          }
          else if (player.state == Player.State.LANDING)
          {
            player.updateVelocityX(Globals.PLAYER_VELOCITY_LANDING * delta);
          }
        }

        if (player.applyGravity)
        {
          player.velocityY = player.velocityY + Globals.GRAVITY * delta;
        }

        boolean xCollision = false;
        boolean yCollision = false;

        if (player.hasVelocityX())
        {
          final float distanceX = player.velocityX * delta;
          player.setX(player.getPosition().x + distanceX);
          xCollision = detectCollisions(player, true, false);
        }

        if (player.hasVelocityY() && player.state != Player.State.EXIT && player.state != Player.State.DEATH)
        {
          final float distanceY = player.velocityY * delta;
          player.setY(player.getPosition().y + distanceY);
          yCollision = detectCollisions(player, false, true);
        }

        final boolean detect = detectWalljump(player);

        if (player.state == Player.State.WALL_LANDING && (!detect || yCollision))
        {
          player.setState(Player.State.JUMPING);
        }

        if (!xCollision && !yCollision && !detect)
        {
          player.resetTouchedFor();
        }
      }
    }
  }

  private void applyMovement(final Entity entity)
  {
    if (entity != null && !entity.dead)
    {
      switch (entity.movement)
      {
        case REGULAR:
        {
          if (entity.applyGravity)
          {
            entity.velocityY = entity.velocityY + Globals.GRAVITY * delta;
          }

          final float distanceX = entity.velocityX * delta;
          final float distanceY = entity.velocityY * delta;
          entity.setPosition(entity.getPosition().x + distanceX,
                             entity.getPosition().y + distanceY);

          break;
        }

        case WAYPOINTS:
        {
          // Need at least two waypoints
          if (entity.getWaypoints() == null || entity.getWaypoints().size() < 2)
          {
            return;
          }

          if (entity.currentWaypoint == null)
          {
            entity.currentWaypoint = entity.getWaypoints().get(entity.waypointIndex);
          }

          final float distanceToTarget = MathUtils.distance(entity.getPosition().x,
                                                            entity.getPosition().y,
                                                            entity.currentWaypoint.x,
                                                            entity.currentWaypoint.y);

          if (distanceToTarget < 0.5)
          {
            entity.waypointIndex++;

            if (entity.waypointIndex >= entity.getWaypoints().size())
            {
              entity.waypointIndex = 0;
            }

            entity.currentWaypoint = entity.getWaypoints().get(entity.waypointIndex);

            return;
          }

          final float toTargetX = (entity.currentWaypoint.x - entity.getPosition().x) / distanceToTarget;
          final float toTargetY = (entity.currentWaypoint.y - entity.getPosition().y) / distanceToTarget;
          final float distanceX = entity.velocityX * delta;
          final float distanceY = entity.velocityY * delta;

          entity.setPosition(entity.getPosition().x + (toTargetX * distanceX),
                             entity.getPosition().y + (toTargetY * distanceY));

          break;
        }

        case NO_MOVEMENT:
        {
          break;
        }
      }
    }
  }

  private boolean detectCollisions(final Player player, final boolean xAxis, final boolean yAxis)
  {
    if (level.exit != null && level.exit.overlaps(player))
    {
      player.stop();
      player.setState(Player.State.EXIT);
      return false;
    }

    if (player.getPosition().y < level.deathPoint.y)
    {
      player.stop();
      player.setState(Player.State.DEATH);
      return false;
    }

    for (int i = 0; i < level.trampolines.size(); i++)
    {
      final Trampoline trampoline = level.trampolines.get(i);

      if (EntityUtils.overlaps(player, trampoline))
      {
        player.velocityY = (Math.abs(player.velocityY) + 50.0f) * 0.60f;
        return false;
      }
    }

    boolean collision = false;

    if (xAxis)
    {
      for (int i = 0; i < level.platforms.size(); i++)
      {
        final Platform platform = level.platforms.get(i);
        final float platformLeftX = platform.getPosition().x - platform.getWidthOffset();
        final float platformRightX = platform.getPosition().x + platform.getWidthOffset();

        if (player.getPosition().x > platformRightX && player.velocityX < 0.0f && EntityUtils.overlaps(player, platform))
        {
          collision = true;

          player.setX(platformRightX + player.collisionBoxWidthOffset);
          player.velocityX = 0.0f;
          player.setLocation(Player.Location.PLATFORM);

          if (player.direction == Player.Direction.LEFT)
          {
            player.setState(Player.State.WALL_LANDING);
          }

          player.updateTouchedFor(delta);

          break;
        }
        else if (player.getPosition().x < platformLeftX && player.velocityX > 0.0f && EntityUtils.overlaps(player, platform))
        {
          collision = true;

          player.setX(platformLeftX - player.collisionBoxWidthOffset);
          player.velocityX = 0.0f;
          player.setLocation(Player.Location.PLATFORM);

          if (player.direction == Player.Direction.RIGHT)
          {
            player.setState(Player.State.WALL_LANDING);
          }

          player.updateTouchedFor(delta);

          break;
        }
      }
    }

    if (yAxis)
    {
      for (int i = 0; i < level.platforms.size(); i++)
      {
        final Platform platform = level.platforms.get(i);
        final float platformTopY = platform.getPosition().y + platform.getHeightOffset();
        final float platformBottomY = platform.getPosition().y - platform.getHeightOffset();

        if (player.getPosition().y > platformTopY && player.velocityY < 0.0f && EntityUtils.overlaps(player, platform))
        {
          collision = true;

          player.setY(platformTopY + player.collisionBoxHeightOffset);
          player.velocityY = 0.0f;
          player.setLocation(Player.Location.PLATFORM);

          if (player.state == Player.State.JUMPING)
          {
            player.setState(Player.State.LANDING);
          }

          player.updateTouchedFor(delta);

          if (player.touchedFor > Globals.FRICTION_PLATFORM_APPLY_AFTER_SECONDS)
          {
            if (player.velocityX > FRICTION_PLATFORM)
            {
              player.velocityX = player.velocityX - FRICTION_PLATFORM;
            }
            else if (player.velocityX < -FRICTION_PLATFORM)
            {
              player.velocityX = player.velocityX + FRICTION_PLATFORM;
            }
            else
            {
              player.velocityX = 0.0f;

              if (player.state == Player.State.LANDING)
              {
                player.setState(Player.State.IDLE);
              }
            }
          }

          break;
        }
        else if (player.getPosition().y < platformBottomY && player.velocityY > 0.0f && EntityUtils.overlaps(player, platform))
        {
          collision = true;

          player.setY(platformBottomY - player.collisionBoxHeightOffset);
          player.velocityY = 0.0f;

          break;
        }
      }
    }

    return collision;
  }

  private boolean detectWalljump(final Player player)
  {
    final float reach = 1.0f;
    rectangle1.set(player.collisionBox.x + reach,
                   player.collisionBox.y,
                   player.collisionBox.width,
                   player.collisionBox.height);
    rectangle2.set(player.collisionBox.x - reach,
                   player.collisionBox.y,
                   player.collisionBox.width,
                   player.collisionBox.height);
    boolean detect = false;

    for (int i = 0; i < level.platforms.size(); i++)
    {
      final Platform platform = level.platforms.get(i);

      if (EntityUtils.overlaps(rectangle1, platform))
      {
        detect = true;

        break;
      }

      if (EntityUtils.overlaps(rectangle2, platform))
      {
        detect = true;

        break;
      }
    }

    if (detect)
    {
      player.setLocation(Player.Location.PLATFORM);
      player.updateTouchedFor(delta);
    }

    return detect;
  }

  public void clear()
  {
    players = null;
    level = null;
    delta = 0.0f;
  }
}
