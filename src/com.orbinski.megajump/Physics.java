package com.orbinski.megajump;

import java.util.List;

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
      updateEntity(level.exit);
      return;
    }

    // Do exit movement first
    updateEntity(level.exit);

    // Then do player movement and collision detection
    updatePlayers();
  }

  public void updatePlayers()
  {
    if (players == null)
    {
      return;
    }

    for (int i = 0; i < players.size(); i++)
    {
      final Player player = players.get(i);

      if (player.isMoving() && player.state != Player.State.EXIT && player.state != Player.State.DEATH)
      {
        updateEntity(players.get(i));

        if (level.exit != null && level.exit.overlaps(player))
        {
          player.stop();
          player.setState(Player.State.EXIT);
          break;
        }

        if (player.getPosition().y < level.deathPoint.y)
        {
          player.stop();
          player.setState(Player.State.DEATH);
          break;
        }

        player.setLocation(Player.Location.NONE);

        boolean hit = false;

        for (int z = 0; z < level.trampolines.size(); z++)
        {
          final Trampoline trampoline = level.trampolines.get(z);

          // TODO: pull code from class to here
          if (trampoline.apply(player))
          {
            hit = true;
            break;
          }
        }

        if (!hit)
        {
          for (int z = 0; z < level.platforms.size(); z++)
          {
            final Platform platform = level.platforms.get(z);

            // TODO: pull code from class to here
            if (player.overlaps(platform))
            {
              hit = true;
              break;
            }
          }
        }
      }
    }
  }

  private void updateEntity(final Entity entity)
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

  public void clear()
  {
    players = null;
    level = null;
    delta = 0.0f;
  }
}
