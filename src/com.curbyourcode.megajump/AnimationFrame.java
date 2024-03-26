package com.orbinski.megajump;

public class AnimationFrame
{
  MTexture texture;
  float length;

  public AnimationFrame copy()
  {
    final AnimationFrame copy = new AnimationFrame();
    copy.texture = texture;
    copy.length = length;
    return copy;
  }
}
