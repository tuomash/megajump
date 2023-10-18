package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

class Shape
{
  enum Type
  {
    QUAD,
    FILLED_QUAD,
    LINE
  }

  Type type;
  ShapeRenderer.ShapeType shapeType;

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
