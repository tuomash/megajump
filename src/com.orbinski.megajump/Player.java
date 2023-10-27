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
  final float cursorWidth = 0.5f;
  final float cursorHeight = 0.5f;

  float cursorX;
  float cursorY;
  boolean targeting;

  private float jumpElapsed;
  private final float jumpTarget = 1.25f;
  private boolean canJump = true;

  Player()
  {
    super(-75.0f, -30.0f, 10.0f, 10.0f, true);

    movement = Movement.REGULAR;
    // drawBorder = true;
    // drawCollisions = true;

    // rightSide.height = getHeight() * 0.75f;

    bottomSide.height = 0.5f;

    setDirection(Direction.RIGHT);
    setState(State.IDLE);
    setPosition(Position.START);
  }

  void update(final float delta)
  {
    if (state == State.EXIT || state == State.DEATH)
    {
      return;
    }

    super.update(delta);
    UserInterface.updateSpeedText((int) velocityX, (int) velocityY);

    if (!canJump)
    {
      jumpElapsed = jumpElapsed + delta;
      UserInterface.jumpBar.updateBar(jumpElapsed, jumpTarget);

      if (jumpElapsed > jumpTarget)
      {
        jumpElapsed = 0.0f;
        canJump = true;
      }
    }

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

  void updateCursorLocation(final float x, final float y)
  {
    this.cursorX = x;
    this.cursorY = y;
  }

  void jump()
  {
    if (canJump())
    {
      applyGravity = true;
      UserInterface.retryText.visible = false;

      final float maxDiffX = 40.0f;
      final float playerWorldX = getX();
      float diffX = Math.abs(cursorX - playerWorldX);

      if (diffX > maxDiffX)
      {
        diffX = maxDiffX;
      }

      final float percentageX = diffX / maxDiffX;
      float jumpVelocityX = maxJumpVelocityX * percentageX;

      // Clamp max jump x velocity
      if (jumpVelocityX > maxJumpVelocityX)
      {
        jumpVelocityX = maxJumpVelocityX;
      }

      if (cursorX < getX())
      {
        jumpVelocityX = -jumpVelocityX;
      }

      // Add jump x velocity existing velocity
      // Only add if max x velocity has not been reached
      if (velocityX < maxVelocityX && velocityX > -maxVelocityX)
      {
        updateVelocityX(jumpVelocityX);
      }

      final float maxDiffY = 40.0f;
      final float playerWorldY = getY();
      float diffY = Math.abs(cursorY - playerWorldY);

      if (diffY > maxDiffY)
      {
        diffY = maxDiffY;
      }

      final float percentageY = diffY / maxDiffY;
      float jumpVelocityY = maxJumpVelocityY * percentageY;

      // Clamp max jump y velocity
      if (jumpVelocityY > maxJumpVelocityY)
      {
        jumpVelocityY = maxJumpVelocityY;
      }

      if (cursorY < getY())
      {
        jumpVelocityY = -jumpVelocityY;
      }

      // Add jump y velocity existing velocity
      // Only add if max y velocity has not been reached
      if (velocityY < maxVelocityY && velocityY > -maxVelocityY)
      {
        updateVelocityY(jumpVelocityY);
      }

      if (velocityX > 0.0f)
      {
        setDirection(Direction.RIGHT);
      }
      else if (velocityX < 0.0f)
      {
        setDirection(Direction.LEFT);
      }

      canJump = false;
      setState(State.JUMPING);
    }
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
    if (entity.isDoor())
    {
      return super.overlaps(entity);
    }
    else if (entity.isBlock())
    {
      final boolean overlaps = super.overlaps(entity);

      if (overlaps)
      {
        stop();
        moveToPreviousLocation();
        setState(State.IDLE);
      }

      return overlaps;

      /* TODO: implement proper collision detection
      boolean overlaps = false;

      if (!rightSideCollision && EntityUtils.overlaps(rightSide, entity))
      {
        setX(entity.getBottomLeftCornerX() - getWidthOffset());
        velocityX = 0.0f;
        overlaps = true;
        rightSideCollision = true;
      }
      else if (!bottomSideCollision && EntityUtils.overlaps(bottomSide, entity))
      {
        setY(entity.getY() + entity.getHeightOffset() + getHeightOffset());
        velocityX = 0.0f;
        velocityY = 0.0f;
        moving = false;
        overlaps = true;
        applyGravity = false;
        bottomSideCollision = true;
      }

      return overlaps;
       */
    }
    else if (entity.isPlatform())
    {
      boolean overlaps = false;

      if (!bottomSideCollision && EntityUtils.overlaps(bottomSide, entity))
      {
        overlaps = true;
        setY(entity.getY() + entity.getHeightOffset() + collisionBox.height / 2.0f - 0.7f);
        velocityY = 0.0f;
        setPosition(Position.PLATFORM);
        bottomSideCollision = true;

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

      return overlaps;
    }

    return false;
  }

  @Override
  void setX(final float x)
  {
    super.setX(x);
    // rightSide.x = x + getWidthOffset() - rightSide.width;
    bottomSide.x = x - bottomSide.width * 0.5f;
    collisionBox.setX(x - collisionBox.width * 0.5f);
  }

  @Override
  void setY(final float y)
  {
    super.setY(y);
    // rightSide.y = y - rightSide.height * 0.5f;
    bottomSide.y = getBottomLeftCornerY() + 1.5f;
    collisionBox.setY(y - collisionBox.height * 0.5f);
  }

  @Override
  void setWidth(final float width)
  {
    super.setWidth(width);
    bottomSide.width = width * 0.55f;
    collisionBox.setWidth(width * 0.55f);
  }

  @Override
  void setHeight(final float height)
  {
    super.setHeight(height);
    // rightSide.height = height * 0.5f;
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
    return canJump && (position == Position.START || position == Position.PLATFORM);
  }

  void reset()
  {
    targeting = false;
    canJump = true;
    jumpElapsed = 0.0f;
    setX(-75.0f);
    setY(-30.0f);
    applyGravity = false;
    stop();
    state = null;
    setDirection(Direction.RIGHT);
    setState(State.IDLE);
    setPosition(Position.START);
    UserInterface.jumpBar.reset();
  }
}
