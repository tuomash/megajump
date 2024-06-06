package com.orbinski.megajump;

public class Player extends Entity
{
  public enum Direction
  {
    LEFT,
    RIGHT
  }

  public enum State
  {
    IDLE,
    JUMPING,
    LANDING,
    WALL_LANDING,
    EXIT,
    DEATH
  }

  public enum Location
  {
    START,
    NONE,
    PLATFORM
  }

  public enum VerticalMovement
  {
    UP,
    DOWN,
    NONE
  }

  public enum HorizontalMovement
  {
    LEFT,
    RIGHT,
    NONE
  }

  public int id = 1;
  private String name;
  public final GameText playerNameText = new GameText();

  Direction direction;
  private Direction previousDirection = Direction.LEFT;
  public State state;
  private Location location;
  public VerticalMovement verticalMovement = VerticalMovement.NONE;
  public HorizontalMovement horizontalMovement = HorizontalMovement.NONE;

  public final JumpAssistant assistant;

  private Animation idleLeft;
  private Animation idleRight;
  private Animation landLeft;
  private Animation landRight;
  private Animation wallLandLeft;
  private Animation wallLandRight;

  float touchedFor;
  int chainedJumps = 0;

  public Player()
  {
    super(-75.0f, -30.0f, 10.0f, 10.0f, true);

    setName("Player");
    playerNameText.visible = true;
    playerNameText.font = Resources.font14White;

    movement = Movement.REGULAR;
    assistant = new JumpAssistant(this);
    // drawBorder = true;
    // drawCollisionBox = true;

    setState(State.IDLE);
    setDirection(Direction.RIGHT);
    setLocation(Location.START);

    if (Animations.playerIdleLeft != null)
    {
      idleLeft = Animations.playerIdleLeft.copy();
    }

    if (Animations.playerIdleRight != null)
    {
      idleRight = Animations.playerIdleRight.copy();
    }

    if (Animations.playerLandLeft != null)
    {
      landLeft = Animations.playerLandLeft.copy();
    }

    if (Animations.playerLandRight != null)
    {
      landRight = Animations.playerLandRight.copy();
    }

    if (Animations.playerWallLandLeft != null)
    {
      wallLandLeft = Animations.playerWallLandLeft.copy();
    }

    if (Animations.playerWallLandRight != null)
    {
      wallLandRight = Animations.playerWallLandRight.copy();
    }
  }

  @Override
  public void update(final float delta)
  {
    if (state == State.EXIT || state == State.DEATH)
    {
      return;
    }

    super.update(delta);
    UserInterface.updateSpeedText((int) velocityX, (int) velocityY);
    assistant.update(delta);

    if (state == State.JUMPING)
    {
      if (velocityY > 10.0f)
      {
        if (direction == Direction.LEFT)
        {
          texture = Resources.playerJumpLeftAscend;
        }
        else
        {
          texture = Resources.playerJumpRightAscend;
        }
      }
      else if (velocityY < -10.0f)
      {
        if (direction == Direction.LEFT)
        {
          texture = Resources.playerJumpLeftDescend;
        }
        else
        {
          texture = Resources.playerJumpRightDescend;
        }
      }
      else
      {
        if (direction == Direction.LEFT)
        {
          texture = Resources.playerJumpLeftMax;
        }
        else
        {
          texture = Resources.playerJumpRightMax;
        }
      }
    }
    else
    {
      if (animation != null)
      {
        animation.update(delta);
        texture = animation.getFrame().texture;
      }
    }
  }

  public void updateTouchedFor(final float delta)
  {
    touchedFor = touchedFor + delta;
  }

  public void jump()
  {
    assistant.targeting = false;
    assistant.jump();
  }

  public void clearMovement()
  {
    verticalMovement = VerticalMovement.NONE;
    horizontalMovement = HorizontalMovement.NONE;
  }

  public String getName()
  {
    return name;
  }

  public void setName(final String name)
  {
    this.name = name;
    playerNameText.text = name;
  }

