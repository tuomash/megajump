package com.orbinski.megajump;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity
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
  public boolean interpolate;

  private float width;
  private float widthOffset;
  private float height;
  private float heightOffset;

  Movement movement = Movement.NO_MOVEMENT;

  boolean drawCollisionBox = false;
  Rectangle collisionBox;
  Vector2 collisionBoxPos;
  Vector2 collisionBoxPrevPos;
  float collisionBoxWidthOffset;
  float collisionBoxHeightOffset;

  public boolean applyGravity = true;

  boolean applyWidthOffset = true;
  boolean applyHeightOffset = true;

  private List<Point2D.Float> waypoints;
  Point2D.Float currentWaypoint;
  int waypointIndex;

  public float velocityX;
  public float velocityY;
  boolean drawBorder = false;
  boolean visible = true;
  boolean selected = false;
  boolean dead;
  Sound moveSound;
  Sound deathSound;

  Animation prevAnimation;
  Animation animation;
  MTexture texture;

  public Entity()
  {
    setWidth(2.5f);
    setHeight(2.5f);
  }

  public Entity(final EntityWrapper wrapper)
  {
    setWidth(wrapper.width);
    setHeight(wrapper.height);
    setPosition(wrapper.x, wrapper.y);
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

  public Entity(final float width, final float height)
  {
    setWidth(width);
    setHeight(height);
  }

  public Entity(final float x, final float y, final float width, final float height)
  {
    setWidth(width);
    setHeight(height);
    setPosition(x, y);
  }

  public Entity(final float x, final float y, final float width, final float height, final boolean collision)
  {
    if (collision)
    {
      collisionBox = new Rectangle();
      collisionBoxPos = new Vector2();
      collisionBoxPrevPos = new Vector2();
    }

    setWidth(width);
    setHeight(height);
    setPosition(x, y);
  }

  protected void copyValues(final Entity copy)
  {
    copy.interpolate = interpolate;
    copy.movement = movement;

    if (collisionBox != null)
    {
      copy.collisionBox = new Rectangle();
      copy.collisionBoxPos = new Vector2();
      copy.collisionBoxPrevPos = new Vector2();
    }

    copy.applyGravity = applyGravity;
    copy.applyWidthOffset = applyWidthOffset;
    copy.applyHeightOffset = applyHeightOffset;

    if (waypoints != null)
    {
      copy.waypoints = new ArrayList<>();

      for (int i = 0; i < waypoints.size(); i++)
      {
        final Point2D.Float existing = waypoints.get(i);
        final Point2D.Float point = new Point2D.Float(existing.x, existing.y);
        copy.waypoints.add(point);
      }
    }

    copy.setWidth(getWidth());
    copy.setHeight(copy.getHeight());
    copy.setPosition(getPosition());
  }

  public void update(final float delta)
  {
    if (dead)
    {
      return;
    }
  }

  public void setPosition(final Vector2 position)
  {
    setPosition(position.x, position.y);
  }

  public void setPosition(final float x, final float y)
  {
    setX(x);
    setY(y);
  }

  void moveToPreviousPosition()
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

  public void stop()
  {
    velocityX = 0.0f;
    velocityY = 0.0f;
  }

  public boolean hasVelocityX()
  {
    return velocityX > 0.0f || velocityX < 0.0f;
  }

  public boolean hasVelocityY()
  {
    return velocityY > 0.0f || velocityY < 0.0f;
  }

  public boolean isMoving()
  {
    return hasVelocityX() || hasVelocityY();
  }

  boolean overlaps(final Entity entity)
  {
    return !this.equals(entity) && EntityUtils.overlaps(this, entity);
  }

  public boolean contains(final float x, final float y)
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

  Vector2 getBottomLeftCornerPosition()
  {
    return bottomLeftCornerPosition;
  }

  Vector2 getPreviousBottomLeftCornerPosition()
  {
    return previousBottomLeftCornerPosition;
  }

  public void setX(final float x)
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
      collisionBoxPrevPos.x = collisionBox.x;
      collisionBox.setX(x - collisionBoxWidthOffset);
      collisionBoxPos.x = collisionBox.x;
    }
  }

  public void setY(final float y)
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
      collisionBoxPrevPos.y = collisionBox.y;
      collisionBox.setY(y - collisionBoxHeightOffset);
      collisionBoxPos.y = collisionBox.y;
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
      collisionBoxWidthOffset = width / 2.0f;
    }

    setPosition(getPosition());
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
      collisionBoxHeightOffset = height / 2.0f;
    }

    setPosition(getPosition());
  }

  float getHeightOffset()
  {
    return heightOffset;
  }

  public void updateVelocityX(final float input)
  {
    velocityX = velocityX + input;
    velocityX = clampVelocityX(velocityX);
  }

  public float clampVelocityX(final float input)
  {
    if (input > Globals.MAX_VELOCITY_X)
    {
      return Globals.MAX_VELOCITY_X;
    }
    else if (input < -Globals.MAX_VELOCITY_X)
    {
      return -Globals.MAX_VELOCITY_X;
    }

    return input;
  }

  public void updateVelocityY(final float input)
  {
    velocityY = velocityY + input;
    velocityY = clampVelocityY(velocityY);
  }

  public float clampVelocityY(final float input)
  {
    if (input > Globals.MAX_VELOCITY_Y)
    {
      return Globals.MAX_VELOCITY_Y;
    }
    else if (input < -Globals.MAX_VELOCITY_Y)
    {
      return -Globals.MAX_VELOCITY_Y;
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

  public boolean interpolate()
  {
    return interpolate;
  }

  @Override
  public boolean equals(final Object obj)
  {
    // TODO: use Entity.id when it's done
    return super.equals(obj) && this == obj;
  }
}
