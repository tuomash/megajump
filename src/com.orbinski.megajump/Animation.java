package com.orbinski.megajump;

import java.util.ArrayList;
import java.util.List;

class Animation
{
  final List<AnimationFrame> frames = new ArrayList<>();

  private int index;
  private float elapsed;

  int groupId;
  boolean loop;
  boolean playing = true;

  public Animation copy()
  {
    final Animation copy = new Animation();

    for (int i = 0; i < frames.size(); i++)
    {
      copy.frames.add(frames.get(i).copy());
    }

    copy.groupId = groupId;
    copy.loop = loop;
    return copy;
  }

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

  void mergeState(final Animation animation)
  {
    this.elapsed = animation.elapsed;
    this.index = animation.index;
    this.playing = animation.playing;
  }

  boolean isAtEnd()
  {
    return index == frames.size() - 1;
  }

  void reset()
  {
    elapsed = 0.0f;
    index = 0;
    playing = true;
  }
}
