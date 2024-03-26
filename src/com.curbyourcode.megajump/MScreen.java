package com.orbinski.megajump;

public class MScreen
{
  private static float leftAreaX;
  private static float rightAreaX;
  private static float topAreaY;
  private static float bottomAreaY;

  public static void layout(final int width, final int height)
  {
    final float areaWidth = width * 0.03f;
    leftAreaX = areaWidth;
    rightAreaX = width - areaWidth;

    final float areaHeight = height * 0.04f;
    topAreaY = areaHeight;
    bottomAreaY = height - areaHeight;
  }

  public static boolean isInTopArea(final float inputY)
  {
    return inputY < topAreaY;
  }

  public static boolean isInBottomArea(final float inputY)
  {
    return inputY > bottomAreaY;
  }

  public static boolean isInLeftArea(final float inputX)
  {
    return inputX < leftAreaX;
  }

  public static boolean isInRightArea(final float inputX)
  {
    return inputX > rightAreaX;
  }
}
