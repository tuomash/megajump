package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;

class Bar extends UIElement
{
  static Color DARK_RED = new Color(139.0f / 255.0f, 0.0f, 0.0f, 1.0f);

  private float percentage;
  int barWidth;

  public Bar()
  {
  }

  void updateBar(final float numerator, final float denominator)
  {
    percentage = numerator / denominator;
    barWidth = (int) (getWidth() * percentage);

    if (barWidth > getWidth())
    {
      barWidth = getWidth();
    }
  }

  void reset()
  {
    percentage = 1.0f;
    barWidth = getWidth();
  }
}
