package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Texture;

class MTexture
{
  Texture texture;
  int srcX;
  int srcY;
  int srcWidth;
  int srcHeight;

  MTexture copy()
  {
    final MTexture copy = new MTexture();
    copy.texture = texture;
    copy.srcX = srcX;
    copy.srcY = srcY;
    copy.srcWidth = srcWidth;
    copy.srcHeight = srcHeight;
    return copy;
  }
}
