package com.orbinski.megajump;

import java.util.List;

import static com.orbinski.megajump.Globals.*;

public class Physics
{
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

      if (player.isMoving() && player.state != Player.State.EXIT && player.state != Player.State.DEATH)
      {
        if (player.applyGravity)
        {
          player.velocityY = player.velocityY + Globals.GRAVITY * delta;
        }

        final float distanceX = player.velocityX * delta;
        player.setX(player.getPosition().x + distanceX);
        detectCollisions(player, true, false);

        if (player.isMoving() && player.state != Player.State.EXIT && player.state != Player.State.DEATH)
        {
          final float distanceY = player.velocityY * delta;
          player.setY(player.getPosition().y + distanceY);
          detectCollisions(player, false, true);
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

  /*
  private void detectCollisionsV1(final Player player)
  {
    if (level.exit != null && level.exit.overlaps(player))
    {
      player.stop();
      player.setState(Player.State.EXIT);
      return;
    }

    if (player.getPosition().y < level.deathPoint.y)
    {
      player.stop();
      player.setState(Player.State.DEATH);
      return;
    }

    player.setLocation(Player.Location.NONE);

    boolean hit = false;

    for (int z = 0; z < level.trampolines.size(); z++)
    {
      final Trampoline trampoline = level.trampolines.get(z);

      if (EntityUtils.overlaps(player.bottomSide, trampoline))
      {
        player.velocityY = (Math.abs(player.velocityY) + 50.0f) * 0.60f;

        hit = true;
        break;
      }
    }

    if (!hit)
    {
      for (int z = 0; z < level.platforms.size(); z++)
      {
        final Platform platform = level.platforms.get(z);

        if (player.velocityY < 0.0f && EntityUtils.overlaps(player.bottomSide, platform))
        {
          hit = true;
          player.setY(platform.getPosition().y + platform.getHeightOffset() + 3.0f);
          player.velocityY = 0.0f;
          player.setLocation(Player.Location.PLATFORM);

          if (player.state == Player.State.JUMPING)
          {
            player.setState(Player.State.LANDING);
          }
          else if (player.state == Player.State.LANDING && (player.animation == null || player.animation.isAtEnd()))
          {
            player.setState(Player.State.IDLE);
          }

          if (player.velocityX > FRICTION)
          {
            player.velocityX = player.velocityX - FRICTION;
          }
          else if (player.velocityX < -FRICTION)
          {
            player.velocityX = player.velocityX + FRICTION;
          }
          else
          {
            player.velocityX = 0.0f;
          }
        }
        else if (player.velocityY > 0.0f && EntityUtils.overlaps(player.topSide, platform))
        {
          hit = true;
          player.setY(platform.getPosition().y - platform.getHeightOffset() - 3.0f);
          player.velocityY = 0.0f;
        }
        else if (player.velocityX < 0.0f && EntityUtils.overlaps(player.leftSide, platform))
        {
          hit = true;
          player.setX(platform.getPosition().x + platform.getWidthOffset() + 1.8f);
          player.velocityX = 0.0f;
        }
        else if (player.velocityX > 0.0f && EntityUtils.overlaps(player.rightSide, platform))
        {
          hit = true;
          player.setX(platform.getPosition().x - platform.getWidthOffset() - 1.8f);
          player.velocityX = 0.0f;
        }

        if (hit)
        {
          break;
        }
      }
    }
  }
   */

  private void detectCollisions(final Player player, final boolean xAxis, final boolean yAxis)
  {
    if (level.exit != null && level.exit.overlaps(player))
    {
      player.stop();
      player.setState(Player.State.EXIT);
      return;
    }

    if (player.getPosition().y < level.deathPoint.y)
    {
      player.stop();
      player.setState(Player.State.DEATH);
      return;
    }

    player.setLocation(Player.Location.NONE);

    for (int i = 0; i < level.trampolines.size(); i++)
    {
      final Trampoline trampoline = level.trampolines.get(i);

      if (EntityUtils.overlaps(player, trampoline))
      {
        player.velocityY = (Math.abs(player.velocityY) + 50.0f) * 0.60f;
        return;
      }
    }

    if  (xAxis)
    {
      final float adjustment = 2.25f;

      for (int i = 0; i < level.platforms.size(); i++)
      {
        final Platform platform = level.platforms.get(i);

        if (player.velocityX < 0.0f && EntityUtils.overlaps(player, platform))
        {
          player.setX(platform.getPosition().x + platform.getWidthOffset() + adjustment);
          player.velocityX = 0.0f;
        }
        else if (player.velocityX > 0.0f && EntityUtils.overlaps(player, platform))
        {
          player.setX(platform.getPosition().x - platform.getWidthOffset() - adjustment);
          player.velocityX = 0.0f;
        }
      }
    }

    if (yAxis)
    {
      final float adjustment = 3.25f;

      for (int i = 0; i < level.platforms.size(); i++)
      {
        final Platform platform = level.platforms.get(i);

        if (player.velocityY < 0.0f && EntityUtils.overlaps(player, platform))
        {
          player.setY(platform.getPosition().y + platform.getHeightOffset() + adjustment);
          player.velocityY = 0.0f;
          player.setLocation(Player.Location.PLATFORM);

          if (player.state == Player.State.JUMPING)
          {
            player.setState(Player.State.LANDING);
          }
          else if (player.state == Player.State.LANDING && (player.animation == null || player.animation.isAtEnd()))
          {
            player.setState(Player.State.IDLE);
          }

          if (player.velocityX > FRICTION)
          {
            player.velocityX = player.velocityX - FRICTION;
          }
          else if (player.velocityX < -FRICTION)
          {
            player.velocityX = player.velocityX + FRICTION;
          }
          else
          {
            player.velocityX = 0.0f;
          }
        }
        else if (player.velocityY > 0.0f && EntityUtils.overlaps(player, platform))
        {
          player.setY(platform.getPosition().y - platform.getHeightOffset() - adjustment);
          player.velocityY = 0.0f;
        }
      }
    }
  }

  public void clear()
  {
    players = null;
    level = null;
    delta = 0.0f;
  }
}
