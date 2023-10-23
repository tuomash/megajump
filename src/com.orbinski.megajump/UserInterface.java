package com.orbinski.megajump;

import java.util.ArrayList;
import java.util.List;

import static com.orbinski.megajump.MathUtils.*;

class UserInterface
{
  static final Help help = new Help();
  private static final List<Text> texts = new ArrayList<>();

  static Text waterMarkText;

  static Text pausedText;

  static Text levelNameText;
  static Text elapsedTimeText;
  static Text bestTimeText;

  static Text retryText;
  static Text nextLevelText;
  static Text trophyLevelText;

  static Text goldRequirementText;
  static Text silverRequirementText;
  static Text bronzeRequirementText;

  static void create()
  {
    help.create();

    waterMarkText = createText("Alpha Footage");

    pausedText = createText("PAUSED");

    levelNameText = createText(null);
    elapsedTimeText = createText("Elapsed:");
    bestTimeText = createText("Best time:");

    retryText = createText("Press R to retry");
    retryText.visible = false;
    nextLevelText = createText("Press N for next level");
    nextLevelText.visible = false;
    trophyLevelText = createText("Trophy:");

    goldRequirementText = createText("Gold:");
    silverRequirementText = createText("Silver:");
    bronzeRequirementText = createText("Bronze:");
  }

  static void layout(final int width, final int height)
  {
    System.out.println(width + "x" + height);

    for (int i = 0; i < texts.size(); i++)
    {
      final Text text = texts.get(i);

      if (width <= 1360 && height <= 768)
      {
        text.font = Resources.font14White;
      }
      else if (width <= 1920 && height <= 1080)
      {
        text.font = Resources.font18White;
      }
      else
      {
        text.font = Resources.font24White;
      }
    }

    if (width <= 1360 && height <= 768)
    {
      waterMarkText.setPosition(20, height - 20);

      pausedText.setPosition(20, 280);

      levelNameText.setPosition(20, 40);
      elapsedTimeText.setPosition(20, 80);
      bestTimeText.setPosition(20, 120);

      final int secondHelpX = 260;

      retryText.setPosition(secondHelpX, 40);
      nextLevelText.setPosition(secondHelpX, 80);
      trophyLevelText.setPosition(secondHelpX, 120);

      final int thirdHelpX = 520;

      goldRequirementText.setPosition(thirdHelpX, 120);
      silverRequirementText.setPosition(thirdHelpX, 80);
      bronzeRequirementText.setPosition(thirdHelpX, 40);
    }
    else if (width <= 1920 && height <= 1080)
    {
      waterMarkText.setPosition(20, height - 20);

      pausedText.setPosition(20, 280);

      levelNameText.setPosition(20, 40);
      elapsedTimeText.setPosition(20, 80);
      bestTimeText.setPosition(20, 120);

      final int secondHelpX = 360;

      retryText.setPosition(secondHelpX, 40);
      nextLevelText.setPosition(secondHelpX, 80);
      trophyLevelText.setPosition(secondHelpX, 120);

      final int thirdHelpX = 660;

      goldRequirementText.setPosition(thirdHelpX, 120);
      silverRequirementText.setPosition(thirdHelpX, 80);
      bronzeRequirementText.setPosition(thirdHelpX, 40);
    }
    else
    {
      waterMarkText.setPosition(20, height - 20);

      pausedText.setPosition(20, 280);

      levelNameText.setPosition(20, 40);
      elapsedTimeText.setPosition(20, 80);
      bestTimeText.setPosition(20, 120);

      final int secondHelpX = 460;

      retryText.setPosition(secondHelpX, 40);
      nextLevelText.setPosition(secondHelpX, 80);
      trophyLevelText.setPosition(secondHelpX, 120);

      final int thirdHelpX = 860;

      goldRequirementText.setPosition(thirdHelpX, 120);
      silverRequirementText.setPosition(thirdHelpX, 80);
      bronzeRequirementText.setPosition(thirdHelpX, 40);
    }
  }

  private static Text createText(final String text)
  {
    final Text textObj = new Text();
    textObj.text = text;
    textObj.font = Resources.font24White;
    textObj.visible = true;
    texts.add(textObj);
    return textObj;
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