  @Override
  public void setPosition(final float x, final float y)
  {
    setPosition(x, y, true);
  }

  public void setPosition(final float x, final float y, final boolean updateNamePosition)
  {
    super.setPosition(x, y);

    if (updateNamePosition)
    {
      updatePlayerNameTextPosition();
    }
  }

  public void updatePlayerNameTextPosition()
  {
    if (playerNameText != null)
    {
      playerNameText.setPosition(getPosition().x - 2.5f, getPosition().y + 5.0f);
    }
  }

  @Override
  void setWidth(final float width)
  {
    super.setWidth(width);
    collisionBox.setWidth(width * 0.45f);
    collisionBoxWidthOffset = collisionBox.getWidth() / 2.0f;
  }

  @Override
  void setHeight(final float height)
  {
    super.setHeight(height);
    collisionBox.setHeight(height * 0.65f);
    collisionBoxHeightOffset = collisionBox.getHeight() / 2.0f;
  }

  @Override
  public float getRenderOffsetX()
  {
    if (animation != null && animation.renderOffsetX)
    {
      if (direction == Direction.RIGHT)
      {
        return 1.0f;
      }
      else if (direction == Direction.LEFT)
      {
        return -1.0f;
      }
    }

    return 0.0f;
  }

  boolean setDirection(final Direction direction)
  {
    if (direction != null && this.direction != direction)
    {
      previousDirection = this.direction;
      this.direction = direction;

      if (state == State.WALL_LANDING)
      {
        setState(State.JUMPING);
      }
      else
      {
        updateAnimationState(false);
      }

      return true;
    }

    return false;
  }

  public void setState(final State state)
  {
    if (state != null && this.state != state)
    {
      this.state = state;
      updateAnimationState(true);
    }
  }

  public Location getLocation()
  {
    return location;
  }

  void setLocation(final Location location)
  {
    if (location != null && this.location != location)
    {
      this.location = location;
    }
  }

  public void updateAnimationState(final boolean reset)
  {
    if (this.state != null)
    {
      switch (this.state)
      {
        case IDLE:
        {
          if (direction == Direction.LEFT)
          {
            setAnimation(idleLeft);
          }
          else
          {
            setAnimation(idleRight);
          }

          break;
        }

        case JUMPING:
        {
          setAnimation(null);

          break;
        }

        case LANDING:
        {
          if (direction == Direction.LEFT)
          {
            setAnimation(landLeft);
          }
          else
          {
            setAnimation(landRight);
          }

          break;
        }

        case WALL_LANDING:
        {
          if (direction == Direction.LEFT)
          {
            setAnimation(wallLandLeft);
          }
          else
          {
            setAnimation(wallLandRight);
          }

          break;
        }
      }

      if (animation != null)
      {
        if (reset)
        {
          animation.reset();
        }
        else if (prevAnimation != null && animation.groupId == prevAnimation.groupId)
        {
          animation.mergeState(prevAnimation);
        }
      }
    }
  }

  public boolean canJump()
  {
    return assistant.canJump();
  }

  @Override
  public boolean interpolate()
  {
    return interpolate && isMoving();
  }

  public void resetTouchedFor()
  {
    touchedFor = 0.0f;
  }

  public void setVerticalMovement(final VerticalMovement verticalMovement)
  {
    this.verticalMovement = verticalMovement;
  }

  public void setHorizontalMovement(final HorizontalMovement horizontalMovement)
  {
    this.horizontalMovement = horizontalMovement;

    if (horizontalMovement == HorizontalMovement.LEFT)
    {
      setDirection(Player.Direction.LEFT);
    }
    else if (horizontalMovement == HorizontalMovement.RIGHT)
    {
      setDirection(Direction.RIGHT);
    }
  }

  public void reset()
  {
    resetTouchedFor();
    assistant.reset();
    applyGravity = false;
    stop();
    state = null;
    setDirection(Direction.RIGHT);
    setState(State.IDLE);
    setLocation(Location.START);

    if (UserInterface.jumpBar != null)
    {
      UserInterface.jumpBar.reset();
    }
  }
}
