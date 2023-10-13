package com.orbinski.megajump;

import static com.orbinski.megajump.Globals.random;

class MathUtils
{
  static float distance(final float x1,
                        final float y1,
                        final float x2,
                        final float y2)
  {
    final float distX = x2 - x1;
    final float distY = y2 - y1;
    return (float) Math.sqrt(distX * distX + distY * distY);
  }

  static int random(final int low, final int high)
  {
    return random.nextInt(high - low) + low;
  }

  static String toTimestamp(final int inputInMilliseconds)
  {
    final int seconds = toSeconds(inputInMilliseconds);
    final int milliseconds = getMillisecondRemainders(inputInMilliseconds);
    return seconds + "." + milliseconds + "s";
  }

  static int toSeconds(final int milliseconds)
  {
    return milliseconds / 1000;
  }

  static int getMillisecondRemainders(final int milliseconds)
  {
    return milliseconds % 1000;
  }
}
