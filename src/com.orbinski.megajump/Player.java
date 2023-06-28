package com.orbinski.megajump;

class Player
{
  enum Direction
  {
    UP,
    LEFT,
    RIGHT,
    DOWN
  }

  private float x = 0.0f;
  private float y = -25.0f;
  float rx;
  float ry = y;
  float width = 20.0f;
  float height = 10.0f;
  Direction direction;

  void update()
  {
    if (direction != null)
    {
      switch (direction)
      {
        case LEFT -> x = -50.0f;
        case RIGHT -> x = 50.0f;
      }
    }
    else
    {
      x = 0.0f;
    }

    // TODO: calculate offset before?
    rx = x - (width / 2.0f);
  }
}
