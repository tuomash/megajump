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

      for (int i = 0; i < 10; i++)
      {
        final AnimationFrame frame = new AnimationFrame();
        frame.texture = Resources.playerIdleRight;
        frame.length = 0.09f;
        frame.srcX = i * 48;
        frame.srcY = 0;
        frame.srcWidth = 48;
        frame.srcHeight = 48;
        animation.frames.add(frame);
      }

      playerIdleRight = animation;
    }
  }
}
