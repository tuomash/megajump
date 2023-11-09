package com.orbinski.megajump;

class Player extends Entity
{
  enum Direction
  {
    LEFT,
    RIGHT
  }

  enum State
  {
    IDLE,
    JUMPING,
    LANDING,
    EXIT,
    DEATH
  }

  enum Position
  {
    START,
    NONE,
    PLATFORM
  }

  Direction direction;
  State state;
  private Position position;

  final float maxJumpVelocityX = 65.0f;
  final float maxJumpVelocityY = 75.0f;

  final JumpAssistant assistant;

  Player()
  {
    super(-75.0f, -30.0f, 10.0f, 10.0f, true);

    movement = Movement.REGULAR;
    assistant = new JumpAssistant(this);
    // drawBorder = true;
    // drawCollisions = true;

    topSide.height = 0.5f;

    leftSide.height = 5.25f;
    leftSide.width = 0.5f;

    rightSide.height = 5.25f;
    rightSide.width = 0.5f;

    bottomSide.height = 0.5f;

    setDirection(Direction.RIGHT);
    setState(State.IDLE);
    setPosition(Position.START);
  }

  @Override
  void updatePhysics(final float delta)
  {
    if (state == State.EXIT || state == State.DEATH)
    {
      return;
    }

    super.updatePhysics(delta);
  }

  @Override
  void update(final float delta)
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

  void jump()
  {
    assistant.targeting = false;
    assistant.jump();
  }

  void moveUp()
  {
    if (state == State.JUMPING)
    {
      updateVelocityY(0.5f);
    }
  }

  void moveLeft()
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

  void moveRight()
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

  void moveDown()
  {
    if (state == State.JUMPING)
    {
      updateVelocityY(-0.5f);
    }
  }

  @Override
  boolean overlaps(final Entity entity)
  {
    boolean overlaps = false;

    if (velocityY < 0.0f && EntityUtils.overlaps(bottomSide, entity))
    {
      overlaps = true;
      setY(entity.getY() + entity.getHeightOffset() + 3.0f);
      velocityY = 0.0f;
      setPosition(Position.PLATFORM);

      if (state == State.JUMPING)
      {
        setState(State.LANDING);
      }
      else if (state == State.LANDING && (animation == null || animation.isAtEnd()))
      {
        setState(State.IDLE);
      }

      final float friction = 0.8f;

      if (velocityX > friction)
      {
        velocityX = velocityX - friction;
      }
      else if (velocityX < -friction)
      {
        velocityX = velocityX + friction;
      }
      else
      {
        velocityX = 0.0f;
      }
    }
    else if (velocityY > 0.0f && EntityUtils.overlaps(topSide, entity))
    {
      overlaps = true;
      setY(entity.getY() - entity.getHeightOffset() - 3.0f);
      velocityY = 0.0f;
    }
    else if (velocityX < 0.0f && EntityUtils.overlaps(leftSide, entity))
    {
      overlaps = true;
      setX(entity.getX() + entity.getWidthOffset() + 1.8f);
      velocityX = 0.0f;
    }
    else if (velocityX > 0.0f && EntityUtils.overlaps(rightSide, entity))
    {
      overlaps = true;
      setX(entity.getX() - entity.getWidthOffset() - 1.8f);
      velocityX = 0.0f;
    }

    return overlaps;
  }

  @Override
  void setX(final float x)
  {
    super.setX(x);
    topSide.x = x - topSide.width * 0.5f;
    leftSide.x = x - getWidthOffset() + leftSide.width + 2.75f;
    rightSide.x = x + getWidthOffset() - rightSide.width - 3.25f;
    bottomSide.x = x - bottomSide.width * 0.5f;
    collisionBox.setX(x - collisionBox.width * 0.5f);
  }

  @Override
  void setY(final float y)
  {
    super.setY(y);
    topSide.y = y + 2.5f;
    leftSide.y = y - leftSide.height * 0.5f - 0.275f;
    rightSide.y = y - rightSide.height * 0.5f - 0.275f;
    bottomSide.y = getBottomLeftCornerY() + 1.5f;
    collisionBox.setY(y - collisionBox.height * 0.5f);
  }

  @Override
  void setWidth(final float width)
  {
    super.setWidth(width);
    topSide.width = width * 0.55f;
    bottomSide.width = width * 0.55f;
    collisionBox.setWidth(width * 0.55f);
  }

  @Override
  void setHeight(final float height)
  {
    super.setHeight(height);
    collisionBox.setHeight(height * 0.8f);
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

  void setState(final State state)
  {
    if (state != null && this.state != state)
    {
      this.state = state;
      updateAnimationState(true);
    }
  }

  public Position getPosition()
  {
    return position;
  }

  void setPosition(final Position position)
  {
    if (position != null && this.position != position)
    {
      this.position = position;
    }
  }

  void updateAnimationState(final boolean reset)
  {
    switch (this.state)
    {
      case IDLE:
      {
        if (direction == Direction.LEFT)
        {
          setAnimation(Animations.playerIdleLeft);
        }
        else
        {
          setAnimation(Animations.playerIdleRight);
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
          setAnimation(Animations.playerLandLeft);
        }
        else
        {
          setAnimation(Animations.playerLandRight);
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

  boolean canJump()
  {
    return assistant.canJump();
  }

  void reset()
  {
    assistant.reset();
    applyGravity = false;
    stop();
    state = null;
    setDirection(Direction.RIGHT);
    setState(State.IDLE);
    setPosition(Position.START);
    UserInterface.jumpBar.reset();
  }
}
