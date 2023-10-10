package com.orbinski.megajump;

class UserInterface
{
  static final Text levelNameText = new Text();
  static final Text elapsedTimeText = new Text();
  static final Text bestTimeText = new Text();

  static void create()
  {
    levelNameText.setX(20);
    levelNameText.setY(40);
    levelNameText.font = UIRenderer.font24White;

    elapsedTimeText.text = "Elapsed:";
    elapsedTimeText.setX(20);
    elapsedTimeText.setY(80);
    elapsedTimeText.font = UIRenderer.font24White;

    bestTimeText.text = "Best time:";
    bestTimeText.setX(20);
    bestTimeText.setY(120);
    bestTimeText.font = UIRenderer.font24White;
  }

  static void updateLevelNameText(final String levelName)
  {
    levelNameText.text = "Level: " + levelName;
  }

  static void updateElapsedTimeText(final float time)
  {
    elapsedTimeText.text = "Elapsed: " + time + "s";
  }

  static void clearElapsedTimeText()
  {
    elapsedTimeText.text = "Elapsed:";
  }

  static void updateBestTimeText(final float time)
  {
    bestTimeText.text = "Best time: " + time + "s";
  }

  static void clearBestTimeText()
  {
    bestTimeText.text = "Best time:";
  }
}
