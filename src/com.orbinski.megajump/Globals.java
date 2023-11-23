package com.orbinski.megajump;

import java.util.Random;

public class Globals
{
  public static final float WORLD_WIDTH = 200;
  public static final float WORLD_HEIGHT = 100;

  public static final float TIME_STEP_SECONDS = 1 / 60.0f;
  public static final float MAX_FRAME_TIME_SECONDS = TIME_STEP_SECONDS * 20.0f;

  public static final float MAX_VELOCITY_X = 300.0f;
  public static final float MAX_VELOCITY_Y = 300.0f;
  public static final float MAX_JUMP_VELOCITY_X = 130.0f;
  public static final float MAX_JUMP_VELOCITY_Y = 140.0f;
  public static final float MAX_ONE_JUMP_VELOCITY_X = 65.0f;
  public static final float MAX_ONE_JUMP_VELOCITY_Y = 75.0f;
  public static final float GRAVITY = -60.0f;
  public static final float FRICTION_PLATFORM = 0.8f;
  public static final float FRICTION_PLATFORM_APPLY_AFTER_SECONDS = 0.2f;

  public static final int SERVER_MAX_PLAYER_COUNT = 4;
  public static final int SERVER_MAX_MESSAGES = 10;
  public static final float SERVER_TICK_RATE_SECONDS = 1 / 60.0f;
  public static final long SERVER_TICK_RATE_MILLISECONDS = (long) (SERVER_TICK_RATE_SECONDS * 1000.0f);

  public static final Random random = new Random();

  public static int screenWidth;
  public static int screenHeight;

  public static boolean debug;
  public static boolean watermark;
  public static boolean hideUI;
  public static boolean editor;
  public static String levelTag;
}
