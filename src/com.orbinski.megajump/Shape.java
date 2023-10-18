package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;

class Shape
{
  enum Type
  {
    QUAD,
    FILLED_QUAD,
    LINE
  }

  Type type;
  boolean render;

  Color color;

  float x;
  float y;
  float width;
  float height;

  float x1;
  float y1;
  float x2;
  float y2;
}
