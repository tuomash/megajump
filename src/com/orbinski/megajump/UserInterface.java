package com.orbinski.megajump;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.ArrayList;
import java.util.List;

import static com.orbinski.megajump.MathUtils.*;

public class UserInterface
{
  static Color DARK_RED = new Color(139.0f / 255.0f, 0.0f, 0.0f, 1.0f);
  static Color DARK_GREEN = new Color(8.0f / 255.0f, 144.0f / 255.0f, 0.0f, 1.0f);

  private static final GlyphLayout glyphLayout = new GlyphLayout();

  static final Help help = new Help();
  private static final List<Text> texts = new ArrayList<>();

  static Text waterMarkText;

  static Text pausedText;

  static Text speedText;
  static Text levelNameText;
  static Text elapsedTimeText;
  static Text bestTimeText;

  static Text retryText;
  static Text nextLevelText;
  static Text trophyLevelText;

  static Text goldRequirementText;
  static Text silverRequirementText;
  static Text bronzeRequirementText;

  static Bar jumpBar;

  // Editor

  static Text unsavedChangesText;
  static Text newLevelNameText;
  static Text commandText;
  static Text edgeScrollingText;
  static Text moveCameraXText;
  static Text moveCameraYText;
  static Text zoomText;
  static Caret caret;

  static void create()
  {
    help.create();

    waterMarkText = createText("Alpha Footage");

    pausedText = createText("PAUSED");

    levelNameText = createText(null);
    elapsedTimeText = createText("Elapsed:");
    bestTimeText = createText("Best time:");
    speedText = createText("Speed:");

    retryText = createText("Press R to retry");
    retryText.visible = false;
    nextLevelText = createText("Press N for next level");
    nextLevelText.visible = false;
    trophyLevelText = createText("Trophy:");

    goldRequirementText = createText("Gold:");
    silverRequirementText = createText("Silver:");
    bronzeRequirementText = createText("Bronze:");

    jumpBar = new Bar();
    jumpBar.setX(20);
    jumpBar.setY(200);
    jumpBar.setHeight(20);
    jumpBar.setWidth(300);
    jumpBar.updateBar(1.0f, 1.0f);
    enableJumpBar();

    unsavedChangesText = createText("Unsaved changes");
    unsavedChangesText.font = Resources.font24Red;
    unsavedChangesText.visible = false;

    newLevelNameText = createText("Rename level to: ");
    newLevelNameText.visible = false;

    commandText = createText("cmd: ");
    commandText.visible = false;

    edgeScrollingText = createText("Edge scroll: ");
    moveCameraXText = createText("Move cam x: ");
    moveCameraYText = createText("Move cam y: ");
    zoomText = createText("Zoom: ");

    caret = new Caret();
  }

