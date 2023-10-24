package com.orbinski.megajump;

class Animations
{
  static Animation playerIdleRight;

  public static void load()
  {
    if (Resources.playerIdleRight != null)
    {
      final Animation animation = new Animation();
      animation.loop = true;

      for (int i = 0; i < Resources.playerIdleRight.size(); i++)
      {
        final AnimationFrame frame = new AnimationFrame();
        frame.texture = Resources.playerIdleRight.get(i);
        frame.length = 0.09f;
        animation.frames.add(frame);
      }

      playerIdleRight = animation;
    }
  }
}
