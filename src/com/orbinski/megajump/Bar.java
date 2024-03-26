package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;

class Bar extends UIElement
{
  private float percentage;
  int barWidth;
  Color color;

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
