package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;

public class Caret extends UIElement
{
  boolean show = false;
  private float timeNeeded = 0.5f;
  private float elapsed = 0.0f;
  Color color = Color.WHITE;

  public Caret()
  {
    visible = false;
    setWidth(12);
    setHeight(24);
  }

  public void update(final float delta)
  {
    elapsed = elapsed + delta;

    if (elapsed > timeNeeded)
    {
      show = !show;
      elapsed = 0.0f;
    }
  }
}
