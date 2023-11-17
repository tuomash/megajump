package com.orbinski.megajump;

import com.badlogic.gdx.math.Vector2;

public class GameText extends Text
{
  Vector2 textPosition;

  public GameText()
  {
    textPosition = new Vector2();
  }

  public void setPosition(final float x, final float y)
  {
    textPosition.x = x;
    textPosition.y = y;

    // Project world coords to screen coords
    Renderer.project(textPosition);

    super.setPosition((int) textPosition.x, (int) textPosition.y);
  }
}
