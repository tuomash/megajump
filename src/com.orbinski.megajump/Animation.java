package com.orbinski.megajump;

import java.util.ArrayList;
import java.util.List;

class Animation
{
  final List<AnimationFrame> frames = new ArrayList<>();

  int index;
  float elapsed;
  boolean loop;
  boolean playing = true;

  void update(final float delta)
  {
    if (playing)
    {
      elapsed = elapsed + delta;

      final AnimationFrame frame = getFrame();

      if (elapsed > frame.length)
      {
        elapsed = 0.0f;
        nextFrame();
      }
    }
  }

  private void nextFrame()
  {
    index++;

    if (index >= frames.size())
    {
      if (loop)
      {
        index = 0;
      }
      else
      {
        index = frames.size() - 1;
        playing = false;
      }
    }
  }

  AnimationFrame getFrame()
  {
    return frames.get(index);
  }
}
