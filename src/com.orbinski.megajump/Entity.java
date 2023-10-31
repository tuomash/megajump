package com.orbinski.megajump;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

abstract class Entity
{
  enum Movement
  {
    REGULAR,
    WAYPOINTS,
    NO_MOVEMENT
  }

  private float x;
  private float y;
  private float prevX;
  private float prevY;
  private float bottomLeftCornerX;
  private float bottomLeftCornerY;
  private float width;
  private float widthOffset;
  private float height;
  private float heightOffset;

  Movement movement = Movement.NO_MOVEMENT;

  boolean drawCollisions = false;
  Rectangle collisionBox;
  Rectangle topSide;
  Rectangle leftSide;
  Rectangle rightSide;
  Rectangle bottomSide;

  boolean applyGravity = true;

  boolean applyWidthOffset = true;
  boolean applyHeightOffset = true;

  private List<Point2D.Float> waypoints;
  Point2D.Float currentWaypoint;
  int waypointIndex;

  final float maxVelocityX = 130.0f;
  final float maxVelocityY = 140.0f;
  float velocityX;
  float velocityY;
  boolean drawBorder = false;
  boolean visible = true;
  boolean selected = false;
  boolean dead;
  Sound moveSound;
  Sound deathSound;

  Animation prevAnimation;
  Animation animation;
  MTexture texture;

  Entity()
  {
    setWidth(2.5f);
    setHeight(2.5f);
  }

  Entity(final float width, final float height)
  {
    setWidth(width);
    setHeight(height);
  }

  Entity(final float x, final float y, final float width, final float height)
  {
    setWidth(width);
    setHeight(height);
    setX(x);
    setY(y);
  }

  Entity(final float x, final float y, final float width, final float height, final boolean collision)
  {
    if (collision)
    {
      collisionBox = new Rectangle();
      topSide = new Rectangle();
      leftSide = new Rectangle();
      rightSide = new Rectangle();
      bottomSide = new Rectangle();
    }

    setWidth(width);
    setHeight(height);
    setX(x);
    setY(y);
  }

  void update(final float delta)
  {
    if (dead)
    {
      return;
    }

    switch (movement)
    {
      case REGULAR:
      {
        if (applyGravity)
        {
          velocityY = velocityY + Globals.GRAVITY;
        }

        final float distanceX = velocityX * delta;
        final float distanceY = velocityY * delta;
        setX(getX() + distanceX);
        setY(getY() + distanceY);

        break;
      }

      case WAYPOINTS:
      {
        // Need at least two waypoints
        if (waypoints == null || waypoints.size() < 2)
        {
          return;
        }

        if (currentWaypoint == null)
        {
          currentWaypoint = waypoints.get(waypointIndex);
        }

        final float distanceToTarget = MathUtils.distance(getX(), getY(), currentWaypoint.x, currentWaypoint.y);

        if (distanceToTarget < 0.5)
        {
          waypointIndex++;

          if (waypointIndex >= waypoints.size())
          {
            waypointIndex = 0;
          }

          currentWaypoint = waypoints.get(waypointIndex);

          return;
        }

        final float toTargetX = (currentWaypoint.x - getX()) / distanceToTarget;
        final float toTargetY = (currentWaypoint.y - getY()) / distanceToTarget;
        final float distanceX = this.velocityX * delta;
        final float distanceY = this.velocityY * delta;

        setX(getX() + (toTargetX * distanceX));
        setY(getY() + (toTargetY * distanceY));

        break;
      }

      case NO_MOVEMENT:
      {
        break;
      }
    }
  }

  void moveToPreviousLocation()
  {
    moveToPreviousX();
    moveToPreviousY();
  }

  void moveToPreviousX()
  {
    setX(prevX);
  }

  void moveToPreviousY()
  {
    setX(prevX);
  }

  void stop()
  {
    velocityX = 0.0f;
    velocityY = 0.0f;
  }

