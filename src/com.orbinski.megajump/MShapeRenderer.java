package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

class MShapeRenderer
{
  final Viewport viewport;
  final ShapeRenderer shapeRenderer;

  final Shape[] shapes;
  int shapeIndex = -1;

  MShapeRenderer(final Viewport viewport, final int shapeLimit)
  {
    this.viewport = viewport;

    shapeRenderer = new ShapeRenderer();
    shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
    shapeRenderer.setAutoShapeType(true);

    shapes = new Shape[shapeLimit];

    for (int i = 0; i < shapes.length; i++)
    {
      shapes[i] = new Shape();
    }
  }

  void update()
  {
    shapeIndex = -1;
    shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
  }

  void addQuad(final float x,
               final float y,
               final float width,
               final float height,
               final Color color)
  {
    shapeIndex++;

    if (shapeIndex >= shapes.length)
    {
      shapeIndex = shapes.length - 1;
    }

    final Shape shape = shapes[shapeIndex];
    shape.type = Shape.Type.QUAD;
    shape.shapeType = ShapeRenderer.ShapeType.Line;
    shape.color = color;
    shape.x = x;
    shape.y = y;
    shape.width = width;
    shape.height = height;
  }

  void addFilledQuad(final float x,
                     final float y,
                     final float width,
                     final float height,
                     final Color color)
  {
    shapeIndex++;

    if (shapeIndex >= shapes.length)
    {
      shapeIndex = shapes.length - 1;
    }

    final Shape shape = shapes[shapeIndex];
    shape.type = Shape.Type.FILLED_QUAD;
    shape.shapeType = ShapeRenderer.ShapeType.Filled;
    shape.color = color;
    shape.x = x;
    shape.y = y;
    shape.width = width;
    shape.height = height;
  }

  void addLine(final float x1,
               final float y1,
               final float x2,
               final float y2,
               final Color color)
  {
    shapeIndex++;

    if (shapeIndex >= shapes.length)
    {
      shapeIndex = shapes.length - 1;
    }

    final Shape shape = shapes[shapeIndex];
    shape.type = Shape.Type.LINE;
    shape.shapeType = ShapeRenderer.ShapeType.Line;
    shape.color = color;
    shape.x1 = x1;
    shape.y1 = y1;
    shape.x2 = x2;
    shape.y2 = y2;
  }

  void renderShapes()
  {
    if (shapeIndex == -1)
    {
      return;
    }

    shapeRenderer.begin();

    for (int i = 0; i <= shapeIndex; i++)
    {
      final Shape shape = shapes[i];

      if (shape != null && shape.color != null)
      {
        shapeRenderer.setColor(shape.color.r, shape.color.g, shape.color.b, shape.color.a);
        shapeRenderer.set(shape.shapeType);

        if (shape.type == Shape.Type.QUAD || shape.type == Shape.Type.FILLED_QUAD)
        {
          shapeRenderer.rect(shape.x, shape.y, shape.width, shape.height);
        }
        else if (shape.type == Shape.Type.LINE)
        {
          shapeRenderer.line(shape.x1, shape.y1, shape.x2, shape.y2);
        }
      }
    }

    shapeRenderer.end();
  }

  void dispose()
  {
    shapeRenderer.dispose();
  }
}
