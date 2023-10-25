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
    LANDING
  }

  Direction direction;
  State state;

  final float maxVelocityX = 70.0f;
  final float maxVelocityY = 80.0f;
  final float cursorWidth = 0.5f;
  final float cursorHeight = 0.5f;

  float cursorX;
  float cursorY;
  boolean targeting;

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
  }

  void update(final float delta)
  {
    super.update(delta);

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
    if (!moving)
    {
      moving = true;
      UserInterface.retryText.visible = false;

      final float maxDiffX = 40.0f;
      final float playerWorldX = getX();
      float diffX = Math.abs(cursorX - playerWorldX);

      if (diffX > maxDiffX)
      {
        diffX = maxDiffX;
      }

      final float percentageX = diffX / maxDiffX;
      velocityX = maxVelocityX * percentageX;

      if (velocityX > maxVelocityX)
      {
        velocityX = maxVelocityX;
      }

      if (cursorX < getX())
      {
        velocityX = -velocityX;
      }

      final float maxDiffY = 40.0f;
      final float playerWorldY = getY();
      float diffY = Math.abs(cursorY - playerWorldY);

      if (diffY > maxDiffY)
      {
        diffY = maxDiffY;
      }

      final float percentageY = diffY / maxDiffY;
      velocityY = maxVelocityY * percentageY;

      if (velocityY > maxVelocityY)
      {
        velocityY = maxVelocityY;
      }

      if (cursorY < getY())
      {
        velocityY = -velocityY;
      }

      if (velocityX > 0.0f)
      {
        setDirection(Direction.RIGHT);
      }
      else if (velocityX < 0.0f)
      {
        setDirection(Direction.LEFT);
      }

      setState(State.JUMPING);
    }
  }

  void moveUp()
  {
    if (moving && state == State.JUMPING)
    {
      velocityY = velocityY + 0.5f;
    }
  }

  void moveLeft()
  {
    if (moving && state == State.JUMPING)
    {
      velocityX = velocityX - 0.5f;
    }

    setDirection(Direction.LEFT);
  }

  void moveRight()
  {
    if (moving && state == State.JUMPING)
    {
      velocityX = velocityX + 0.5f;
    }

    setDirection(Direction.RIGHT);
  }

  void moveDown()
  {
    if (moving && state == State.JUMPING)
    {
      velocityY = velocityY - 0.5f;
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
        moving = false;
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
        setY(entity.getY() + entity.getHeightOffset() + collisionBox.height / 2.0f - 0.5f);
        velocityY = 0.0f;

        if (velocityX > 0.3f)
        {
          velocityX = velocityX - 0.3f;
        }
        else if (velocityX < -0.3f)
        {
          velocityX = velocityX + 0.3f;
        }
        else
        {
          moving = false;
          setState(State.IDLE);
          velocityX = 0.0f;
          return overlaps;
        }

        applyGravity = false;
        bottomSideCollision = true;

        if (state != State.LANDING)
        {
          setState(State.LANDING);
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

  void setDirection(final Direction direction)
  {
    if (direction != null && this.direction != direction)
    {
      this.direction = direction;
    }
  }

  void setState(final State state)
  {
    if (state != null && this.state != state)
    {
      this.state = state;

      switch (state)
      {
        case IDLE:
        {
          if (direction == Direction.LEFT)
          {
            animation = Animations.playerIdleLeft;
          }
          else
          {
            animation = Animations.playerIdleRight;
          }

          break;
        }

        case JUMPING:
        {
          animation = null;

          break;
        }

        case LANDING:
        {
          if (direction == Direction.LEFT)
          {
            animation = Animations.playerLandLeft;
          }
          else
          {
            animation = Animations.playerLandRight;
          }

          break;
        }
      }

      if (animation != null)
      {
        animation.reset();
      }
    }
  }

  void reset()
  {
    moving = false;
    targeting = false;
    setX(-75.0f);
    setY(-30.0f);
    velocityX = 0.0f;
    velocityY = 0.0f;
    state = null;
    setDirection(Direction.RIGHT);
    setState(State.IDLE);
  }
}
