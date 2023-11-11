package com.orbinski.megajump;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

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

  private final Vector2 position = new Vector2();
  private final Vector2 previousPosition = new Vector2();
  private final Vector2 bottomLeftCornerPosition = new Vector2();
  private final Vector2 previousBottomLeftCornerPosition = new Vector2();

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

  Entity(final EntityWrapper wrapper)
  {
    setWidth(wrapper.width);
    setHeight(wrapper.height);
    setX(wrapper.x);
    setY(wrapper.y);
    movement = Movement.valueOf(wrapper.movement.toUpperCase());

    if (wrapper.waypoints != null && !wrapper.waypoints.isEmpty())
    {
      waypoints = new ArrayList<>();

      for (int i = 0; i < wrapper.waypoints.size(); i++)
      {
        waypoints.add(wrapper.waypoints.get(i));
      }
    }
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

  void updatePhysics(final float delta)
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
        setX(position.x + distanceX);
        setY(position.y + distanceY);

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

        final float distanceToTarget = MathUtils.distance(position.x, position.y, currentWaypoint.x, currentWaypoint.y);

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

        final float toTargetX = (currentWaypoint.x - position.x) / distanceToTarget;
        final float toTargetY = (currentWaypoint.y - position.y) / distanceToTarget;
        final float distanceX = this.velocityX * delta;
        final float distanceY = this.velocityY * delta;

        setX(position.x + (toTargetX * distanceX));
        setY(position.y + (toTargetY * distanceY));

        break;
      }

      case NO_MOVEMENT:
      {
        break;
      }
    }
  }

  void update(final float delta)
  {
    if (dead)
    {
      return;
    }
  }

  void moveToPreviousLocation()
  {
    moveToPreviousX();
    moveToPreviousY();
  }

  void moveToPreviousX()
  {
    setX(previousPosition.x);
  }

  void moveToPreviousY()
  {
    setY(previousPosition.y);
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

  public Vector2 getPosition()
  {
    return position;
  }

  public Vector2 getPreviousPosition()
  {
    return previousPosition;
  }

  public Vector2 getBottomLeftCornerPosition()
  {
    return bottomLeftCornerPosition;
  }

  public Vector2 getPreviousBottomLeftCornerPosition()
  {
    return previousBottomLeftCornerPosition;
  }

  void setX(final float x)
  {
    previousPosition.x = position.x;
    position.x = x;

    previousBottomLeftCornerPosition.x = bottomLeftCornerPosition.x;

    if (applyWidthOffset)
    {
      bottomLeftCornerPosition.x = x - widthOffset;
    }
    else
    {
      bottomLeftCornerPosition.x = x;
    }

    if (collisionBox != null)
    {
      collisionBox.setX(bottomLeftCornerPosition.x);
    }
  }

  void setY(final float y)
  {
    previousPosition.y = position.y;
    position.y = y;

    previousBottomLeftCornerPosition.y = bottomLeftCornerPosition.y;

    if (applyHeightOffset)
    {
      bottomLeftCornerPosition.y = y - heightOffset;
    }
    else
    {
      bottomLeftCornerPosition.y = y;
    }

    if (collisionBox != null)
    {
      collisionBox.setY(bottomLeftCornerPosition.y);
    }
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

  public List<Point2D.Float> getWaypoints()
  {
    return waypoints;
  }

  boolean isExit()
  {
    return false;
  }

  boolean isSpawn()
  {
    return false;
  }

  boolean isDecoration()
  {
    return false;
  }

  boolean isBlock()
  {
    return false;
  }

  boolean isTrampoline()
  {
    return false;
  }

  boolean isPlatform()
  {
    return false;
  }

  boolean isTeleport()
  {
    return false;
  }

  void increaseWidth()
  {
  }

  void decreaseWidth()
  {
  }

  void increaseHeight()
  {
  }

  void decreaseHeight()
  {
  }

  public void setAnimation(final Animation animation)
  {
    prevAnimation = this.animation;
    this.animation = animation;
  }

  @Override
  public boolean equals(final Object obj)
  {
    // TODO: use Entity.id when it's done
    return super.equals(obj) && this == obj;
  }
}
