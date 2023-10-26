package com.orbinski.megajump;

class Animations
{
  static Animation playerIdleLeft;
  static Animation playerIdleRight;
  static Animation playerLandLeft;
  static Animation playerLandRight;

  public static void load()
  {
    if (Resources.playerIdleLeft != null)
    {
      final Animation animation = new Animation();
      animation.groupId = 1;
      animation.loop = true;

      for (int i = 0; i < Resources.playerIdleLeft.size(); i++)
      {
        final AnimationFrame frame = new AnimationFrame();
        frame.texture = Resources.playerIdleLeft.get(i);
        frame.length = 0.09f;
        animation.frames.add(frame);
      }

      playerIdleLeft = animation;
    }

    if (Resources.playerIdleRight != null)
    {
      final Animation animation = new Animation();
      animation.groupId = 1;
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

    if (Resources.playerLandLeft != null)
    {
      final Animation animation = new Animation();
      animation.groupId = 2;

      for (int i = 0; i < Resources.playerLandLeft.size(); i++)
      {
        final AnimationFrame frame = new AnimationFrame();
        frame.texture = Resources.playerLandLeft.get(i);
        frame.length = 0.09f;
        animation.frames.add(frame);
      }

      playerLandLeft = animation;
    }

    if (Resources.playerLandRight != null)
    {
      final Animation animation = new Animation();
      animation.groupId = 2;

      for (int i = 0; i < Resources.playerLandRight.size(); i++)
      {
        final AnimationFrame frame = new AnimationFrame();
        frame.texture = Resources.playerLandRight.get(i);
        frame.length = 0.09f;
        animation.frames.add(frame);
      }

      playerLandRight = animation;
    }
  }
}