  boolean isMoving()
  {
    return velocityX > 0.0f || velocityX < 0.0f || velocityY > 0.0f || velocityY < 0.0f;
  }

  boolean overlaps(final Entity entity)
  {
    return EntityUtils.overlaps(this, entity);
  }

  boolean contains(final float x, final float y)
  {
    return EntityUtils.contains(this, x, y);
  }

  void reset()
  {
    if (waypoints != null && waypoints.size() >= 2)
    {
      setX(waypoints.get(0).x);
      setY(waypoints.get(0).y);
      currentWaypoint = null;
      waypointIndex = 1;
    }
  }

  float getX()
  {
    return x;
  }

  void setX(final float x)
  {
    prevX = this.x;
    this.x = x;

    if (applyWidthOffset)
    {
      bottomLeftCornerX = x - widthOffset;
    }
    else
    {
      bottomLeftCornerX = x;
    }

    if (collisionBox != null)
    {
      collisionBox.setX(bottomLeftCornerX);
    }
  }

  float getY()
  {
    return y;
  }

  void setY(final float y)
  {
    prevY = this.y;
    this.y = y;

    if (applyHeightOffset)
    {
      bottomLeftCornerY = y - heightOffset;
    }
    else
    {
      bottomLeftCornerY = y;
    }

    if (collisionBox != null)
    {
      collisionBox.setY(bottomLeftCornerY);
    }
  }

  float getPrevX()
  {
    return prevX;
  }

  float getPrevY()
  {
    return prevY;
  }

  float getBottomLeftCornerX()
  {
    return bottomLeftCornerX;
  }

  float getBottomLeftCornerY()
  {
    return bottomLeftCornerY;
  }

  float getWidth()
  {
    return width;
  }

  void setWidth(final float width)
  {
    this.width = width;
    widthOffset = width / 2.0f;

    if (collisionBox != null)
    {
      collisionBox.setWidth(width);
    }
  }

  float getWidthOffset()
  {
    return widthOffset;
  }

  float getHeight()
  {
    return height;
  }

  void setHeight(final float height)
  {
    this.height = height;
    heightOffset = height / 2.0f;

    if (collisionBox != null)
    {
      collisionBox.setHeight(height);
    }
  }

  float getHeightOffset()
  {
    return heightOffset;
  }

  void updateVelocityX(final float input)
  {
    updateVelocityX(input, true);
  }

  void updateVelocityX(final float input, final boolean clamp)
  {
    velocityX = velocityX + input;

    if (clamp)
    {
      velocityX = clampVelocityX(velocityX);
    }
  }

  float clampVelocityX(final float input)
  {
    if (input > maxVelocityX)
    {
      return maxVelocityX;
    }
    else if (input < -maxVelocityX)
    {
      return -maxVelocityX;
    }

    return input;
  }

  void updateVelocityY(final float input)
  {
    velocityY = velocityY + input;
    velocityY = clampVelocityY(velocityY);
  }

  float clampVelocityY(final float input)
  {
    if (input > maxVelocityY)
    {
      return maxVelocityY;
    }
    else if (input < -maxVelocityY)
    {
      return -maxVelocityY;
    }

    return input;
  }

  void addWaypoint(final Point2D.Float waypoint)
  {
    if (waypoints == null)
    {
      waypoints = new ArrayList<>();
    }

    movement = Movement.WAYPOINTS;
    waypoints.add(waypoint);

    // Entity is set to start from the first waypoint
    if (waypoints.size() == 1)
    {
      setX(waypoint.x);
      setY(waypoint.y);
    }
    // First target is the second waypoint
    else if (waypoints.size() == 2)
    {
      waypointIndex = 1;
    }
  }

  boolean isDoor()
  {
    return false;
  }

  boolean isBlock()
  {
    return false;
  }

  boolean isPlatform()
  {
    return false;
  }

  public void setAnimation(final Animation animation)
  {
    prevAnimation = this.animation;
    this.animation = animation;
  }
}
