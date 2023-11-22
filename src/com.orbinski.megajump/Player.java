package com.orbinski.megajump;

public class Player extends Entity
{
  enum Direction
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

  enum Location
  {
    START,
    NONE,
    PLATFORM
  }

  public int id = 1;
  private String name;
  public final GameText playerNameText = new GameText();

  Direction direction;
  public State state;
  private Location location;

  public final float maxJumpVelocityX = 65.0f;
  public final float maxJumpVelocityY = 75.0f;

  public final JumpAssistant assistant;

  private Animation idleLeft;
  private Animation idleRight;
  private Animation landLeft;
  private Animation landRight;

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

    setDirection(Direction.RIGHT);
    setState(State.IDLE);
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

  public void jump()
  {
    assistant.targeting = false;
    assistant.jump();
  }

  public void moveUp()
  {
    if (state == State.JUMPING)
    {
      updateVelocityY(0.5f);
    }
  }

  public void moveLeft()
  {
    if (state == State.JUMPING)
    {
      updateVelocityX(-0.5f, false);
    }
    else if (state == State.LANDING)
    {
      updateVelocityX(-0.15f);
    }

    if (setDirection(Direction.LEFT))
    {
      updateAnimationState(false);
    }
  }

  public void moveRight()
  {
    if (state == State.JUMPING)
    {
      updateVelocityX(0.5f, false);
    }
    else if (state == State.LANDING)
    {
      updateVelocityX(0.15f);
    }

    if (setDirection(Direction.RIGHT))
    {
      updateAnimationState(false);
    }
  }

  public void moveDown()
  {
    if (state == State.JUMPING)
    {
      updateVelocityY(-0.5f);
    }
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

  boolean setDirection(final Direction direction)
  {
    if (direction != null && this.direction != direction)
    {
      this.direction = direction;
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

  public boolean canJump()
  {
    return assistant.canJump();
  }

  public boolean interpolate()
  {
    return interpolate && isMoving();
  }

  public void reset()
  {
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
