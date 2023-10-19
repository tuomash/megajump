package com.orbinski.megajump;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class UIRenderer
{
  final Game game;
  final OrthographicCamera camera;
  final Viewport viewport;
  final SpriteBatch spriteBatch;

  final Text[] texts = new Text[50];
  int textIndex = -1;

  UIRenderer(final Game game)
  {
    this.game = game;

    camera = new OrthographicCamera();
    camera.update();

    viewport = new ScreenViewport(camera);

    spriteBatch = new SpriteBatch();
    spriteBatch.setProjectionMatrix(camera.combined);
  }

  void render()
  {
    viewport.apply();
    spriteBatch.setProjectionMatrix(camera.combined);

    textIndex = -1;

    if (game.help)
    {
      addHelpTexts();
    }
    else
    {
      if (Globals.watermark)
      {
        addText(UserInterface.waterMarkText);
      }

      if (game.paused)
      {
        addText(UserInterface.pausedText);
      }

      addText(UserInterface.levelNameText);
      addText(UserInterface.elapsedTimeText);
      addText(UserInterface.bestTimeText);

      addText(UserInterface.retryText);
      addText(UserInterface.nextLevelText);
      addText(UserInterface.trophyLevelText);

      addText(UserInterface.goldRequirementText);
      addText(UserInterface.silverRequirementText);
      addText(UserInterface.bronzeRequirementText);
    }

    renderTexts();
  }

  void addHelpTexts()
  {
    for (int i = 0; i < UserInterface.help.texts.size(); i++)
    {
      final Text text = UserInterface.help.texts.get(i);
      addText(text);
    }
  }

  void addText(final Text text)
  {
    textIndex++;

    if (textIndex >= texts.length)
    {
      textIndex = texts.length - 1;
    }

    texts[textIndex] = text;
  }

  void renderTexts()
  {
    if (textIndex == -1)
    {
      return;
    }

    spriteBatch.begin();

    for (int i = 0; i <= textIndex; i++)
    {
      final Text text =  texts[i];

      if (text != null && text.visible && text.font != null && text.text != null && !text.text.isEmpty())
      {
        text.font.draw(spriteBatch,
                       text.text,
                       text.getX(),
                       text.getY());

      }
    }

    spriteBatch.end();
  }

  void renderUIElement(final UIElement element)
  {
    if (element != null)
    {
      if (element.backgroundColor != null)
      {
        /*
        renderHudFilledQuad(element.getX(),
                            element.getY(),
                            element.getWidth(),
                            element.getHeight(),
                            element.backgroundColor);
         */
      }

      if (element.texture != null)
      {
        spriteBatch.begin();
        spriteBatch.draw(element.texture, element.getX(), element.getY(), element.getWidth(), element.getHeight());
        spriteBatch.end();
      }

      if (element.getOverlay() != null)
      {
        // renderOverlay(element.getOverlay());
      }
    }
  }
}
