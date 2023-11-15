package com.orbinski.megajump;

import java.util.Random;

public class Globals
{
  static final float WORLD_WIDTH = 200;
  static final float WORLD_HEIGHT = 100;

  public static final float TIME_STEP_SECONDS = 1 / 60.0f;
  public static final float MAX_FRAME_TIME_SECONDS = TIME_STEP_SECONDS * 20.0f;

  static final float GRAVITY = -60.0f;

  public static final int MAX_PLAYER_COUNT = 4;

  public static final float SERVER_TICK_RATE_SECONDS = 1 / 60.0f;
  public static final long SERVER_TICK_RATE_MILLISECONDS = (long) (SERVER_TICK_RATE_SECONDS * 1000.0f);

  static final Random random = new Random();

  static int screenWidth;
  static int screenHeight;

  static boolean debug;
  static boolean watermark;
  static boolean hideUI;
  static boolean editor;
  static String levelTag;
}
