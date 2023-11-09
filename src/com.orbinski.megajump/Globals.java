package com.orbinski.megajump;

import java.util.Random;

class Globals
{
  static final float WORLD_WIDTH = 200;
  static final float WORLD_HEIGHT = 100;

  static final float TIME_STEP_SECONDS = 1 / 60.0f;
  static final float MAX_FRAME_TIME = TIME_STEP_SECONDS * 20.0f;
  static final int MAX_UPDATES = 20;

  static final float GRAVITY = -0.6f;

  static final Random random = new Random();

  static int screenWidth;
  static int screenHeight;

  static boolean debug;
  static boolean watermark;
  static boolean hideUI;
  static boolean editor;
  static String levelTag;
}