  static void layout(final int width, final int height)
  {
    // System.out.println(width + "x" + height);

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
        if (text.font != null)
        {
          if (text.font.getColor() == Color.WHITE)
          {
            text.font = Resources.font24White;
          }
          else if (text.font.getColor() == Color.RED)
          {
            text.font = Resources.font24Red;
          }
        }
        else
        {
          text.font = Resources.font24White;
        }
      }
    }

    if (width <= 1360 && height <= 768)
    {
      waterMarkText.setPosition(width - 120, height - 20);

      pausedText.setPosition(20, 280);

      levelNameText.setPosition(20, 20);
      elapsedTimeText.setPosition(20, 40);
      bestTimeText.setPosition(20, 60);
      speedText.setPosition(20, 80);

      final int secondHelpX = 260;

      retryText.setPosition(secondHelpX, 20);
      nextLevelText.setPosition(secondHelpX, 40);
      trophyLevelText.setPosition(secondHelpX, 60);

      final int thirdHelpX = 520;

      bronzeRequirementText.setPosition(thirdHelpX, 20);
      silverRequirementText.setPosition(thirdHelpX, 40);
      goldRequirementText.setPosition(thirdHelpX, 60);

      unsavedChangesText.setPosition(20, 40);
      newLevelNameText.setPosition(20, 20);
      commandText.setPosition(20, 20);

      edgeScrollingText.setPosition(secondHelpX, 60);
      moveCameraXText.setPosition(secondHelpX, 40);
      moveCameraYText.setPosition(secondHelpX, 20);

      zoomText.setPosition(thirdHelpX, 20);
    }
    else if (width <= 1920 && height <= 1080)
    {
      waterMarkText.setPosition(width - 160, height - 20);

      pausedText.setPosition(20, 280);

      levelNameText.setPosition(20, 30);
      elapsedTimeText.setPosition(20, 60);
      bestTimeText.setPosition(20, 90);
      speedText.setPosition(20, 120);

      final int secondHelpX = 360;

      retryText.setPosition(secondHelpX, 30);
      nextLevelText.setPosition(secondHelpX, 60);
      trophyLevelText.setPosition(secondHelpX, 90);

      final int thirdHelpX = 660;

      bronzeRequirementText.setPosition(thirdHelpX, 30);
      silverRequirementText.setPosition(thirdHelpX, 60);
      goldRequirementText.setPosition(thirdHelpX, 90);

      unsavedChangesText.setPosition(20, 60);
      newLevelNameText.setPosition(20, 30);
      commandText.setPosition(20, 30);

      edgeScrollingText.setPosition(secondHelpX, 90);
      moveCameraXText.setPosition(secondHelpX, 60);
      moveCameraYText.setPosition(secondHelpX, 30);

      zoomText.setPosition(thirdHelpX, 30);
    }
    else
    {
      waterMarkText.setPosition(width - 200, height - 20);

      pausedText.setPosition(20, 280);

      levelNameText.setPosition(20, 40);
      elapsedTimeText.setPosition(20, 80);
      bestTimeText.setPosition(20, 120);
      speedText.setPosition(20, 160);

      final int secondHelpX = 460;

      retryText.setPosition(secondHelpX, 40);
      nextLevelText.setPosition(secondHelpX, 80);
      trophyLevelText.setPosition(secondHelpX, 120);

      final int thirdHelpX = 860;

      goldRequirementText.setPosition(thirdHelpX, 120);
      silverRequirementText.setPosition(thirdHelpX, 80);
      bronzeRequirementText.setPosition(thirdHelpX, 40);

      unsavedChangesText.setPosition(20, 80);
      newLevelNameText.setPosition(20, 40);
      commandText.setPosition(20, 40);

      edgeScrollingText.setPosition(secondHelpX, 120);
      moveCameraXText.setPosition(secondHelpX, 80);
      moveCameraYText.setPosition(secondHelpX, 40);

      zoomText.setPosition(thirdHelpX, 40);
    }

    jumpBar.setX(width - jumpBar.getWidth() - 20);
    jumpBar.setY(20);

    // Help texts

    {
      final int x = 20;

      int y = height - 20;

      for (int i = 0; i < help.gameTexts.size(); i++)
      {
        final Text text = help.gameTexts.get(i);
        text.setX(x);
        text.setY(y);

        y = y - 40;
      }

      y = height - 20;

      for (int i = 0; i < help.editorTexts.size(); i++)
      {
        final Text text = help.editorTexts.get(i);
        text.setX(x);
        text.setY(y);

        y = y - 40;
      }
    }
  }

  public static void update(final float delta)
  {
    if (caret != null)
    {
      caret.update(delta);
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

  public static void updateLevelNameText(final String levelName)
  {
    if (levelNameText != null)
    {
      levelNameText.text = "Level: " + levelName;
    }
  }

  static void updateElapsedTimeText(final int milliseconds)
  {
    if (elapsedTimeText != null)
    {
      elapsedTimeText.text = "Elapsed: " + toTimestamp(milliseconds);
    }
  }

  static void clearElapsedTimeText()
  {
    if (elapsedTimeText != null)
    {
      elapsedTimeText.text = "Elapsed:";
    }
  }

  static void updateBestTimeText(final int milliseconds)
  {
    if (bestTimeText != null)
    {
      bestTimeText.text = "Best time: " + toTimestamp(milliseconds);
    }
  }

  static void updateSpeedText(final int velocityX, final int velocityY)
  {
    if (speedText != null)
    {
      speedText.text = "Speed: x" + velocityX + " y" + velocityY;
    }
  }

  static void clearBestTimeText()
  {
    if (bestTimeText != null)
    {
      bestTimeText.text = "Best time:";
    }
  }

  static void updateTrophyLevelText(final Level.Trophy trophy)
  {
    if (trophyLevelText != null)
    {
      trophyLevelText.text = "Trophy: " + trophy;
    }
  }

  static void updateGoldRequirementText(final int milliseconds)
  {
    if (goldRequirementText != null)
    {
      goldRequirementText.text = "Gold: " + toTimestamp(milliseconds);
    }
  }

  static void updateSilverRequirementText(final int milliseconds)
  {
    if (silverRequirementText != null)
    {
      silverRequirementText.text = "Silver: " + toTimestamp(milliseconds);
    }
  }

  static void updateBronzeRequirementText(final int milliseconds)
  {
    if (bronzeRequirementText != null)
    {
      bronzeRequirementText.text = "Bronze: " + toTimestamp(milliseconds);
    }
  }

  public static void enableJumpBar()
  {
    if (jumpBar != null)
    {
      jumpBar.color = Color.PURPLE;
    }
  }

  public static void disableJumpBar()
  {
    if (jumpBar != null)
    {
      jumpBar.color = Color.GRAY;
    }
  }

  static void updateNewLevelNameText(final String levelName)
  {
    if (newLevelNameText != null)
    {
      newLevelNameText.text = "Rename level to: " + levelName;
      updateCaretPosition();
    }
  }

  static void updateCommandText(final String cmd)
  {
    if (commandText != null)
    {
      commandText.text = "cmd: " + cmd;
      updateCaretPosition();
    }
  }

  static void updateEdgeScrollingText(final boolean scroll)
  {
    if (edgeScrollingText != null)
    {
      edgeScrollingText.text = "Edge scroll: " + scroll;
    }
  }

  static void updateMoveCameraXText(final boolean move)
  {
    if (moveCameraXText != null)
    {
      moveCameraXText.text = "Move cam x: " + move;
    }
  }

  static void updateMoveCameraYText(final boolean move)
  {
    if (moveCameraYText != null)
    {
      moveCameraYText.text = "Move cam y: " + move;
    }
  }

  static void updateZoomText(final float zoom)
  {
    if (zoomText != null)
    {
      zoomText.text = "Zoom: " + zoom;
    }
  }

  static void updateCaretPosition()
  {
    if (caret != null && caret.visible)
    {
      if (newLevelNameText != null && newLevelNameText.visible)
      {
        glyphLayout.setText(newLevelNameText.font, newLevelNameText.text);
        caret.setPosition(newLevelNameText.getX() + (int) glyphLayout.width + 5, newLevelNameText.getY() - 20);
      }
      else if (commandText != null && commandText.visible)
      {
        glyphLayout.setText(commandText.font, commandText.text);
        caret.setPosition(commandText.getX() + (int) glyphLayout.width + 5, commandText.getY() - 20);
      }
    }
  }
}
