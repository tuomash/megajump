package com.orbinski.megajump;

import static com.orbinski.megajump.MathUtils.*;

class UserInterface
{
  static final Text levelNameText = new Text();
  static final Text elapsedTimeText = new Text();
  static final Text bestTimeText = new Text();

  static final Text retryText = new Text();
  static final Text nextLevelText = new Text();
  static final Text trophyLevelText = new Text();

  static final Text goldRequirementText = new Text();
  static final Text silverRequirementText = new Text();
  static final Text bronzeRequirementText = new Text();

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

    final int secondHelpX = 460;

    retryText.text = "Press R to retry";
    retryText.setX(secondHelpX);
    retryText.setY(40);
    retryText.font = UIRenderer.font24White;
    retryText.visible = false;

    nextLevelText.text = "Press N for next level";
    nextLevelText.setX(secondHelpX);
    nextLevelText.setY(80);
    nextLevelText.font = UIRenderer.font24White;
    nextLevelText.visible = false;

    trophyLevelText.text = "Trophy:";
    trophyLevelText.setX(secondHelpX);
    trophyLevelText.setY(120);
    trophyLevelText.font = UIRenderer.font24White;
    trophyLevelText.visible = true;

    final int thirdHelpX = 860;

    goldRequirementText.text = "Gold:";
    goldRequirementText.setX(thirdHelpX);
    goldRequirementText.setY(120);
    goldRequirementText.font = UIRenderer.font24White;
    goldRequirementText.visible = true;

    silverRequirementText.text = "Silver:";
    silverRequirementText.setX(thirdHelpX);
    silverRequirementText.setY(80);
    silverRequirementText.font = UIRenderer.font24White;
    silverRequirementText.visible = true;

    bronzeRequirementText.text = "Bronze:";
    bronzeRequirementText.setX(thirdHelpX);
    bronzeRequirementText.setY(40);
    bronzeRequirementText.font = UIRenderer.font24White;
    bronzeRequirementText.visible = true;
  }

  static void updateLevelNameText(final String levelName)
  {
    levelNameText.text = "Level: " + levelName;
  }

  static void updateElapsedTimeText(final int milliseconds)
  {
    elapsedTimeText.text = "Elapsed: " + toTimestamp(milliseconds);
  }

  static void clearElapsedTimeText()
  {
    elapsedTimeText.text = "Elapsed:";
  }

  static void updateBestTimeText(final int milliseconds)
  {
    bestTimeText.text = "Best time: " + toTimestamp(milliseconds);
  }

  static void clearBestTimeText()
  {
    bestTimeText.text = "Best time:";
  }

  static void updateTrophyLevelText(final Level.Trophy trophy)
  {
    trophyLevelText.text = "Trophy: " + trophy;
  }

  static void updateGoldRequirementText(final int milliseconds)
  {
    goldRequirementText.text = "Gold: " + toTimestamp(milliseconds);
  }

  static void updateSilverRequirementText(final int milliseconds)
  {
    silverRequirementText.text = "Silver: " + toTimestamp(milliseconds);
  }

  static void updateBronzeRequirementText(final int milliseconds)
  {
    bronzeRequirementText.text = "Bronze: " + toTimestamp(milliseconds);
  }
}
